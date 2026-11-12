package zin.rashidi.boot.modulith.student;

import org.springframework.data.annotation.Id;

import static zin.rashidi.boot.modulith.student.Student.Status.ACTIVE;

/**
 * @author Rashidi Zin
 */
class Student {

    @Id
    private Long id;
    private final String name;
    private Status status;

    public Student(String name) {
        this.name = name;
        this.status = ACTIVE;
    }

    public Long id() {
        return id;
    }

    public Status status() {
        return status;
    }

    public Student status(Status status) {
        this.status = status;
        return this;
    }

    enum Status {
        ACTIVE, INACTIVE
    }

}
