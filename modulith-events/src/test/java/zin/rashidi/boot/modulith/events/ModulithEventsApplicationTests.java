package zin.rashidi.boot.modulith.events;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

/**
 * @author Rashidi Zin
 */
class ModulithEventsApplicationTests {

    private final ApplicationModules modules = ApplicationModules.of(ModulithEventsApplication.class);

    @Test
    @DisplayName("Verify modular architecture boundaries and rules")
    void verifyModularity() {
        modules.verify();
    }

    @Test
    @DisplayName("Generate module documentation")
    void renderDocumentation() {
        new Documenter(modules).writeDocumentation();
    }

}