package zin.rashidi.boot.ai.neo4j.document;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DocumentSearchService {

    private final VectorStore vectorStore;

    public DocumentSearchService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void add(DocumentItem item) {
        var document = new Document(item.id(), item.content(), Map.of());
        this.vectorStore.add(List.of(document));
    }

    public List<DocumentItem> search(String query) {
        var searchRequest = SearchRequest.builder()
                .query(query)
                .topK(1)
                .similarityThreshold(0.0)
                .build();

        return this.vectorStore.similaritySearch(searchRequest).stream()
                .map(document -> new DocumentItem(document.getId(), document.getContent()))
                .toList();
    }
}
