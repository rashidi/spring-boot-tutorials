package zin.rashidi.boot.web.problemdetails;

import org.springframework.boot.SpringApplication;

public class TestWebProblemDetailsApplication {

    public static void main(String[] args) {
        SpringApplication.from(WebProblemDetailsApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
