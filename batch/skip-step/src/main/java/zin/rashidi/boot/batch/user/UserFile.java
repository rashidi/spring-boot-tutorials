package zin.rashidi.boot.batch.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Rashidi Zin
 */
@JsonIgnoreProperties(ignoreUnknown = true)
record UserFile(Long id, String name, String username) {
}
