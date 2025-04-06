package zin.rashidi.boot.modulith.student;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;
import org.springframework.test.util.ReflectionTestUtils;
import zin.rashidi.boot.modulith.TestcontainersConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Rashidi Zin
 */
@Import(TestcontainersConfiguration.class)
@ApplicationModuleTest
class StudentManagementTests {

    @Autowired
    private StudentManagement students;

    @Test
    @DisplayName("When the student with id 4 is inactivated Then StudentInactivated event will be triggered with student id 4")
    void inactive(Scenario scenario) {
        var student = new Student("Bob Johnson");
        ReflectionTestUtils.setField(student, "id", 4L);

        scenario.stimulate(() -> students.inactive(student))
                .andWaitForEventOfType(StudentInactivated.class)
                .toArriveAndVerify(inActivatedStudent -> assertThat(inActivatedStudent).extracting("id").isEqualTo(4L));
    }

}
