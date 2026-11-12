package zin.rashidi.boot.data.jpa.country;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * @author Rashidi Zin
 */
@Entity
class Country {

    @Id
    String isoCode;

    String name;

    protected Country() {
    }

    public Country(String isoCode, String name) {
        this.isoCode = isoCode;
        this.name = name;
    }

    public String isoCode() {
        return isoCode;
    }

    public String name() {
        return name;
    }

}
