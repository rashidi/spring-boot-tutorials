package zin.rashidi.boot.modulith.externalization;

import org.springframework.boot.SpringApplication;

/**
 * @author Rashidi Zin
 */
class TestModulithExternalizationApplication {

    public static void main(String[] args) {
        SpringApplication.from(ModulithExternalizationApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}