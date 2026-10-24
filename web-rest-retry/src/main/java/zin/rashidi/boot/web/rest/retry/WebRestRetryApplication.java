package zin.rashidi.boot.web.rest.retry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

@EnableRetry
@SpringBootApplication
public class WebRestRetryApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebRestRetryApplication.class, args);
    }
}
