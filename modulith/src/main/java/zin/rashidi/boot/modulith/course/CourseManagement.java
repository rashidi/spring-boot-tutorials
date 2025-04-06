package zin.rashidi.boot.modulith.course;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Rashidi Zin
 */
@Service
@Transactional(readOnly = true)
class CourseManagement {

    private final CourseRepository courses;

    CourseManagement(CourseRepository courses) {
        this.courses = courses;
    }

    @Transactional
    void updateCourse(Course course) {
        courses.save(course);
    }

}
