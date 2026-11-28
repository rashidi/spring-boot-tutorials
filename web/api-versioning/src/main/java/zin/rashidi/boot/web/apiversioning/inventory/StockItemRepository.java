package zin.rashidi.boot.web.apiversioning.inventory;

import org.springframework.data.repository.ListCrudRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

interface StockItemRepository extends ListCrudRepository<StockItem, Long> {

    Optional<StockItem> findBySku(String sku);

    List<StockItem> findBySkuIn(Collection<String> skus);

}
