## 2024-10-27 - FetchType.LAZY Optimization
**Learning:** By default, JPA `@ManyToOne` and `@OneToOne` associations are fetched eagerly, which can lead to N+1 query problems. Using `FetchType.LAZY` mitigates this issue and improves data fetching performance when the associated entities are not strictly needed. Also, `import jakarta.persistence.*;` is already present in these files so `FetchType` compiles successfully without additional imports.
**Action:** Always prefer `FetchType.LAZY` for `@ManyToOne` and `@OneToOne` relationships in JPA unless EAGER fetching is explicitly required by the business logic or view.
## 2024-10-27 - Object Allocation Optimization in Repositories
**Learning:** Hardcoded sample data returned directly inside repository or service methods (like `List.of(...)`) causes unnecessary object allocations and Garbage Collection pressure on every invocation.
**Action:** Always extract hardcoded sample data into `private static final` immutable collections (like `List.of(...)`) to cache it at the class level and reuse the single instance across calls.
## 2024-10-27 - Reflection Overhead in Data Repositories
**Learning:** In Spring Data repositories (like `JpaCustomBaseRepository`), executing Reflection checks (e.g. `ReflectionUtils.getDeclaredMethods` or `ReflectionUtils.findField`) inside query execution paths like `findAll()` causes significant performance overhead and object allocations on every database query.
**Action:** Always compute class-level reflection and metadata checks exactly once during the class's constructor or initialization phase, and cache the result in a `private final` variable to completely eliminate the runtime overhead.
