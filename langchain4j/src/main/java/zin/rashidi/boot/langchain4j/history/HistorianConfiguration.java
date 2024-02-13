package zin.rashidi.boot.langchain4j.history;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.retriever.EmbeddingStoreRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.elasticsearch.ElasticsearchEmbeddingStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import static dev.langchain4j.memory.chat.MessageWindowChatMemory.withMaxMessages;

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
        return EmbeddingStoreRetriever.from(embeddingStore, new AllMiniLmL6V2EmbeddingModel(), 1, 0.6)
                .toContentRetriever();
    }

    @Bean
    EmbeddingStore<TextSegment> embeddingStore(Environment environment) {
        return ElasticsearchEmbeddingStore.builder()
                .serverUrl(environment.getProperty("app.elasticsearch.uri"))
                .indexName("history")
                .build();
    }

}
