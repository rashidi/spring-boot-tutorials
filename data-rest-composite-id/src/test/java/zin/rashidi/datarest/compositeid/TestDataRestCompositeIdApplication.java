package zin.rashidi.datarest.compositeid;

import org.springframework.boot.SpringApplication;

public class TestDataRestCompositeIdApplication {

    public static void main(String[] args) {
        SpringApplication.from(DataRestCompositeIdApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
