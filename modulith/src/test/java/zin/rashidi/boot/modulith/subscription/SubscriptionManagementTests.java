package zin.rashidi.boot.modulith.subscription;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;
import zin.rashidi.boot.modulith.TestcontainersConfiguration;
import zin.rashidi.boot.modulith.course.CourseEnded;
import zin.rashidi.boot.modulith.student.StudentInactivated;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rashidi Zin
 */
@Import(TestcontainersConfiguration.class)
@ApplicationModuleTest
class SubscriptionManagementTests {

    @Autowired
    private SubscriptionRepository subscriptions;

    @Test
    @DisplayName("When CourseEnded is triggered with id 5 Then all subscriptions for the course will be CANCELLED")
    void courseEnded(Scenario scenario) {
        var event = new CourseEnded(5L);

        scenario.publish(event)
                .andWaitForStateChange(() -> subscriptions.cancelByCourseId(5L))
                .andVerify(updatedRows -> assertThat(updatedRows).isEqualTo(2));
    }

    @Test
    @DisplayName("When StudentInactivated is triggered with id 5 Then all subscriptions for the student will be CANCELLED")
    void studentInactivated(Scenario scenario) {
        var event = new StudentInactivated(5L);

        scenario.publish(event)
                .andWaitForStateChange(() -> subscriptions.cancelByStudentId(5L))
                .andVerify(updatedRows -> assertThat(updatedRows).isEqualTo(2));
    }

}