package zin.rashidi.boot.langchain4j.translate;

import java.util.List;

/**
 * @author Rashidi Zin
 */
record Translate(Language language, Text text) {

    record Language(String source, String target) {}

    record Text(String source, String target, List<Breakdown> breakdowns) {}

    record Breakdown(String source, String target) {}

}
