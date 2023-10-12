package zin.rashidi.boot.data.jpa.user;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

/**
 * @author Rashidi Zin
 */
@Entity
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;

    private Status status;

    protected User() {
    }

    public User(String name, Status status) {
        this.name = name;
        this.status = status;
    }

    public UUID id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Status status() {
        return status;
    }

    public enum Status {

        ACTIVE,

        INACTIVE

    }

}
