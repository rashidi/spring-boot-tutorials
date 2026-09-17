package zin.rashidi.boot.ai.redis.document;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentSearchService {

    private final VectorStore vectorStore;

    public DocumentSearchService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void add(List<DocumentItem> items) {
        var documents = items.stream()
            .map(item -> new Document(item.id().toString(), item.text(), item.metadata()))
            .toList();
        vectorStore.add(documents);
    }

    public List<Document> search(String query, int topK, double similarityThreshold) {
        return vectorStore.similaritySearch(
            SearchRequest.builder()
                .query(query)
                .topK(topK)
                .similarityThreshold(similarityThreshold)
                .build()
        );
    }
}
