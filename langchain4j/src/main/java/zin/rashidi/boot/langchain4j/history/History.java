package zin.rashidi.boot.langchain4j.history;

/**
 * @author Rashidi Zin
 */
class History {

    private final String country;
    private final int year;
    private String person;
    private String event;
    private String error;

    public History(String country, int year) {
        this.country = country;
        this.year = year;
    }

    public String country() {
        return country;
    }

    public int year() {
        return year;
    }

    public String person() {
        return person;
    }

    public History person(String person) {
        this.person = person;
        return this;
    }

    public String event() {
        return event;
    }

    public History event(String event) {
        this.event = event;
        return this;
    }

    public String error() {
        return error;
    }

    public History error(String error) {
        this.error = error;
        return this;
    }

}
