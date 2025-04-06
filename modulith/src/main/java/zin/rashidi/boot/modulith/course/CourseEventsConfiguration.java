package zin.rashidi.boot.modulith.course;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.relational.core.mapping.event.AfterSaveEvent;

import static zin.rashidi.boot.modulith.course.Course.Status.ENDED;

/**
 * @author Rashidi Zin
 */
@Configuration
class CourseEventsConfiguration {

    @Bean
    public ApplicationListener<AfterSaveEvent<Course>> courseEnded(ApplicationEventPublisher publisher) {
        return event -> {
            if (ENDED == event.getEntity().status()) {
                publisher.publishEvent(new CourseEnded(event.getEntity().id()));
            }
        };
    }

}
