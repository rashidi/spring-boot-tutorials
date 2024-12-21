package zin.rashidi.boot.test.slices.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
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
    void findByUsername() throws Exception {
        var fakeUser = new UserWithoutId("Rashidi Zin", "rashidi.zin", ACTIVE);

        doReturn(Optional.of(fakeUser)).when(repository).findByUsername("rashidi.zin");

        mvc.perform(get("/users/{username}", "rashidi.zin").contentType(APPLICATION_JSON).accept(APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(repository).findByUsername("rashidi.zin");
    }

}