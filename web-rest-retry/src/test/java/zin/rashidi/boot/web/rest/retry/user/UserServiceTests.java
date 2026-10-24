package zin.rashidi.boot.web.rest.retry.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;

@SpringBootTest
class UserServiceTests {

    @Autowired
    private UserService userService;

    @Autowired
    private RestClient.Builder restClientBuilder;

    @Test
    @DisplayName("Given the API returns 500 twice When getting users Then it should retry and succeed on the third attempt")
    void testGetUsersWithRetry() throws Exception {
        MockRestServiceServer server = MockRestServiceServer.bindTo(this.restClientBuilder).build();

        // Ensure we are injecting the mocked RestClient into the target object, not the AOP proxy itself
        Object target = userService;
        if (AopUtils.isAopProxy(userService)) {
            target = ((Advised) userService).getTargetSource().getTarget();
        }
        ReflectionTestUtils.setField(target, "restClient", restClientBuilder.baseUrl("https://jsonplaceholder.typicode.com").build());

        String jsonResponse = "[{\"id\": 1, \"name\": \"Leanne Graham\", \"username\": \"Bret\", \"email\": \"Sincere@april.biz\"}]";

        server.expect(requestTo("https://jsonplaceholder.typicode.com/users"))
              .andRespond(withServerError());

        server.expect(requestTo("https://jsonplaceholder.typicode.com/users"))
              .andRespond(withServerError());

        server.expect(requestTo("https://jsonplaceholder.typicode.com/users"))
              .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        List<User> users = this.userService.getUsers();

        assertThat(users).hasSize(1);
        assertThat(users.get(0).name()).isEqualTo("Leanne Graham");

        server.verify();
    }
}
