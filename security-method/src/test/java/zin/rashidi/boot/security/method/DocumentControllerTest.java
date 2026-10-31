package zin.rashidi.boot.security.method;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.security.test.context.support.WithMockUser;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers(disabledWithoutDocker = true)
class DocumentControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private MockMvcTester mvc;

    @Autowired
    private DocumentRepository repository;

    private Document existingDocument;

    @BeforeEach
    void setUp() {
        Document document = new Document();
        document.setTitle("Existing Title");
        document.setContent("Existing Content");
        document.setAuthor("Existing Author");
        existingDocument = repository.save(document);
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Given unauthenticated request When GET /documents Then returns 401 Unauthorized")
    void getDocumentsUnauthenticated() {
        mvc.get().uri("/documents")
                .assertThat().hasStatus(401);
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Given USER role When GET /documents Then returns 200 OK with list of documents")
    void getDocumentsAsUser() {
        mvc.get().uri("/documents")
                .assertThat().hasStatusOk()
                .bodyJson().extractingPath("$[0].title").asString().isEqualTo("Existing Title");
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Given USER role When POST /documents Then returns 403 Forbidden")
    void createDocumentAsUser() {
        mvc.post().uri("/documents").with(csrf())
                .contentType("application/json")
                .content("""
                        {
                            "title": "New Title",
                            "content": "New Content",
                            "author": "New Author"
                        }
                        """)
                .assertThat().hasStatus(403);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Given ADMIN role When POST /documents Then returns 201 Created")
    void createDocumentAsAdmin() {
        mvc.post().uri("/documents").with(csrf())
                .contentType("application/json")
                .content("""
                        {
                            "title": "New Title",
                            "content": "New Content",
                            "author": "New Author"
                        }
                        """)
                .assertThat().hasStatus(201)
                .bodyJson().extractingPath("$.title").asString().isEqualTo("New Title");
    }
}
