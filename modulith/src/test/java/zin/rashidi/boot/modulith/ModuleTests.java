package zin.rashidi.boot.modulith;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

import static org.springframework.modulith.docs.Documenter.Options.defaults;

/**
 * @author Rashidi Zin
 */
class ModuleTests {

    private final ApplicationModules modules = ApplicationModules.of(ModulithApplication.class);

    @Test
    @DisplayName("Given the application modules When verified Then architecture should be valid")
    void verify() {
        modules.verify();
    }

    @Test
    @DisplayName("Given the application modules When documented Then documentation should be generated")
    void document() {
        new Documenter(modules, defaults().withOutputFolder("docs"))
                .writeModulesAsPlantUml()
                .writeDocumentation(Documenter.DiagramOptions.defaults(), Documenter.CanvasOptions.defaults().revealInternals());
    }

}
