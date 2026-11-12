package zin.rashidi.boot.modulith.student;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.relational.core.mapping.event.AfterSaveEvent;

import static zin.rashidi.boot.modulith.student.Student.Status.INACTIVE;

/**
 * @author Rashidi Zin
 */
@Configuration
class StudentEventsConfiguration {

    @Bean
    public ApplicationListener<AfterSaveEvent<Student>> studentInactivated(ApplicationEventPublisher publisher) {
        return event -> {
            if (INACTIVE == event.getEntity().status())
                publisher.publishEvent(new StudentInactivated(event.getEntity().id()));
        };
    }

}
