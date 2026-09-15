package zin.rashidi.boot.ai.redis;

import org.springframework.boot.SpringApplication;

public class TestAiRedisApplication {

    public static void main(String[] args) {
        SpringApplication.from(AiRedisApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
