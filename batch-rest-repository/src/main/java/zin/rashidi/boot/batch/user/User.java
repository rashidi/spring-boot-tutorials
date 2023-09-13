package zin.rashidi.boot.batch.user;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Rashidi Zin
 */
@Document
@JsonIgnoreProperties(ignoreUnknown = true)
record User(@MongoId Long id, String username) {
}
