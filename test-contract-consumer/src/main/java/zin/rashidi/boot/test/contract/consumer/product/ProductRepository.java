package zin.rashidi.boot.test.contract.consumer.product;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

/**
 * @author Rashidi Zin
 */
@HttpExchange(url = "/products", accept = MediaType.APPLICATION_JSON_VALUE)
interface ProductRepository {

    @GetExchange("/{id}")
    Product findById(@PathVariable Long id);

}
