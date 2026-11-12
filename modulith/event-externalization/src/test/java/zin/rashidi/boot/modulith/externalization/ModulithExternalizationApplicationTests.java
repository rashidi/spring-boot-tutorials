package zin.rashidi.boot.modulith.externalization;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

import static org.springframework.modulith.docs.Documenter.Options.defaults;

/**
 * @author Rashidi Zin
 */
@DisplayName("Given the modular monolith application, When architecture and documentation are verified, Then all module boundaries are respected")
class ModulithExternalizationApplicationTests {

    private final ApplicationModules modules = ApplicationModules.of(ModulithExternalizationApplication.class);

    @Test
    @DisplayName("Given the application modules, When verifying modular structure, Then no architectural violations are detected")
    void verifyModularity() {
        modules.verify();
    }

    @Test
    @DisplayName("Given the application modules, When generating documentation, Then PlantUML and module canvases are written")
    void renderDocumentation() {
        new Documenter(modules, defaults().withOutputFolder("docs"))
                .writeModulesAsPlantUml()
                .writeDocumentation(Documenter.DiagramOptions.defaults(), Documenter.CanvasOptions.defaults().revealInternals());
    }

}