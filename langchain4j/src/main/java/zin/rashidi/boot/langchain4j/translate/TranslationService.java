package zin.rashidi.boot.langchain4j.translate;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

/**
 * @author Rashidi Zin
 */
interface TranslationService {

    @SystemMessage({
            "You are a translator for {{sourceLanguage}} to {{targetLanguage}}.",
            """
            Your response should be in JSON format which includes the following fields:
            - language.source - The source language of the text.
            - language.target - The target language of the text.
            - text.source - The source text.
            - text.target - The target text.
            - text.breakdowns - The breakdowns of the source and target text.
            """
    })
    @UserMessage("Translate this text: {{text}}")
    Translate translate(@V("sourceLanguage") String sourceLanguage, @V("targetLanguage") String targetLanguage, @V("text") String text);

}
