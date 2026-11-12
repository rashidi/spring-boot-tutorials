package zin.rashidi.boot.web.restclient.post;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Rashidi Zin
 */
@HttpExchange(url = "/posts", accept = APPLICATION_JSON_VALUE)
interface PostRepository {

    @GetExchange
    List<Post> findAll();

    @GetExchange("/{id}")
    Post findById(@PathVariable Long id);

}
