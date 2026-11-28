package zin.rashidi.boot.web.apiversioning.catalog;

class DuplicateSkuException extends RuntimeException {

    DuplicateSkuException(String sku) {
        super("Product with SKU " + sku + " already exists");
    }

}
