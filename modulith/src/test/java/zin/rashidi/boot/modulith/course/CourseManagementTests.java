package zin.rashidi.boot.modulith.course;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;
import org.springframework.test.util.ReflectionTestUtils;
import zin.rashidi.boot.modulith.TestcontainersConfiguration;

import static java.time.Duration.ofMillis;
import static org.assertj.core.api.Assertions.assertThat;
import static zin.rashidi.boot.modulith.course.Course.Status.ENDED;

/**
 * @author Rashidi Zin
 */
@Import(TestcontainersConfiguration.class)
@ApplicationModuleTest
class CourseManagementTests {

    @Autowired
    private CourseManagement courses;

    @Test
    @DisplayName("When a course is ENDED Then CourseEnded event will be triggered with the course Id")
    void courseEnded(Scenario scenario) {
        var course = new Course("Advanced Java Programming").status(ENDED);
        ReflectionTestUtils.setField(course, "id", 2L);

        scenario.stimulate(() -> courses.updateCourse(course))
                .andWaitAtMost(ofMillis(101))
                .andWaitForEventOfType(CourseEnded.class)
                .toArriveAndVerify(event -> assertThat(event).extracting("id").isEqualTo(2L));
    }

}