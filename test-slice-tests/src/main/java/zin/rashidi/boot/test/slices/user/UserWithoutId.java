package zin.rashidi.boot.test.slices.user;

import org.springframework.beans.factory.annotation.Value;
import zin.rashidi.boot.test.slices.user.User.Status;

/**
 * @author Rashidi Zin
 */
interface UserWithoutId {

    @Value("#{target.name.first} #{target.name.last}")
    String name();

    String username();

    Status status();

}
