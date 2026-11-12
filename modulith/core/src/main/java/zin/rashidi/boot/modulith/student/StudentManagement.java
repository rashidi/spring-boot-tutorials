package zin.rashidi.boot.modulith.student;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static zin.rashidi.boot.modulith.student.Student.Status.INACTIVE;

/**
 * @author Rashidi Zin
 */
@Service
class StudentManagement {

    private final StudentRepository students;

    StudentManagement(StudentRepository students) {
        this.students = students;
    }

    @Transactional
    public Student inactive(Student student) {
        return students.save(student.status(INACTIVE));
    }

}
