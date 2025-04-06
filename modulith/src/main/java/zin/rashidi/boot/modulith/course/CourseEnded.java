package zin.rashidi.boot.modulith.course;

import org.jmolecules.event.annotation.DomainEvent;

/**
 * @author Rashidi Zin
 */
@DomainEvent
public record CourseEnded(Long id) {}
