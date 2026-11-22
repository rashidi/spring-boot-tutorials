package zin.rashidi.boot.security.onetimetoken;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.authentication.ott.JdbcOneTimeTokenService;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.util.LinkedMultiValueMap;
import zin.rashidi.boot.security.onetimetoken.delivery.TokenDeliveryLog;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@AutoConfigureRestTestClient
@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DisplayName("Given passwordless one-time token authentication backed by PostgreSQL")
class OneTimeTokenAuthenticationTests {

    @Autowired
    private RestTestClient restClient;

    @Autowired
    private JdbcClient jdbcClient;

    @Autowired
    private TokenDeliveryLog deliveryLog;

    @Autowired
    private JdbcOneTimeTokenService oneTimeTokenService;

    @BeforeEach
    void resetDatabase() {
        jdbcClient.sql("TRUNCATE TABLE one_time_tokens, token_deliveries, accounts RESTART IDENTITY")
                .update();
        jdbcClient.sql("""
                INSERT INTO accounts (username, email, display_name, role, enabled)
                VALUES ('rashidi', 'rashidi@zin.my', 'Rashidi Zin', 'USER', TRUE),
                       ('suspended', 'suspended@zin.my', 'Suspended User', 'USER', FALSE)
                """)
                .update();
    }

    @Nested
    @DisplayName("Given an unauthenticated client")
    class UnauthenticatedClient {

        @Test
        @DisplayName("When GET /api/accounts/me is requested without a session, Then status is 401 Unauthorized")
        void rejectsUnauthenticatedProfileAccess() {
            restClient.get()
                    .uri("/api/accounts/me")
                    .exchange()
                    .expectStatus().isUnauthorized()
                    .expectBody()
                    .jsonPath("$.error").isEqualTo("Authentication required");
        }

    }

    @Nested
    @DisplayName("Given an active registered account")
    class ActiveRegisteredAccount {

        @Test
        @DisplayName("When a one-time token is generated and redeemed, Then the token is persisted, delivered, consumed, and grants access to GET /api/accounts/me")
        void authenticatesWithOneTimeTokenAndAccessesProtectedProfile() {
            requestOneTimeToken("rashidi");

            var deliveries = deliveryLog.findByUsername("rashidi");
            assertThat(deliveries).hasSize(1);

            var delivery = deliveryLog.findLatestByUsername("rashidi").orElseThrow();
            assertThat(delivery.recipientEmail()).isEqualTo("rashidi@zin.my");
            assertThat(delivery.tokenValue()).isNotBlank();
            assertThat(delivery.magicLink().getPath()).isEqualTo("/login/ott");
            assertThat(delivery.magicLink().getQuery()).isEqualTo("token=" + delivery.tokenValue());
            assertThat(delivery.expiresAt()).isAfter(Instant.now());
            assertThat(delivery.deliveredAt()).isNotNull();
            assertThat(countStoredTokens(delivery.tokenValue())).isEqualTo(1);

            var sessionCookie = redeemTokenAndExtractSessionCookie(delivery.tokenValue(), "rashidi");

            assertThat(countStoredTokens(delivery.tokenValue())).isZero();

            restClient.get()
                    .uri("/api/accounts/me")
                    .header(HttpHeaders.COOKIE, sessionCookie)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.username").isEqualTo("rashidi")
                    .jsonPath("$.email").isEqualTo("rashidi@zin.my")
                    .jsonPath("$.displayName").isEqualTo("Rashidi Zin")
                    .jsonPath("$.role").isEqualTo("USER");
        }

        @Test
        @DisplayName("When an authenticated account is disabled before requesting GET /api/accounts/me, Then status is 404 Not Found")
        void returnsNotFoundWhenAuthenticatedAccountIsNoLongerActive() {
            requestOneTimeToken("rashidi");
            var tokenValue = deliveryLog.findLatestByUsername("rashidi").orElseThrow().tokenValue();
            var sessionCookie = redeemTokenAndExtractSessionCookie(tokenValue, "rashidi");

            jdbcClient.sql("UPDATE accounts SET enabled = FALSE WHERE username = 'rashidi'")
                    .update();

            restClient.get()
                    .uri("/api/accounts/me")
                    .header(HttpHeaders.COOKIE, sessionCookie)
                    .exchange()
                    .expectStatus().isNotFound();
        }

        @Test
        @DisplayName("When an already-consumed one-time token is submitted again, Then authentication fails with 401 Unauthorized")
        void rejectsReusedOneTimeToken() {
            requestOneTimeToken("rashidi");
            var tokenValue = deliveryLog.findLatestByUsername("rashidi").orElseThrow().tokenValue();

            redeemTokenAndExtractSessionCookie(tokenValue, "rashidi");

            var replayForm = new LinkedMultiValueMap<String, String>();
            replayForm.add("token", tokenValue);

            restClient.post()
                    .uri("/login/ott")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(replayForm)
                    .exchange()
                    .expectStatus().isUnauthorized()
                    .expectBody()
                    .jsonPath("$.error").isEqualTo("Invalid or expired one-time token");
        }

        @Test
        @DisplayName("When an expired one-time token is submitted, Then authentication fails with 401 Unauthorized and cleanupExpiredTokens removes it")
        void rejectsExpiredOneTimeTokenAndCleansUp() {
            requestOneTimeToken("rashidi");
            var tokenValue = deliveryLog.findLatestByUsername("rashidi").orElseThrow().tokenValue();

            jdbcClient.sql("UPDATE one_time_tokens SET expires_at = :expiredAt WHERE token_value = :tokenValue")
                    .param("expiredAt", Timestamp.from(Instant.now().minus(10, ChronoUnit.MINUTES)))
                    .param("tokenValue", tokenValue)
                    .update();

            var loginForm = new LinkedMultiValueMap<String, String>();
            loginForm.add("token", tokenValue);

            restClient.post()
                    .uri("/login/ott")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(loginForm)
                    .exchange()
                    .expectStatus().isUnauthorized()
                    .expectBody()
                    .jsonPath("$.error").isEqualTo("Invalid or expired one-time token");

            oneTimeTokenService.cleanupExpiredTokens();
            assertThat(countStoredTokens(tokenValue)).isZero();
        }

    }

    @Nested
    @DisplayName("Given an unknown or disabled account")
    class UnknownOrDisabledAccount {

        @Test
        @DisplayName("When a one-time token is requested for an unknown username, Then status is 202 Accepted without delivering a magic link")
        void doesNotDeliverTokenForUnknownUsername() {
            requestOneTimeToken("ghost");

            assertThat(deliveryLog.findByUsername("ghost")).isEmpty();
            assertThat(deliveryLog.findLatestByUsername("ghost")).isEmpty();
        }

        @Test
        @DisplayName("When a one-time token is requested for a disabled account, Then status is 202 Accepted without delivering a magic link")
        void doesNotDeliverTokenForDisabledAccount() {
            requestOneTimeToken("suspended");

            assertThat(deliveryLog.findByUsername("suspended")).isEmpty();
        }

        @Test
        @DisplayName("When a persisted token for a disabled or unknown account is submitted to POST /login/ott, Then authentication fails with 401 Unauthorized")
        void rejectsTokenForDisabledOrUnknownAccount() {
            requestOneTimeToken("suspended");
            requestOneTimeToken("ghost");

            var tokens = jdbcClient.sql("SELECT token_value FROM one_time_tokens WHERE username IN ('suspended', 'ghost')")
                    .query(String.class)
                    .list();
            assertThat(tokens).hasSize(2);

            for (var token : tokens) {
                var loginForm = new LinkedMultiValueMap<String, String>();
                loginForm.add("token", token);

                restClient.post()
                        .uri("/login/ott")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .body(loginForm)
                        .exchange()
                        .expectStatus().isUnauthorized()
                        .expectBody()
                        .jsonPath("$.error").isEqualTo("Invalid or expired one-time token");
            }
        }

        @Test
        @DisplayName("When an invalid one-time token is submitted to POST /login/ott, Then authentication fails with 401 Unauthorized")
        void rejectsUnknownToken() {
            var loginForm = new LinkedMultiValueMap<String, String>();
            loginForm.add("token", "00000000-0000-0000-0000-000000000000");

            restClient.post()
                    .uri("/login/ott")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(loginForm)
                    .exchange()
                    .expectStatus().isUnauthorized()
                    .expectBody()
                    .jsonPath("$.error").isEqualTo("Invalid or expired one-time token");
        }

    }

    private void requestOneTimeToken(String username) {
        var generateForm = new LinkedMultiValueMap<String, String>();
        generateForm.add("username", username);

        restClient.post()
                .uri("/ott/generate")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(generateForm)
                .exchange()
                .expectStatus().isAccepted()
                .expectBody()
                .jsonPath("$.message").isEqualTo("If an active account exists, a one-time login link has been sent.");
    }

    private String redeemTokenAndExtractSessionCookie(String tokenValue, String expectedUsername) {
        var loginForm = new LinkedMultiValueMap<String, String>();
        loginForm.add("token", tokenValue);

        var result = restClient.post()
                .uri("/login/ott")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(loginForm)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.username").isEqualTo(expectedUsername)
                .jsonPath("$.authenticated").isEqualTo(true)
                .returnResult();

        var setCookie = Objects.requireNonNull(
                result.getResponseHeaders().getFirst(HttpHeaders.SET_COOKIE),
                "Expected Set-Cookie header on successful one-time token login"
        );
        return setCookie.split(";", 2)[0];
    }

    private long countStoredTokens(String tokenValue) {
        return jdbcClient.sql("SELECT COUNT(*) FROM one_time_tokens WHERE token_value = :tokenValue")
                .param("tokenValue", tokenValue)
                .query(Long.class)
                .single();
    }

}
