package zin.rashidi.boot.ai.history;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.Year;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rashidi Zin
 */
@SpringBootTest
class HistoryRepositoryTests {

    @Autowired
    private HistoryRepository historian;

    @Test
    @DisplayName("When I request for event in Malaysia in 1957 Then information about Independence Day will be provided")
    void findByCountryAndYear() {
        var response = historian.findByCountryAndYear("Malaysia", Year.of(1957));

        assertThat(response)
                .extracting("date", "person")
                .containsExactly(LocalDate.of(1957, 8, 31), "Tunku Abdul Rahman");

        assertThat(response).extracting("event").asString().containsIgnoringCase("Independence");
    }

    @Test
    @DisplayName("Given available information is only up to 2021 When I request for information after 2021 Then all fields should be null")
    void findByCountryAndYearAfter2021() {
        var response = historian.findByCountryAndYear("Malaysia", Year.of(2022));

        assertThat(response)
                .extracting("date", "event", "person")
                .containsOnlyNulls();
    }

}