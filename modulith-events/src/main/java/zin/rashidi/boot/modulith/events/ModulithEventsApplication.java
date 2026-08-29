package zin.rashidi.boot.modulith.events;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.modulith.Modulith;

/**
 * @author Rashidi Zin
 */
@Modulith(
        sharedModules = "common",
        useFullyQualifiedModuleNames = false
)
@SpringBootApplication
public class ModulithEventsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModulithEventsApplication.class, args);
    }

}