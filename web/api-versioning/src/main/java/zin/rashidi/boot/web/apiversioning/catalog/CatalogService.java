package zin.rashidi.boot.web.apiversioning.catalog;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zin.rashidi.boot.web.apiversioning.inventory.InventoryCatalog;
import zin.rashidi.boot.web.apiversioning.inventory.StockSnapshot;

import java.util.List;

@Service
class CatalogService {

    private final ProductRepository repository;
    private final InventoryCatalog inventoryCatalog;
    private final ApplicationEventPublisher eventPublisher;

    CatalogService(
            ProductRepository repository,
            InventoryCatalog inventoryCatalog,
            ApplicationEventPublisher eventPublisher
    ) {
        this.repository = repository;
        this.inventoryCatalog = inventoryCatalog;
        this.eventPublisher = eventPublisher;
    }

    List<ProductV1Response> findAllV1() {
        return repository.findAllByOrderBySkuAsc().stream()
                .map(this::toV1Response)
                .toList();
    }

    List<ProductV2Response> findAllV2() {
        var products = repository.findAllByOrderBySkuAsc();
        var skus = products.stream().map(Product::sku).toList();
        var stockBySku = inventoryCatalog.findBySkus(skus);

        return products.stream()
                .map(product -> toV2Response(
                        product,
                        stockBySku.getOrDefault(product.sku(), StockSnapshot.unavailable(product.sku()))
                ))
                .toList();
    }

    ProductV1Response findBySkuV1(String sku) {
        return toV1Response(findExistingProduct(sku));
    }

    ProductV2Response findBySkuV2(String sku) {
        var product = findExistingProduct(sku);
        var stock = inventoryCatalog.findBySku(sku);
        return toV2Response(product, stock);
    }

    ProductV21Response findBySkuV21(String sku) {
        var product = findExistingProduct(sku);
        var stock = inventoryCatalog.findBySku(sku);
        return new ProductV21Response(
                product.sku(),
                product.name(),
                product.category(),
                new Money(product.price(), product.currency()),
                stock.quantity(),
                stock.availability(),
                stock.restockRecommended()
        );
    }

    @Transactional
    ProductV2Response create(CreateProductRequest request) {
        if (repository.existsBySku(request.sku())) {
            throw new DuplicateSkuException(request.sku());
        }

        var saved = repository.save(new Product(
                null,
                request.sku(),
                request.name(),
                request.category(),
                request.price(),
                request.currency()
        ));

        var stock = inventoryCatalog.registerInitialStock(
                saved.sku(),
                request.initialQuantity(),
                request.reorderThreshold()
        );

        eventPublisher.publishEvent(new ProductCreatedEvent(saved.sku(), saved.name(), saved.category()));

        return toV2Response(saved, stock);
    }

    private Product findExistingProduct(String sku) {
        return repository.findBySku(sku)
                .orElseThrow(() -> new ProductNotFoundException(sku));
    }

    private ProductV1Response toV1Response(Product product) {
        return new ProductV1Response(
                product.sku(),
                product.name(),
                product.price(),
                product.currency()
        );
    }

    private ProductV2Response toV2Response(Product product, StockSnapshot stock) {
        return new ProductV2Response(
                product.sku(),
                product.name(),
                new Money(product.price(), product.currency()),
                stock.quantity(),
                stock.availability()
        );
    }

}
