package zin.rashidi.boot.web.apiversioning.versioning;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.accept.ApiVersionDeprecationHandler;
import org.springframework.web.accept.StandardApiVersionDeprecationHandler;

import java.net.URI;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Configuration
class ApiVersioningConfiguration {

    static final ZonedDateTime V1_DEPRECATION_DATE = ZonedDateTime.of(2026, 6, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    static final ZonedDateTime V1_SUNSET_DATE = ZonedDateTime.of(2027, 6, 1, 0, 0, 0, 0, ZoneOffset.UTC);
    static final URI V1_MIGRATION_GUIDE = URI.create("https://rashidi.zin.my/spring-boot-tutorials/web-api-versioning.html");

    @Bean
    ApiVersionDeprecationHandler apiVersionDeprecationHandler() {
        var handler = new StandardApiVersionDeprecationHandler();
        handler.configureVersion("1.0")
                .setDeprecationDate(V1_DEPRECATION_DATE)
                .setSunsetDate(V1_SUNSET_DATE)
                .setDeprecationLink(V1_MIGRATION_GUIDE);
        return handler;
    }

}
