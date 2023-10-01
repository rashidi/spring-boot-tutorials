package zin.rashidi.boot.batch.user;

import org.springframework.data.annotation.Id;

/**
 * @author Rashidi Zin
 */
record User(@Id Long id, String name, String username) {
}
