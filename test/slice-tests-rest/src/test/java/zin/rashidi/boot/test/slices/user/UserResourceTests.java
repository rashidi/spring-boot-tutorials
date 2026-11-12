package zin.rashidi.boot.test.slices.user;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static java.util.Optional.empty;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.assertj.MockMvcTester.from;
import static zin.rashidi.boot.test.slices.user.User.Status.ACTIVE;

/**
 * @author Rashidi Zin
 */
@WebMvcTest(controllers = UserResource.class, includeFilters = @Filter(EnableWebSecurity.class))
class UserResourceTests {

    private static MockMvcTester mvc;

    @MockitoBean
    private UserRepository repository;

    @BeforeAll
    static void setup(@Autowired WebApplicationContext context) {
        mvc = from(context, builder -> builder.apply(springSecurity()).build());
    }

    @Test
    @WithMockUser
    @DisplayName("Given username rashidi.zin exists When when I request for the username Then the response status should be OK")
    void findByUsername() {
        var fakeUser = Optional.of(new UserWithoutId("Rashidi Zin", "rashidi.zin", ACTIVE));

        doReturn(fakeUser).when(repository).findByUsername("rashidi.zin");

        mvc
                .get().uri("/users/{username}", "rashidi.zin")
                .assertThat()
                .hasStatus(OK);

        verify(repository).findByUsername("rashidi.zin");
    }

    @Test
    @WithMockUser
    @DisplayName("Given username rashidi.zin does not exist When when I request for the username Then the response status should be NOT_FOUND")
    void findByNonExistingUsername() {
        doReturn(empty()).when(repository).findByUsername("rashidi.zin");

        mvc
                .get().uri("/users/{username}", "rashidi.zin")
                .assertThat()
                .hasStatus(NOT_FOUND);

        verify(repository).findByUsername("rashidi.zin");
    }

    @Test
    @DisplayName("Given there is no authentication When I request for the username Then the response status should be UNAUTHORIZED")
    void findByUsernameWithoutAuthentication() {
        mvc
                .get().uri("/users/{username}", "rashidi.zin")
                .assertThat().hasStatus(UNAUTHORIZED);

        verify(repository, never()).findByUsername("rashidi.zin");
    }

}