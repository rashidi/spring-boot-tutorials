package zin.rashidi.boot.ai.history;

import java.time.Year;

/**
 * @author Rashidi Zin
 */
interface HistoryRepository {

    History findByCountryAndYear(String country, Year year);

}
