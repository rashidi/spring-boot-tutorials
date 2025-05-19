package zin.rashidi.web.xss.greet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Rashidi Zin
 */
@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
class GreetResourceTests {

    @Autowired
    private MockMvcTester mvc;

    @Test
    @DisplayName("Given XSS protection is enabled Then response header should contain information about X-XSS-Protection and Content-Security-Policy")
    void headers() {
        mvc.get()
                .uri("/greet?name={name}", "rashidi")
            .assertThat()
                .matches(status().isOk())
                .matches(header().string("Content-Security-Policy", "default-src 'self'"))
                .matches(header().string("X-XSS-Protection", "1; mode=block"));
    }

}