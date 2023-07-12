package zin.rashidi.boot.data.audit;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author Rashidi Zin, GfK
 */
@Configuration
@EnableJpaAuditing
class AuditConfiguration {

    @Bean
    public AuditorAware<String> auditorAwareRef() {
        return () -> Optional.of("Mr. Auditor");
    }

}
