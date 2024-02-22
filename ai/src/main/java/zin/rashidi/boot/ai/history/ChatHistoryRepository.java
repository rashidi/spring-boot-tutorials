package zin.rashidi.boot.ai.history;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.parser.BeanOutputParser;
import org.springframework.stereotype.Repository;

import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * @author Rashidi Zin
 */
@Repository
class ChatHistoryRepository implements HistoryRepository {

    private final ChatClient client;
    private final ObjectMapper mapper;

    ChatHistoryRepository(ChatClient client, ObjectMapper mapper) {
        this.client = client;
        this.mapper = mapper;
    }

    @Override
    public History findByCountryAndYear(String country, Year year) {
        var parser = new BeanOutputParser<>(History.class, mapper);
        var messages = List.of(
                systemMessage(country),
                new UserMessage(year.format(DateTimeFormatter.ofPattern("yyyy")))
        );

        var prompt = new Prompt(messages);
        return parser.parse(client.call(prompt).getResult().getOutput().getContent());
    }

    private Message systemMessage(String country) {
        SystemPromptTemplate template = new SystemPromptTemplate(
                """
                You are a historian who is an expert for {country}.
                You will provide a historical event that occurred for the provided year.
                Your knowledge is limited up to the year 2021. You will return an empty response if requested year is after than 2021.
                Your response should be in JSON format which contains date, event, details, and person.
                """
        );

        return template.createMessage(Map.of("country", country));
    }

}
