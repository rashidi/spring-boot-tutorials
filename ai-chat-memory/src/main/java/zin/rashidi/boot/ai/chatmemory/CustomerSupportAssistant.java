package zin.rashidi.boot.ai.chatmemory;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.stereotype.Service;

/**
 * @author Rashidi Zin
 */
@Service
public class CustomerSupportAssistant {

    private final ChatClient chatClient;

    public CustomerSupportAssistant(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String chat(String conversationId, String message) {
        return chatClient.prompt()
                .user(message)
                .advisors(advisor -> advisor.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, conversationId))
                .call()
                .content();
    }

}