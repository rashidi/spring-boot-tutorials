package zin.rashidi.boot.modulith.externalization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.modulith.Modulith;

/**
 * @author Rashidi Zin
 */
@Modulith
@SpringBootApplication
public class ModulithExternalizationApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModulithExternalizationApplication.class, args);
    }

}