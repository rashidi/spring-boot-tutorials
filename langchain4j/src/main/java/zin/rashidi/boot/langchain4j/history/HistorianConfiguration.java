package zin.rashidi.boot.langchain4j.history;

import static dev.langchain4j.memory.chat.MessageWindowChatMemory.withMaxMessages;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.elasticsearch.ElasticsearchEmbeddingStore;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Rashidi Zin
 */
@Configuration
class HistorianConfiguration {

    @Bean
    Historian historian(ChatLanguageModel model, ContentRetriever retriever, HistorianTool tool) {
        return AiServices.builder(Historian.class)
                .chatLanguageModel(model)
                .chatMemory(withMaxMessages(10))
                .contentRetriever(retriever)
                .tools(tool)
                .build();
    }

    @Bean
    ContentRetriever retriever(EmbeddingStore<TextSegment> embeddingStore) {
        return EmbeddingStoreContentRetriever.from(embeddingStore);
    }

    @Bean
    EmbeddingStore<TextSegment> embeddingStore(RestClient restClient) {
        return ElasticsearchEmbeddingStore.builder()
                .restClient(restClient)
                .indexName("history")
                .build();
    }

}
