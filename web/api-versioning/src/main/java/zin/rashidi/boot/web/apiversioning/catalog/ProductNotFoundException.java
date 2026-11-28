package zin.rashidi.boot.web.apiversioning.catalog;

class ProductNotFoundException extends RuntimeException {

    ProductNotFoundException(String sku) {
        super("Product with SKU " + sku + " not found");
    }

}
