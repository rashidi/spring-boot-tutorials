package zin.rashidi.boot.ai.chroma;

import org.springframework.boot.SpringApplication;

/**
 * @author Rashidi Zin
 */
public class TestAiChromaApplication {

    public static void main(String[] args) {
        SpringApplication.from(AiChromaApplication::main)
                .with(TestcontainersConfiguration.class)
                .run(args);
    }

}
