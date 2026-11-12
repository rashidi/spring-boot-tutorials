package zin.rashidi.boot.modulith;

import org.springframework.boot.SpringApplication;

public class TestModulithApplication {

    public static void main(String[] args) {
        SpringApplication.from(ModulithApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
