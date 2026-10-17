package zin.rashidi.boot.architecture.hexagonal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class HexagonalArchitectureApplicationTests {

    ApplicationModules modules = ApplicationModules.of(HexagonalArchitectureApplication.class);

    @Test
    @DisplayName("Should verify modular boundaries using Spring Modulith")
    void verifyModularBoundaries() {
        modules.verify();
    }

}
