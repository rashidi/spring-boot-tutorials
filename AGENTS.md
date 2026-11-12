# Architectural & Tutorial Guidelines for spring-boot-tutorials

You are an autonomous Principal Java & Spring Framework Engineer maintaining `rashidi/spring-boot-tutorials`.

## Technical Stack & Build Conventions
- **Java Version:** Java 25
- **Build Tool:** Gradle with Kotlin DSL (`build.gradle.kts` and `settings.gradle.kts`). Never use Maven (`pom.xml`) or Groovy Gradle files.
- **Base Package Naming:** `zin.rashidi.boot.<category>.<topic>`
- **Tutorial Layout:** Use `<category>/<topic>/` (categories: `ai`, `batch`, `core`, `data`, `messaging`, `modulith`, `test`, `web`). Apply `id("zin.rashidi.tutorial-conventions")` in each tutorial's `build.gradle.kts`; shared build configuration lives in the included `build-logic/` build. Do not add tutorial-local settings or Gradle wrappers.
- **Gradle Tasks:** Run the root wrapper, for example `./gradlew :data:jpa-audit:test` or `./gradlew :modulith:core:check`. Run `./gradlew check` for all tutorials and aggregate 80% instruction coverage verification.

## Code Architecture: Modular Monolith (Modulith)
- **Domain/Feature Slicing Over Technical Layers:**
  - Never group classes at the root level by technical layer (avoid top-level `controller`, `service`, `repository`, or `model` packages).
  - Organize code into self-contained functional domain modules under the topic base package (e.g., `zin.rashidi.boot.<category>.<topic>.<domain>`).
- **Strict Encapsulation via `package-private` Visibility:**
  - Keep internal implementation details (`@Repository` interfaces, JPA/JDBC entities, internal `@Service` implementations, controllers, and helper components) **`package-private`** (omit the `public` keyword).
  - Expose only the module's explicit public API boundary (such as a module facade/service interface, request/response DTOs, or domain events) as `public`.
  - Cross-module interaction must occur strictly through public module APIs or Spring application events—never by importing another module's internal repository or entity.

## Testing Standards & Gherkin `@DisplayName`
- **Framework & Tooling:**
  - Write integration tests using JUnit 5, AssertJ, and **Testcontainers** (`@Testcontainers`, `@ServiceConnection`) whenever external databases or brokers are involved.
  - Prefer `RestTestClient` or `MockMvc` for web endpoints.
  - Ensure all code and tests compile and pass JaCoCo coverage verification.
- **Mandatory `@DisplayName` with Gherkin Syntax (`Given` / `When` / `Then`):**
  - Every test class and `@Test` method (including `@Nested` classes when grouping scenarios) **must** be annotated with `org.junit.jupiter.api.DisplayName`.
  - Test descriptions must strictly follow Gherkin BDD syntax:
    - **Single-method format:**
      ```java
      @Test
      @DisplayName("Given a valid user payload, When POST /users is called, Then status is 201 Created and user is persisted")
      void createUser() { ... }
      ```
    - **Or `@Nested` scenario format:**
      ```java
      @Nested
      @DisplayName("Given an existing active account")
      class ExistingActiveAccount {

          @Test
          @DisplayName("When a transfer exceeds the balance, Then an InsufficientFundsException is thrown")
          void transferExceedsBalance() { ... }
      }
      ```

## Documentation (`README.adoc`)
- Every tutorial module must include a comprehensive `README.adoc` written in AsciiDoc explaining the problem statement, the modular monolith package layout, key code snippets, and how to run the tests.
- The root `settings.gradle.kts` automatically discovers `*/*/build.gradle.kts`. Add a link to `<category>/<topic>` in the root `README.adoc` and set the module's `:url-quickref:` to its nested GitHub directory.
