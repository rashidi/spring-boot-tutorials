package zin.rashidi.boot.security.method;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DocumentService {

    private final DocumentRepository repository;

    public DocumentService(DocumentRepository repository) {
        this.repository = repository;
    }

    @PreAuthorize("hasRole('USER')")
    public List<Document> findAll() {
        return repository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Document create(String title, String content, String author) {
        Document document = new Document();
        document.setTitle(title);
        document.setContent(content);
        document.setAuthor(author);

        return repository.save(document);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(UUID id) {
        repository.deleteById(id);
    }
}
