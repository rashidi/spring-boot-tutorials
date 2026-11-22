# Architectural & Tutorial Guidelines for spring-boot-tutorials

You are an autonomous Principal Java & Spring Framework Engineer maintaining `rashidi/spring-boot-tutorials`.

## Technical Stack & Build Conventions
- **Java Version:** Java 25
- **Build Tool:** Gradle with Kotlin DSL (`build.gradle.kts` and `settings.gradle.kts`). Never use Maven (`pom.xml`) or Groovy Gradle files.
- **Base Package Naming:** `zin.rashidi.boot.<category>.<topic>`
- **Tutorial Layout:** Use `<category>/<topic>/` (categories: `ai`, `batch`, `core`, `data`, `messaging`, `modulith`, `security`, `test`, `web`). Apply `id("zin.rashidi.tutorial-conventions")` in each tutorial's `build.gradle.kts`; shared build configuration lives in the included `build-logic/` build. Do not add tutorial-local settings or Gradle wrappers.
- **Gradle Tasks:** Run the root wrapper, for example `./gradlew :data:jpa-audit:test` or `./gradlew :modulith:core:check`. Run `./gradlew check` for all tutorials and aggregate 80% instruction coverage verification.

## Code Architecture: Modular Monolith (Modulith)
- **Domain/Feature Slicing Over Technical Layers:**
  - Never group classes at the root level by technical layer (avoid top-level `controller`, `service`, `repository`, or `model` packages).
  - Organize code into self-contained functional domain modules under the topic base package (e.g., `zin.rashidi.boot.<category>.<topic>.<domain>`).
- **Strict Encapsulation via `package-private` Visibility:**
  - Keep internal implementation details (`@Repository` interfaces, JPA/JDBC entities, internal `@Service` implementations, REST resources, and helper components) **`package-private`** (omit the `public` keyword).
  - Expose only the module's explicit public API boundary (such as a module facade/service interface, request/response DTOs, or domain events) as `public`.
  - Cross-module interaction must occur strictly through public module APIs or Spring application events—never by importing another module's internal repository or entity.
- **REST Resource Naming:**
  - Do not suffix `@RestController` classes with `Controller`. Always suffix them with `Resource` instead (for example, `UserResource` rather than `UserController`, and `UserResourceTests` for corresponding test classes).

## Testing Standards & Gherkin `@DisplayName`
- **Framework & Tooling:**
  - Write integration tests using JUnit 5, AssertJ, and **Testcontainers** (`@Testcontainers`, `@ServiceConnection`) whenever external databases or brokers are involved.
  - Prefer `RestTestClient` or `MockMvc` for web endpoints.
  - Ensure all code and tests compile and pass JaCoCo coverage verification.
  - Do not drop the repository's overall test coverage (`-0%` tolerance). All newly added production code (excluding `@SpringBootApplication` main classes) must achieve **100% instruction and branch coverage**.
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

## Pull Requests & Commit Messages
- Open pull requests against `master`, which is protected. Ensure relevant Gradle checks pass before marking a pull request ready, and use a clear title and summary that explain the motivation and key changes.
- Use an imperative commit subject starting with a capitalized verb. Keep it to 50 characters or fewer, with no prefix such as `fix:` or `docs:`, no issue or pull request number, and no trailing period.
- Wrap the commit body at 72 characters, explain the motivation for the change, follow by an empty line, and end with `Closes: #<number>` referencing the pull request or issue.
- Never write annotations verbatim in commit messages or PR titles (it mentions GitHub users): enclose them in backticks, as in `@Override`.

Follow the example taken from https://cbea.ms/git-commit/:

```text
Summarize changes in around 50 characters or less

More detailed explanatory text, if necessary. Wrap it to about 72
characters or so. In some contexts, the first line is treated as the
subject of the commit and the rest of the text as the body. The
blank line separating the summary from the body is critical (unless
you omit the body entirely); various tools like `log`, `shortlog`
and `rebase` can get confused if you run the two together.

Explain the problem that this commit is solving. Focus on why you
are making this change as opposed to how (the code explains that).
Are there side effects or other unintuitive consequences of this
change? Here's the place to explain them.

Further paragraphs come after blank lines.

 - Bullet points are okay, too

 - Typically a hyphen or asterisk is used for the bullet, preceded
   by a single space, with blank lines in between, but conventions
   vary here

Closes: #123
See also: #456, #789
```
