package zin.rashidi.boot.ai.toolcalling;

import org.springframework.boot.SpringApplication;

/**
 * @author Rashidi Zin
 */
public class TestAiToolCallingApplication {

    public static void main(String[] args) {
        SpringApplication.from(AiToolCallingApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}