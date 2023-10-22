package zin.rashidi.boot.langchain4j.translate;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.retriever.EmbeddingStoreRetriever;
import dev.langchain4j.retriever.Retriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.elasticsearch.ElasticsearchEmbeddingStore;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import static dev.langchain4j.memory.chat.MessageWindowChatMemory.withMaxMessages;

/**
 * @author Rashidi Zin
 */
@Configuration
class TranslationConfiguration {

    @Bean
    TranslationService translateService(ChatLanguageModel chatLanguageModel, Retriever<TextSegment> retriever) {
        return AiServices.builder(TranslationService.class)
                .chatLanguageModel(chatLanguageModel)
                .chatMemory(withMaxMessages(20))
                .retriever(retriever)
                .build();
    }

    @Bean
    Retriever<TextSegment> retriever(EmbeddingStore<TextSegment> embeddingStore) {
        return EmbeddingStoreRetriever.from(embeddingStore, new AllMiniLmL6V2EmbeddingModel(), 1, 0.6);
    }

    @Bean
    EmbeddingStore<TextSegment> embeddingStore(Environment environment) {
        return ElasticsearchEmbeddingStore.builder()
                .serverUrl(environment.getProperty("app.elasticsearch.uri"))
                .indexName("translation")
                .build();
    }

}
