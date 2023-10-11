package zin.rashidi.boot.data.jpa.jpa;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author Rashidi Zin
 */
@Configuration
@EnableJpaRepositories(
        basePackages = "zin.rashidi.boot.data.jpa",
        repositoryBaseClass = JpaCustomBaseRepository.class
)
class JpaConfiguration {

}
