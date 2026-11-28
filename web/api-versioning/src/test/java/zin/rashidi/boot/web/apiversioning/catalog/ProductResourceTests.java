package zin.rashidi.boot.web.apiversioning.catalog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.client.ApiVersionInserter;
import zin.rashidi.boot.web.apiversioning.TestcontainersConfiguration;
import zin.rashidi.boot.web.apiversioning.audit.CatalogAuditLog;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@AutoConfigureRestTestClient
@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DisplayName("Given a versioned product catalog API backed by PostgreSQL")
class ProductResourceTests {

    private static final String API_VERSION_HEADER = "API-Version";

    @Autowired
    private RestTestClient restClient;

    @Autowired
    private JdbcClient jdbcClient;

    @Autowired
    private CatalogAuditLog auditLog;

    private RestTestClient versionedClient;

    @BeforeEach
    void setUp() {
        versionedClient = restClient.mutate()
                .apiVersionInserter(ApiVersionInserter.useHeader(API_VERSION_HEADER))
                .build();

        jdbcClient.sql("TRUNCATE TABLE catalog_audit_entries, inventory_items, products RESTART IDENTITY")
                .update();

        jdbcClient.sql("""
                INSERT INTO products (sku, name, category, price, currency)
                VALUES ('KB-001', 'Mechanical Keyboard', 'Peripherals', 149.99, 'USD'),
                       ('MS-002', 'Ergonomic Mouse', 'Peripherals', 79.50, 'USD'),
                       ('MN-003', '4K Studio Monitor', 'Displays', 599.00, 'USD'),
                       ('HD-004', 'USB-C Dock', 'Accessories', 129.00, 'USD')
                """)
                .update();

        jdbcClient.sql("""
                INSERT INTO inventory_items (sku, quantity, reorder_threshold)
                VALUES ('KB-001', 25, 5),
                       ('MS-002', 3, 5),
                       ('MN-003', 0, 2)
                """)
                .update();
    }

    @Nested
    @DisplayName("Given API version 1.0 (deprecated legacy contract)")
    class DeprecatedVersionOne {

        @Test
        @DisplayName("When GET /api/products is requested with API-Version 1.0, Then legacy flat payloads and RFC 8594 deprecation headers are returned")
        void returnsLegacyCatalogWithDeprecationHeaders() {
            versionedClient.get()
                    .uri("/api/products")
                    .apiVersion("1.0")
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().exists("Deprecation")
                    .expectHeader().exists("Sunset")
                    .expectHeader().value(HttpHeaders.LINK, link ->
                            assertThat(link).contains("rel=\"deprecation\"")
                                    .contains("https://rashidi.zin.my/spring-boot-tutorials/web-api-versioning.html"))
                    .expectBody()
                    .jsonPath("$.length()").isEqualTo(4)
                    .jsonPath("$[0].sku").isEqualTo("HD-004")
                    .jsonPath("$[1].sku").isEqualTo("KB-001")
                    .jsonPath("$[1].name").isEqualTo("Mechanical Keyboard")
                    .jsonPath("$[1].price").isEqualTo(149.99)
                    .jsonPath("$[1].currency").isEqualTo("USD")
                    .jsonPath("$[1].availability").doesNotExist();
        }

        @Test
        @DisplayName("When GET /api/products/{sku} is requested with API-Version 1.0, Then a single legacy product with deprecation headers is returned")
        void returnsLegacyProductBySkuWithDeprecationHeaders() {
            versionedClient.get()
                    .uri("/api/products/{sku}", "KB-001")
                    .apiVersion("1.0")
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().exists("Deprecation")
                    .expectHeader().exists("Sunset")
                    .expectBody()
                    .jsonPath("$.sku").isEqualTo("KB-001")
                    .jsonPath("$.name").isEqualTo("Mechanical Keyboard")
                    .jsonPath("$.price").isEqualTo(149.99)
                    .jsonPath("$.currency").isEqualTo("USD")
                    .jsonPath("$.category").doesNotExist();
        }

        @Test
        @DisplayName("When GET /api/products/{sku} is requested for an unknown SKU on version 1.0, Then status is 404 Not Found with RFC 9457 ProblemDetail")
        void returnsNotFoundProblemDetailForUnknownSku() {
            versionedClient.get()
                    .uri("/api/products/{sku}", "UNKNOWN-999")
                    .apiVersion("1.0")
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
                    .expectBody()
                    .jsonPath("$.title").isEqualTo("Product Not Found")
                    .jsonPath("$.type").isEqualTo("https://rashidi.zin.my/problems/product-not-found")
                    .jsonPath("$.detail").isEqualTo("Product with SKU UNKNOWN-999 not found");
        }

        @Test
        @DisplayName("When POST /api/products is requested with API-Version 1.0, Then status is 404 Not Found because write support starts at 2.0+")
        void rejectsProductCreationOnVersionOne() {
            var request = new CreateProductRequest(
                    "WC-005", "4K Webcam", "Video", new BigDecimal("199.00"), "USD", 10, 3
            );

            versionedClient.post()
                    .uri("/api/products")
                    .apiVersion("1.0")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON);
        }

    }

    @Nested
    @DisplayName("Given API version 2.0 (default baseline contract)")
    class DefaultVersionTwo {

        @Test
        @DisplayName("When GET /api/products is requested without an API-Version header, Then default version 2.0 returns structured Money and stock availability without deprecation headers")
        void defaultsToVersionTwoWithStructuredMoneyAndStockAvailability() {
            restClient.get()
                    .uri("/api/products")
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().doesNotExist("Deprecation")
                    .expectHeader().doesNotExist("Sunset")
                    .expectBody()
                    .jsonPath("$.length()").isEqualTo(4)
                    .jsonPath("$[0].sku").isEqualTo("HD-004")
                    .jsonPath("$[0].stockQuantity").isEqualTo(0)
                    .jsonPath("$[0].availability").isEqualTo("OUT_OF_STOCK")
                    .jsonPath("$[1].sku").isEqualTo("KB-001")
                    .jsonPath("$[1].price.amount").isEqualTo(149.99)
                    .jsonPath("$[1].price.currency").isEqualTo("USD")
                    .jsonPath("$[1].stockQuantity").isEqualTo(25)
                    .jsonPath("$[1].availability").isEqualTo("IN_STOCK")
                    .jsonPath("$[2].sku").isEqualTo("MN-003")
                    .jsonPath("$[2].stockQuantity").isEqualTo(0)
                    .jsonPath("$[2].availability").isEqualTo("OUT_OF_STOCK")
                    .jsonPath("$[3].sku").isEqualTo("MS-002")
                    .jsonPath("$[3].stockQuantity").isEqualTo(3)
                    .jsonPath("$[3].availability").isEqualTo("LOW_STOCK");
        }

        @Test
        @DisplayName("When GET /api/products is requested on an empty catalog, Then an empty array is returned")
        void returnsEmptyListWhenCatalogIsEmpty() {
            jdbcClient.sql("TRUNCATE TABLE catalog_audit_entries, inventory_items, products RESTART IDENTITY")
                    .update();

            versionedClient.get()
                    .uri("/api/products")
                    .apiVersion("2.0")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.length()").isEqualTo(0);
        }

        @Test
        @DisplayName("When GET /api/products/{sku} is requested with API-Version 2.0 for an in-stock product, Then nested Money and IN_STOCK status are returned")
        void returnsVersionTwoProductBySku() {
            versionedClient.get()
                    .uri("/api/products/{sku}", "KB-001")
                    .apiVersion("2.0")
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().doesNotExist("Deprecation")
                    .expectBody()
                    .jsonPath("$.sku").isEqualTo("KB-001")
                    .jsonPath("$.name").isEqualTo("Mechanical Keyboard")
                    .jsonPath("$.price.amount").isEqualTo(149.99)
                    .jsonPath("$.price.currency").isEqualTo("USD")
                    .jsonPath("$.stockQuantity").isEqualTo(25)
                    .jsonPath("$.availability").isEqualTo("IN_STOCK")
                    .jsonPath("$.category").doesNotExist();
        }

        @Test
        @DisplayName("When GET /api/products/{sku} is requested with API-Version 2.0 for a product without an inventory row, Then OUT_OF_STOCK fallback is returned")
        void returnsOutOfStockFallbackWhenInventoryRecordIsMissing() {
            versionedClient.get()
                    .uri("/api/products/{sku}", "HD-004")
                    .apiVersion("2.0")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.sku").isEqualTo("HD-004")
                    .jsonPath("$.stockQuantity").isEqualTo(0)
                    .jsonPath("$.availability").isEqualTo("OUT_OF_STOCK");
        }

        @Test
        @DisplayName("When POST /api/products is called with API-Version 2.0, Then status is 201 Created, stock is registered, and an audit entry is recorded")
        void createsProductInitializesInventoryAndRecordsAuditEntry() {
            var request = new CreateProductRequest(
                    "WC-005", "4K Webcam", "Video", new BigDecimal("199.00"), "USD", 12, 4
            );

            versionedClient.post()
                    .uri("/api/products")
                    .apiVersion("2.0")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .exchange()
                    .expectStatus().isCreated()
                    .expectHeader().value(HttpHeaders.LOCATION, location ->
                            assertThat(location).endsWith("/api/products/WC-005"))
                    .expectBody()
                    .jsonPath("$.sku").isEqualTo("WC-005")
                    .jsonPath("$.name").isEqualTo("4K Webcam")
                    .jsonPath("$.price.amount").isEqualTo(199.00)
                    .jsonPath("$.price.currency").isEqualTo("USD")
                    .jsonPath("$.stockQuantity").isEqualTo(12)
                    .jsonPath("$.availability").isEqualTo("IN_STOCK");

            var entries = auditLog.findBySku("WC-005");
            assertThat(entries).hasSize(1);
            var entry = entries.getFirst();
            assertThat(entry.sku()).isEqualTo("WC-005");
            assertThat(entry.action()).isEqualTo("PRODUCT_CREATED");
            assertThat(entry.detail()).isEqualTo("Registered 4K Webcam in Video");
            assertThat(entry.createdAt()).isNotNull();
        }

        @Test
        @DisplayName("When POST /api/products is called with an existing SKU, Then status is 409 Conflict with RFC 9457 ProblemDetail")
        void rejectsDuplicateSkuWithConflictProblemDetail() {
            var duplicateRequest = new CreateProductRequest(
                    "KB-001", "Duplicate Keyboard", "Peripherals", new BigDecimal("120.00"), "USD", 5, 2
            );

            versionedClient.post()
                    .uri("/api/products")
                    .apiVersion("2.0")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(duplicateRequest)
                    .exchange()
                    .expectStatus().isEqualTo(409)
                    .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
                    .expectBody()
                    .jsonPath("$.title").isEqualTo("Duplicate Product SKU")
                    .jsonPath("$.type").isEqualTo("https://rashidi.zin.my/problems/duplicate-sku")
                    .jsonPath("$.detail").isEqualTo("Product with SKU KB-001 already exists");
        }

        @Test
        @DisplayName("When POST /api/products is called with an invalid payload, Then status is 400 Bad Request with RFC 9457 ProblemDetail")
        void rejectsInvalidProductPayload() {
            var invalidRequest = new CreateProductRequest(
                    "", "", "", new BigDecimal("-5.00"), "", -1, -1
            );

            versionedClient.post()
                    .uri("/api/products")
                    .apiVersion("2.0")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(invalidRequest)
                    .exchange()
                    .expectStatus().isBadRequest()
                    .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON);
        }

    }

    @Nested
    @DisplayName("Given API version 2.1 (minor evolution with baseline 2.0+ fallback)")
    class MinorEvolutionVersionTwoPointOne {

        @Test
        @DisplayName("When GET /api/products is requested with API-Version 2.1, Then the baseline 2.0+ handler serves the catalog seamlessly")
        void servesCatalogListViaBaselineTwoZeroPlus() {
            versionedClient.get()
                    .uri("/api/products")
                    .apiVersion("2.1")
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().doesNotExist("Deprecation")
                    .expectBody()
                    .jsonPath("$.length()").isEqualTo(4)
                    .jsonPath("$[1].sku").isEqualTo("KB-001")
                    .jsonPath("$[1].price.amount").isEqualTo(149.99)
                    .jsonPath("$[1].availability").isEqualTo("IN_STOCK");
        }

        @Test
        @DisplayName("When GET /api/products/{sku} is requested with API-Version 2.1 for a low-stock product, Then category and restockRecommended true are returned")
        void returnsVersionTwoPointOneDetailsForLowStockProduct() {
            versionedClient.get()
                    .uri("/api/products/{sku}", "MS-002")
                    .apiVersion("2.1")
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().doesNotExist("Deprecation")
                    .expectBody()
                    .jsonPath("$.sku").isEqualTo("MS-002")
                    .jsonPath("$.name").isEqualTo("Ergonomic Mouse")
                    .jsonPath("$.category").isEqualTo("Peripherals")
                    .jsonPath("$.price.amount").isEqualTo(79.50)
                    .jsonPath("$.price.currency").isEqualTo("USD")
                    .jsonPath("$.stockQuantity").isEqualTo(3)
                    .jsonPath("$.availability").isEqualTo("LOW_STOCK")
                    .jsonPath("$.restockRecommended").isEqualTo(true);
        }

        @Test
        @DisplayName("When GET /api/products/{sku} is requested with API-Version 2.1 for an in-stock product, Then category and restockRecommended false are returned")
        void returnsVersionTwoPointOneDetailsForInStockProduct() {
            versionedClient.get()
                    .uri("/api/products/{sku}", "KB-001")
                    .apiVersion("2.1")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.sku").isEqualTo("KB-001")
                    .jsonPath("$.category").isEqualTo("Peripherals")
                    .jsonPath("$.availability").isEqualTo("IN_STOCK")
                    .jsonPath("$.restockRecommended").isEqualTo(false);
        }

        @Test
        @DisplayName("When POST /api/products is called with API-Version 2.1, Then the baseline 2.0+ endpoint creates the product")
        void createsProductViaBaselineTwoZeroPlus() {
            var request = new CreateProductRequest(
                    "SP-006", "Studio Speakers", "Audio", new BigDecimal("299.00"), "USD", 2, 5
            );

            versionedClient.post()
                    .uri("/api/products")
                    .apiVersion("2.1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .exchange()
                    .expectStatus().isCreated()
                    .expectBody()
                    .jsonPath("$.sku").isEqualTo("SP-006")
                    .jsonPath("$.availability").isEqualTo("LOW_STOCK");
        }

    }

    @Nested
    @DisplayName("Given an unsupported or malformed API-Version header")
    class InvalidApiVersions {

        @Test
        @DisplayName("When GET /api/products is requested with unsupported API-Version 9.0, Then status is 400 Bad Request with RFC 9457 ProblemDetail")
        void rejectsUnsupportedApiVersion() {
            restClient.get()
                    .uri("/api/products")
                    .header(API_VERSION_HEADER, "9.0")
                    .exchange()
                    .expectStatus().isBadRequest()
                    .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
                    .expectBody()
                    .jsonPath("$.status").isEqualTo(400)
                    .jsonPath("$.title").isEqualTo("Bad Request");
        }

        @Test
        @DisplayName("When GET /api/products is requested with a malformed API-Version header, Then status is 400 Bad Request with RFC 9457 ProblemDetail")
        void rejectsMalformedApiVersion() {
            restClient.get()
                    .uri("/api/products")
                    .header(API_VERSION_HEADER, "not-a-version")
                    .exchange()
                    .expectStatus().isBadRequest()
                    .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
                    .expectBody()
                    .jsonPath("$.status").isEqualTo(400)
                    .jsonPath("$.title").isEqualTo("Bad Request");
        }

    }

}
