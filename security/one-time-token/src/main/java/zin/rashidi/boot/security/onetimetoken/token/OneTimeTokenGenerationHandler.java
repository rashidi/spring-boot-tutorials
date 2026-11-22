package zin.rashidi.boot.security.onetimetoken.token;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.ott.OneTimeToken;
import org.springframework.security.web.authentication.ott.OneTimeTokenGenerationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tools.jackson.databind.ObjectMapper;
import zin.rashidi.boot.security.onetimetoken.account.AccountDirectory;

import java.io.IOException;
import java.util.Map;

@Component
class OneTimeTokenGenerationHandler implements OneTimeTokenGenerationSuccessHandler {

    private final AccountDirectory accountDirectory;
    private final ApplicationEventPublisher eventPublisher;
    private final ObjectMapper objectMapper;

    OneTimeTokenGenerationHandler(
            AccountDirectory accountDirectory,
            ApplicationEventPublisher eventPublisher,
            ObjectMapper objectMapper
    ) {
        this.accountDirectory = accountDirectory;
        this.eventPublisher = eventPublisher;
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, OneTimeToken oneTimeToken)
            throws IOException {
        accountDirectory.findActiveByUsername(oneTimeToken.getUsername()).ifPresent(account -> {
            var magicLink = ServletUriComponentsBuilder.fromContextPath(request)
                    .path("/login/ott")
                    .queryParam("token", oneTimeToken.getTokenValue())
                    .build()
                    .toUri();

            eventPublisher.publishEvent(new OneTimeTokenIssuedEvent(
                    account.username(),
                    account.email(),
                    oneTimeToken.getTokenValue(),
                    magicLink,
                    oneTimeToken.getExpiresAt()
            ));
        });

        response.setStatus(HttpStatus.ACCEPTED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(
                response.getWriter(),
                Map.of("message", "If an active account exists, a one-time login link has been sent.")
        );
    }

}
