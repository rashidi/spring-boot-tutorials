package zin.rashidi.boot.web.apiversioning.catalog;

import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Optional;

interface ProductRepository extends ListCrudRepository<Product, Long> {

    List<Product> findAllByOrderBySkuAsc();

    Optional<Product> findBySku(String sku);

    boolean existsBySku(String sku);

}
