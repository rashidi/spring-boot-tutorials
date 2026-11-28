package zin.rashidi.boot.web.apiversioning.catalog;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/products")
class ProductResource {

    private final CatalogService catalogService;

    ProductResource(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping(version = "1.0")
    List<ProductV1Response> findAllV1() {
        return catalogService.findAllV1();
    }

    @GetMapping(version = "2.0+")
    List<ProductV2Response> findAllV2() {
        return catalogService.findAllV2();
    }

    @GetMapping(path = "/{sku}", version = "1.0")
    ProductV1Response findBySkuV1(@PathVariable String sku) {
        return catalogService.findBySkuV1(sku);
    }

    @GetMapping(path = "/{sku}", version = "2.0")
    ProductV2Response findBySkuV2(@PathVariable String sku) {
        return catalogService.findBySkuV2(sku);
    }

    @GetMapping(path = "/{sku}", version = "2.1+")
    ProductV21Response findBySkuV21(@PathVariable String sku) {
        return catalogService.findBySkuV21(sku);
    }

    @PostMapping(version = "2.0+")
    ResponseEntity<ProductV2Response> create(@Valid @RequestBody CreateProductRequest request) {
        var created = catalogService.create(request);
        var location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{sku}")
                .buildAndExpand(created.sku())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

}
