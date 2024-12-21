package zin.rashidi.boot.test.slices.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static java.util.Optional.empty;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static zin.rashidi.boot.test.slices.user.User.Status.ACTIVE;

/**
 * @author Rashidi Zin
 */
@WebMvcTest(UserResource.class)
class UserResourceTests {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private UserRepository repository;

    @Test
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
    @DisplayName("Given username rashidi.zin does not exist When when I request for the username Then the response status should be NOT_FOUND")
    void findByNonExistingUsername() throws Exception {
        doReturn(empty()).when(repository).findByUsername("rashidi.zin");

        mvc.perform(
                get("/users/{username}", "rashidi.zin")
        )
                .andExpect(status().isNotFound());

        verify(repository).findByUsername("rashidi.zin");
    }

}