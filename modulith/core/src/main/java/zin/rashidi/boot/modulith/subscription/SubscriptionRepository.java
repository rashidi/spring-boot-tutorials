package zin.rashidi.boot.modulith.subscription;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Rashidi Zin
 */
interface SubscriptionRepository extends CrudRepository<Subscription, Long> {

    @Modifying
    @Query("UPDATE subscription SET status = 'CANCELLED' WHERE course_id = :courseId")
    int cancelByCourseId(Long courseId);

    @Modifying
    @Query("UPDATE subscription SET status = 'CANCELLED' WHERE student_id = :studentId")
    int cancelByStudentId(Long studentId);

}
