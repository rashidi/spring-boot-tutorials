package zin.rashidi.boot.jooq.user;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static zin.rashidi.boot.jooq.Tables.USERS;

/**
 * @author Rashidi Zin
 */
@Repository
class UserJooqRepository implements UserRepository {

    private final DSLContext dsl;

    UserJooqRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return dsl.selectFrom(USERS).where(USERS.USERNAME.eq(username))
                .withReadOnly()
                .maxRows(1)
                .stream()
                .map(record -> new User(record.getId(), record.getName(), record.getUsername()))
                .findFirst();
    }

}
