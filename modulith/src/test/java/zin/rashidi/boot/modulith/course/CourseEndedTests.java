package zin.rashidi.boot.modulith.course;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import zin.rashidi.boot.modulith.TestcontainersConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTableWhere;
import static zin.rashidi.boot.modulith.course.Course.Status.ENDED;

/**
 * @author Rashidi Zin
 */
@Import(TestcontainersConfiguration.class)
@SpringBootTest
class CourseEndedTests {

    @Autowired
    private CourseManagement courses;

    @Autowired
    private JdbcTemplate jdbc;

    @Test
    @DisplayName("Given there are subscriptions with course Id 1 When the course ENDED Then all subscriptions for the course will be CANCELLED")
    void end() {
        var course = new Course("Introduction to Spring Boot").status(ENDED);
        ReflectionTestUtils.setField(course, "id", 1L);

        courses.updateCourse(course);

        await()
                .untilAsserted(() ->
                        assertThat(cancelledSubscriptionsByCourseId(1L)).isEqualTo(2)
                );
    }

    private int cancelledSubscriptionsByCourseId(Long courseId) {
        return countRowsInTableWhere(jdbc, "subscription", "course_id = %d AND status = 'CANCELLED'".formatted(courseId));
    }

}
