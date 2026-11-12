package zin.rashidi.boot.modulith.student;

import org.jmolecules.event.annotation.DomainEvent;

/**
 * @author Rashidi Zin
 */
@DomainEvent
public record StudentInactivated(Long id) {}