package zin.rashidi.boot.jdbi.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.postgres.PostgresPlugin;
import org.jdbi.v3.spring.EnableJdbiRepositories;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Rashidi Zin
 */
@Configuration
@EnableJdbiRepositories(basePackages = "zin.rashidi.boot.jdbi")
public class RepositoryConfiguration {

    @Bean
    public Jdbi jdbi(HikariDataSource dataSource) {
        return Jdbi.create(dataSource)
                .installPlugin(new PostgresPlugin())
                .installPlugin(new SqlObjectPlugin());
    }

}
