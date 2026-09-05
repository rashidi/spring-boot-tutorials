package zin.rashidi.boot.ai.pgvector;

import org.springframework.boot.SpringApplication;

public class TestAiPgvectorApplication {

    public static void main(String[] args) {
        SpringApplication.from(AiPgvectorApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
