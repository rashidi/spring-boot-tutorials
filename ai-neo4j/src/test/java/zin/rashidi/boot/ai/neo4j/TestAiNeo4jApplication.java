package zin.rashidi.boot.ai.neo4j;

import org.springframework.boot.SpringApplication;

public class TestAiNeo4jApplication {

    public static void main(String[] args) {
        SpringApplication.from(AiNeo4jApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
