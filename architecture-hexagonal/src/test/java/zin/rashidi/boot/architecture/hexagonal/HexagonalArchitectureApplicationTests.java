package zin.rashidi.boot.architecture.hexagonal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

class HexagonalArchitectureApplicationTests {

    ApplicationModules modules = ApplicationModules.of(HexagonalArchitectureApplication.class);

    @Test
    @DisplayName("Given the module structure When verifying boundaries Then Spring Modulith should find no violations")
    void verifyModularBoundaries() {
        modules.verify();
    }

}
