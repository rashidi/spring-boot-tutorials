package zin.rashidi.boot.data.jdbc.audit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;

import java.util.Optional;

/**
 * @author Rashidi Zin
 */
@Configuration
@EnableJdbcAuditing
class AuditConfiguration {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of("Mr. Auditor");
    }

}
