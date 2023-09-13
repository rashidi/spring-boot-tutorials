package zin.rashidi.boot.batch.user;

import java.util.List;

import org.springframework.web.service.annotation.GetExchange;

/**
 * @author Rashidi Zin
 */
interface UserRepository {

    @GetExchange("/users")
    List<User> findAll();

}
