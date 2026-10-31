package zin.rashidi.boot.security.method;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
@Testcontainers(disabledWithoutDocker = true)
class DocumentServiceTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private DocumentService documentService;

    @Autowired
    private DocumentRepository repository;

    private Document existingDocument;

    @BeforeEach
    void setUp() {
        Document document = new Document();
        document.setTitle("Test Title");
        document.setContent("Test Content");
        document.setAuthor("Test Author");
        existingDocument = repository.save(document);
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Given unauthenticated user When findAll is called Then AuthenticationCredentialsNotFoundException is thrown")
    void findAllUnauthenticated() {
        assertThatExceptionOfType(AuthenticationCredentialsNotFoundException.class)
                .isThrownBy(() -> documentService.findAll());
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Given user with USER role When findAll is called Then list of documents is returned")
    void findAllAsUser() {
        List<Document> documents = documentService.findAll();

        assertThat(documents).hasSize(1);
        assertThat(documents.get(0).getTitle()).isEqualTo("Test Title");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Given user with ADMIN role When findAll is called Then list of documents is returned")
    void findAllAsAdmin() {
        List<Document> documents = documentService.findAll();

        assertThat(documents).hasSize(1);
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Given user with USER role When create is called Then AccessDeniedException is thrown")
    void createAsUser() {
        assertThatExceptionOfType(AccessDeniedException.class)
                .isThrownBy(() -> documentService.create("New", "Content", "Author"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Given user with ADMIN role When create is called Then document is successfully created")
    void createAsAdmin() {
        Document created = documentService.create("New Title", "New Content", "New Author");

        assertThat(created.getId()).isNotNull();
        assertThat(created.getTitle()).isEqualTo("New Title");
        assertThat(repository.findById(created.getId())).isPresent();
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("Given user with USER role When delete is called Then AccessDeniedException is thrown")
    void deleteAsUser() {
        assertThatExceptionOfType(AccessDeniedException.class)
                .isThrownBy(() -> documentService.delete(existingDocument.getId()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Given user with ADMIN role When delete is called Then document is successfully deleted")
    void deleteAsAdmin() {
        UUID id = existingDocument.getId();
        documentService.delete(id);

        assertThat(repository.findById(id)).isEmpty();
    }
}
