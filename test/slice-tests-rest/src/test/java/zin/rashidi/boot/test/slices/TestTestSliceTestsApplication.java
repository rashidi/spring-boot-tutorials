package zin.rashidi.boot.test.slices;

import org.springframework.boot.SpringApplication;

public class TestTestSliceTestsApplication {

    public static void main(String[] args) {
        SpringApplication.from(TestSliceTestsApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
