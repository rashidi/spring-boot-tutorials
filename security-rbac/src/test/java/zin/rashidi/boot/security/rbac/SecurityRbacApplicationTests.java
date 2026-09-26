package zin.rashidi.boot.security.rbac;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Import(TestcontainersConfiguration.class)
class SecurityRbacApplicationTests {

    @Test
    @DisplayName("Should load application context")
    void contextLoads() {
    }

}