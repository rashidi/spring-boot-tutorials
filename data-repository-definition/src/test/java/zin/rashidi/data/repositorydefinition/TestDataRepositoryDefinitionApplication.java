package zin.rashidi.data.repositorydefinition;

import org.springframework.boot.SpringApplication;

public class TestDataRepositoryDefinitionApplication {

    public static void main(String[] args) {
        SpringApplication.from(DataRepositoryDefinitionApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
