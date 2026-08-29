package zin.rashidi.boot.modulith.events;

import org.springframework.boot.SpringApplication;

/**
 * @author Rashidi Zin
 */
public class TestModulithEventsApplication {

    public static void main(String[] args) {
        SpringApplication.from(ModulithEventsApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}