package zin.rashidi.boot.modulith.events;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.modulith.Modulithic;

/**
 * @author Rashidi Zin
 */
@Modulithic(
        sharedModules = "common",
        useFullyQualifiedModuleNames = false
)
@SpringBootApplication
public class ModulithEventsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModulithEventsApplication.class, args);
    }

}