package zin.rashidi.boot.langchain4j.translate;

import java.util.List;

/**
 * @author Rashidi Zin
 */
record Translate(Detail language, Text text) {

    record Text(String source, String target, List<Detail> breakdowns) {}

    record Detail(String source, String target) {}

}
