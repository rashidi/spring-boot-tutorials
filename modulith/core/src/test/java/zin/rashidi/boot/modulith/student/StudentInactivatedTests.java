package zin.rashidi.boot.modulith.student;

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

/**
 * @author Rashidi Zin
 */
@Import(TestcontainersConfiguration.class)
@SpringBootTest
class StudentInactivatedTests {

    @Autowired
    private StudentManagement students;

    @Autowired
    private JdbcTemplate jdbc;

    @Test
    @DisplayName("Given there are subscriptions with student Id 5 When the student is inactivated Then all subscriptions for the student will be CANCELLED")
    void inactivated() {
        var student = new Student("Charlie Brown");
        ReflectionTestUtils.setField(student, "id", 5L);

        students.inactive(student);

        await().untilAsserted(() ->
                assertThat(inactiveSubscriptionsByStudentId(5L)).isEqualTo(2)
        );
    }

    private int inactiveSubscriptionsByStudentId(Long id) {
        return countRowsInTableWhere(jdbc, "subscription", "student_id = %d AND status = 'CANCELLED'".formatted(id));
    }
}
