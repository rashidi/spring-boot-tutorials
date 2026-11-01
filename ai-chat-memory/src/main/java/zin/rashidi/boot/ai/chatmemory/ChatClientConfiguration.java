package zin.rashidi.boot.ai.chatmemory;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.jdbc.JdbcChatMemory;
import org.springframework.ai.chat.memory.jdbc.JdbcChatMemoryConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author Rashidi Zin
 */
@Configuration(proxyBeanMethods = false)
class ChatClientConfiguration {

    @Bean
    ChatMemory chatMemory(JdbcTemplate jdbcTemplate) {
        return JdbcChatMemory.create(JdbcChatMemoryConfig.builder()
                .withJdbcTemplate(jdbcTemplate)
                .build());
    }

    @Bean
    ChatClient chatClient(ChatClient.Builder builder, ChatMemory chatMemory) {
        return builder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

}