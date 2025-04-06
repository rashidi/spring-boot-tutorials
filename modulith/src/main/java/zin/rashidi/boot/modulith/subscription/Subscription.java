package zin.rashidi.boot.modulith.subscription;

import org.springframework.data.annotation.Id;

import static zin.rashidi.boot.modulith.subscription.Subscription.Status.ACTIVE;

/**
 * @author Rashidi Zin
 */
class Subscription {

    @Id
    private Long id;
    private final Long studentId;
    private final Long courseId;
    private Status status;

    public Subscription(Long studentId, Long courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.status = ACTIVE;
    }

    enum Status {
        ACTIVE, COMPLETED, DORMANT, CANCELLED
    }

}
