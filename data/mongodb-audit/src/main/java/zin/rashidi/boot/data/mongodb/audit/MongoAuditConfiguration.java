package zin.rashidi.boot.data.mongodb.audit;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

/**
 * @author Rashidi Zin
 */
@Configuration
@EnableMongoAuditing
class MongoAuditConfiguration {

    @Bean
    public AuditorAware<String> auditorAwareRef() {
        return () -> Optional.of("Mr. Auditor");
    }

}
