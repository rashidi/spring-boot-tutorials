package zin.rashidi.boot.cloud.jdbcenvrepo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class CloudJdbcEnvRepoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudJdbcEnvRepoApplication.class, args);
	}

}
