package zin.rashidi.boot.observability.opentelemetry;

import org.springframework.boot.SpringApplication;

public class TestObservabilityOpenTelemetryApplication {

    public static void main(String[] args) {
        SpringApplication.from(ObservabilityOpenTelemetryApplication::main).with(TestcontainersConfiguration.class).run(args);
    }
}
