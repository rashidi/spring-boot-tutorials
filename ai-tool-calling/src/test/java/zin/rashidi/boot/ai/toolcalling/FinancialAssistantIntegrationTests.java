package zin.rashidi.boot.ai.toolcalling;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rashidi Zin
 */
@Testcontainers
@SpringBootTest
class FinancialAssistantIntegrationTests {

    @Container
    static GenericContainer<?> wireMock = new GenericContainer<>(DockerImageName.parse("wiremock/wiremock:3.9.1"))
            .withExposedPorts(8080)
            .withClasspathResourceMapping("mappings", "/home/wiremock/mappings", BindMode.READ_ONLY);

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("currency.api.url", () -> "http://" + wireMock.getHost() + ":" + wireMock.getMappedPort(8080));
        registry.add("spring.ai.openai.api-key", () -> "test-api-key");
    }

    @Autowired
    private FinancialAssistant assistant;

    @Autowired
    private CurrencyConversionTools tools;

    @Test
    @DisplayName("When prompt requires currency conversion Then tool invokes external API via WireMock")
    void convertsCurrencyViaTool() {
        var result = tools.convertCurrency("USD", "MYR", BigDecimal.valueOf(100));

        assertThat(result).isEqualByComparingTo(BigDecimal.valueOf(472.00));
    }

    @Test
    @DisplayName("When assistant is asked a question Then response is generated")
    void assistantGeneratesResponse() {
        var response = assistant.ask("Convert 100 USD to MYR");

        assertThat(response).contains("Converted 100 USD to 472.00 MYR");
    }

    @TestConfiguration
    static class TestChatModelConfiguration {

        @Bean
        @Primary
        ChatModel testChatModel(CurrencyConversionTools tools) {
            return (Prompt prompt) -> {
                var converted = tools.convertCurrency("USD", "MYR", BigDecimal.valueOf(100));
                return new ChatResponse(List.of(
                        new Generation(new AssistantMessage("Converted 100 USD to " + converted + " MYR"))
                ));
            };
        }

    }

}