package zin.rashidi.boot.modulith.subscription;

import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;
import zin.rashidi.boot.modulith.course.CourseEnded;
import zin.rashidi.boot.modulith.student.StudentInactivated;

/**
 * @author Rashidi Zin
 */
@Component
class SubscriptionManagement {

    private final SubscriptionRepository subscriptions;

    SubscriptionManagement(SubscriptionRepository subscriptions) {
        this.subscriptions = subscriptions;
    }

    @ApplicationModuleListener
    void cancelByCourse(CourseEnded course) {
        subscriptions.cancelByCourseId(course.id());
    }

    @ApplicationModuleListener
    void cancelByStudent(StudentInactivated student) {
        subscriptions.cancelByStudentId(student.id());
    }

}
