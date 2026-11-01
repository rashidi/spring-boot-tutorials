package zin.rashidi.boot.ai.chatmemory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rashidi Zin
 */
@SpringBootTest
@Import(TestcontainersConfiguration.class)
class CustomerSupportAssistantIntegrationTests {

    @Autowired
    private CustomerSupportAssistant assistant;

    @Autowired
    private ChatMemory chatMemory;

    @Test
    @DisplayName("When chatting across multiple turns Then conversation history is persisted and isolated")
    void maintainsConversationHistory() {
        var conversationId = UUID.randomUUID().toString();

        assistant.chat(conversationId, "My order number is ORD-9921");
        assistant.chat(conversationId, "Can you confirm my order status?");

        var history = chatMemory.get(conversationId, 10);
        assertThat(history).hasSize(4);

        var isolatedConversationId = UUID.randomUUID().toString();
        var isolatedHistory = chatMemory.get(isolatedConversationId, 10);
        assertThat(isolatedHistory).isEmpty();
    }

    @TestConfiguration
    static class TestChatModelConfiguration {

        @Bean
        @Primary
        ChatModel testChatModel() {
            return (Prompt prompt) -> {
                var latestMessage = prompt.getInstructions().getLast().getText();
                return new ChatResponse(List.of(
                        new Generation(new AssistantMessage("Response to: " + latestMessage))
                ));
            };
        }

    }

}