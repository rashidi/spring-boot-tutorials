package zin.rashidi.boot.data.jpa.filteredquery;

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
