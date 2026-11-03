package zin.rashidi.boot.data.rest.compositeid;

import org.springframework.boot.SpringApplication;

public class TestDataRestCompositeIdApplication {

    public static void main(String[] args) {
        SpringApplication.from(DataRestCompositeIdApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
