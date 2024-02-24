package zin.rashidi.boot.ai.history;

import java.time.LocalDate;

/**
 * @author Rashidi Zin
 */
record History(LocalDate date, String event, String details, String person) {
}
