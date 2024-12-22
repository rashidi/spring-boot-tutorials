package zin.rashidi.boot.test.slices.user;

import zin.rashidi.boot.test.slices.user.User.Status;

/**
 * @author Rashidi Zin
 */
record UserWithoutId(String name, String username, Status status) {
}
