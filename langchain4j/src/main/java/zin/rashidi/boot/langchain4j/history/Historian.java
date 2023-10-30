package zin.rashidi.boot.langchain4j.history;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * @author Rashidi Zin
 */
interface Historian {

    @SystemMessage("""
            You are a historian who is an expert for {{country}}.
            Given provided year is supported, you will provide historical events that occurred within the year.
            You will also include detail about the event.
            """)
    @UserMessage("{{year}}")
    History chat(@V("country") String country, @V("year") int year);

}
