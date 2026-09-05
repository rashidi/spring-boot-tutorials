package zin.rashidi.boot.ai.pgvector.document;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Rashidi Zin
 */
@Service
class DocumentSearchService {

    private final VectorStore store;

    DocumentSearchService(VectorStore store) {
        this.store = store;
    }

    void add(List<DocumentItem> items) {
        var documents = items.stream()
                .map(item -> new Document(item.id().toString(), item.content(), item.metadata()))
                .toList();

        store.add(documents);
    }

    List<Document> search(String query, int topK, double similarityThreshold) {
        return store.similaritySearch(
                SearchRequest.builder()
                        .query(query)
                        .topK(topK)
                        .similarityThreshold(similarityThreshold)
                        .build()
        );
    }

}
