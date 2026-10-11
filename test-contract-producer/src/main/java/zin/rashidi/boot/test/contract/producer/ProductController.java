package zin.rashidi.boot.test.contract.producer;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ProductController {

    @GetMapping("/products/{id}")
    public Product getProduct(@PathVariable Long id) {
        if (id == 1L) {
            return new Product(1L, "MacBook Pro", 1999.99);
        }
        throw new IllegalArgumentException("Product not found");
    }
}
