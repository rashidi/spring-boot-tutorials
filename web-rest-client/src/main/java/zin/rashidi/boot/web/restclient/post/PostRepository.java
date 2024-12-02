package zin.rashidi.boot.web.restclient.post;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

/**
 * @author Rashidi Zin
 */
@HttpExchange("/posts")
interface PostRepository {

    @GetExchange
    List<Post> findAll();

    @GetExchange("/{id}")
    Post findById(@PathVariable Long id);

}
