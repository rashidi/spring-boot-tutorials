package zin.rashidi.boot.web.restclient.user;

import java.net.URI;

/**
 * @author Rashidi Zin
 */
record User(Long id, String name, String username, String email, URI website) {
}
