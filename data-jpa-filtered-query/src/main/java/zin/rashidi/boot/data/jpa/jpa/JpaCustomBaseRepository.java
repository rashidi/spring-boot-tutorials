package zin.rashidi.boot.data.jpa.jpa;

import static zin.rashidi.boot.data.jpa.user.User.Status.ACTIVE;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.ReflectionUtils;

import jakarta.persistence.EntityManager;

/**
 * @author Rashidi Zin
 */
class JpaCustomBaseRepository<T, ID> extends SimpleJpaRepository<T, ID> {

    private final boolean hasStatusField;

    public JpaCustomBaseRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.hasStatusField = ReflectionUtils.findField(getDomainClass(), "status") != null;
    }

    @Override
    public List<T> findAll() {
        return hasStatusField ? findAll((root, _, _) -> root.get("status").in(ACTIVE)) : super.findAll();
    }

}
