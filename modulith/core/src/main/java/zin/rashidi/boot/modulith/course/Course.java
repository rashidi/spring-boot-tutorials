package zin.rashidi.boot.modulith.course;

import org.springframework.data.annotation.Id;

import static zin.rashidi.boot.modulith.course.Course.Status.ACTIVE;

/**
 * @author Rashidi Zin
 */
class Course {

    @Id
    private Long id;
    private final String name;
    private Status status;

    Course(String name) {
        this.name = name;
        this.status = ACTIVE;
    }

    public Long id() {
        return id;
    }

    public Course status(Status status) {
        this.status = status;
        return this;
    }

    public Status status() {
        return status;
    }

    enum Status {
        ACTIVE, DORMANT, ENDED
    }

}
