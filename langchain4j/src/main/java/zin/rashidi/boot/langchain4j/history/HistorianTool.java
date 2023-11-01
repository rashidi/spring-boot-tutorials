package zin.rashidi.boot.langchain4j.history;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * @author Rashidi Zin
 */
@Component
class HistorianTool {

    @Tool("Validate year is supported")
    public void assertYear(int year) {
        Assert.isTrue(year < 2021, "Year must be less than 2021");
    }

}
