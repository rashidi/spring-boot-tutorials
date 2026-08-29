package zin.rashidi.boot.modulith.events;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

import static org.springframework.modulith.docs.Documenter.Options.defaults;

/**
 * @author Rashidi Zin
 */
class ModulithEventsApplicationTests {

    private final ApplicationModules modules = ApplicationModules.of(ModulithEventsApplication.class);

    @Test
    @DisplayName("Verify architecture")
    void verify() {
        modules.verify();
    }

    @Test
    @DisplayName("Generate documentation")
    void document() {
        new Documenter(modules, defaults().withOutputFolder("docs"))
                .writeModulesAsPlantUml()
                .writeDocumentation(Documenter.DiagramOptions.defaults(), Documenter.CanvasOptions.defaults().revealInternals());
    }

}