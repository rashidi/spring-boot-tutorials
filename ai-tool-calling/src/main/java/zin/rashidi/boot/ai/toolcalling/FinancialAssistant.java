package zin.rashidi.boot.ai.toolcalling;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

/**
 * @author Rashidi Zin
 */
@Service
public class FinancialAssistant {

    private final ChatClient chatClient;

    public FinancialAssistant(ChatClient.Builder builder, CurrencyConversionTools tools) {
        this.chatClient = builder
                .defaultTools(tools)
                .build();
    }

    public String ask(String query) {
        return chatClient.prompt()
                .user(query)
                .call()
                .content();
    }

}