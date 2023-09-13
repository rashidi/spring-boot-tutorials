package zin.rashidi.boot.batch.user;

import static org.springframework.web.reactive.function.client.support.WebClientAdapter.forClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * @author Rashidi Zin
 */
@Configuration
class UserWebClientConfiguration {

    @Bean
    public UserRepository userRepository(WebClient webClient) {
        return HttpServiceProxyFactory.builder(forClient(webClient))
                .build()
                .createClient(UserRepository.class);
    }

}
