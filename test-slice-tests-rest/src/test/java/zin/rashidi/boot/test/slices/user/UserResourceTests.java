package zin.rashidi.boot.test.slices.user;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static java.util.Optional.empty;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static zin.rashidi.boot.test.slices.user.User.Status.ACTIVE;

/**
 * @author Rashidi Zin
 */
@WebMvcTest(controllers = UserResource.class, includeFilters = @Filter(EnableWebSecurity.class))
class UserResourceTests {

    private static MockMvc mvc;

    @MockitoBean
    private UserRepository repository;

    @BeforeAll
    static void setup(@Autowired WebApplicationContext context) {
        mvc = webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    @WithMockUser
    @DisplayName("Given username rashidi.zin exists When when I request for the username Then the response status should be OK")
    void findByUsername() throws Exception {
        var fakeUser = Optional.of(new UserWithoutId("Rashidi Zin", "rashidi.zin", ACTIVE));

        doReturn(fakeUser).when(repository).findByUsername("rashidi.zin");

        mvc.perform(
                get("/users/{username}", "rashidi.zin")
        )
                .andExpect(status().isOk());

        verify(repository).findByUsername("rashidi.zin");
    }

    @Test
    @WithMockUser
    @DisplayName("Given username rashidi.zin does not exist When when I request for the username Then the response status should be NOT_FOUND")
    void findByNonExistingUsername() throws Exception {
        doReturn(empty()).when(repository).findByUsername("rashidi.zin");

        mvc.perform(
                get("/users/{username}", "rashidi.zin")
        )
                .andExpect(status().isNotFound());

        verify(repository).findByUsername("rashidi.zin");
    }

    @Test
    @DisplayName("Given there is no authentication When I request for the username Then the response status should be UNAUTHORIZED")
    void findByUsernameWithoutAuthentication() throws Exception {
        mvc.perform(
                get("/users/{username}", "rashidi.zin")
        )
                .andExpect(status().isUnauthorized());

        verify(repository, never()).findByUsername("rashidi.zin");
    }

}