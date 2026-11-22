package zin.rashidi.boot.security.onetimetoken.token;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.security.authentication.ott.JdbcOneTimeTokenService;
import org.springframework.security.authentication.ott.OneTimeTokenService;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.ott.DefaultGenerateOneTimeTokenRequestResolver;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.Map;

@Configuration
@EnableWebSecurity
class OneTimeTokenSecurityConfiguration {

    @Bean
    JdbcOneTimeTokenService oneTimeTokenService(JdbcOperations jdbcOperations) {
        return new JdbcOneTimeTokenService(jdbcOperations);
    }

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            OneTimeTokenService oneTimeTokenService,
            OneTimeTokenGenerationHandler generationHandler,
            ObjectMapper objectMapper
    ) throws Exception {
        var requestResolver = new DefaultGenerateOneTimeTokenRequestResolver();
        requestResolver.setExpiresIn(Duration.ofMinutes(5));

        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.ignoringRequestMatchers("/ott/generate", "/login/ott"))
                .exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    objectMapper.writeValue(response.getWriter(), Map.of("error", "Authentication required"));
                }))
                .oneTimeTokenLogin(ott -> ott
                        .tokenService(oneTimeTokenService)
                        .generateRequestResolver(requestResolver)
                        .tokenGenerationSuccessHandler(generationHandler)
                        .showDefaultSubmitPage(false)
                        .successHandler((request, response, authentication) -> {
                            response.setStatus(HttpStatus.OK.value());
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            objectMapper.writeValue(response.getWriter(), Map.of(
                                    "username", authentication.getName(),
                                    "authenticated", true
                            ));
                        })
                        .failureHandler((request, response, exception) -> {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            objectMapper.writeValue(response.getWriter(), Map.of(
                                    "error", "Invalid or expired one-time token"
                            ));
                        })
                )
                .build();
    }

}
