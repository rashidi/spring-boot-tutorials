package zin.rashidi.boot.batch.partitioning;

import org.springframework.boot.SpringApplication;

/**
 * @author Rashidi Zin
 */
class TestBatchPartitionedStepApplication {

    public static void main(String[] args) {
        SpringApplication.from(BatchPartitionedStepApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}