# Spring Data Jpa Audit Example
Enable auditing with Spring Data Jpa's `@CreatedDate` and `@LastModified`. For example with Spring Data MongoDB, please check out [Spring Data MongoDB Audit Example](../data-mongodb-audit).

## Background
[Spring Data Jpa][1] provides auditing feature which includes `@CreateDate`, `@CreatedBy`, `@LastModifiedDate`,
and `@LastModifiedBy`. In this example we will see how it can be implemented with very little configurations.

## Entity Class
In this example we have an entity class, [User][2] which contains information about the table structure. Initial
structure is as follows:

```java
@Entity
class User {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String username;

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private Instant created;

    @LastModifiedBy
    private String modifiedBy;

    @LastModifiedDate
    private Instant modified;

    // omitted getter / setter
}
```

As you can see it is a standard implementation of `@Entity` JPA class. We would like to keep track when an entry is
created with `created` column and when it is modified with `modified` column.

## Enable JpaAudit
In order to enable [JPA Auditing][3] for this project will need to apply three annotations and a configuration class.
Those annotations are; `@EntityListener`, `@CreatedDate`, and `@LastModifiedDate`.

`@EntityListener` will be the one that is responsible to listen to any create or update activity. It requires
`Listeners` to be defined. In this example we will use the default class, `EntityListeners`.

By annotating a column with `@CreatedDate` we will inform Spring that we need this column to have information on
when the entity is created. While `@LastModifiedDate` column will be defaulted to `@CreatedDate` and will be updated
to the current time when the entry is updated.

The final look of `User` class:

```java
@Entity
@EntityListeners(AuditingEntityListener.class)
class User {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String username;

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private Instant created;

    @LastModifiedBy
    private String modifiedBy;

    @LastModifiedDate
    private Instant modified;

    // omitted getter / setter
}
```

As you can see `User` is now annotated with `@EntityListeners` while `created`, `createdBy`, `modified`, and `modifiedBy` columns are annotated
with `@CreatedDate`, `@CreatedBy`, `@LastModifiedDate`, and `@LastModifiedBy`. `createdBy` and `modifiedBy` fields will be automatically populated
if [Spring Security][6] is available in the project path. Alternatively we wil implement our own [AuditorAware][7] in order to inform Spring who
is the current auditor.

We will do so in [AuditConfiguration][4] class. In this class, we will also inform Spring to enable JPA auditing by annotating it with
`@EnableJpaAuditing` annotation.

```java
@Configuration
@EnableJpaAuditing
class AuditConfiguration {

    @Bean
    public AuditorAware<String> auditorAwareRef() {
        return () -> Optional.of("Mr. Auditor");
    }

}
```

That's it! Our application has JPA Auditing feature enabled. The result can be seen in [UserAuditTests][5].

## Verify Audit Implementation
There is no better way to verify an implementation other than running some tests. In our test class we have to scenario:

- Create an entity which will have `created` and `modified` fields has values without us assigning them
- Update created entity and `created` field will remain to have the same value while `modified` values will be updated

### Create an entity
In the following test we will see that values for `created` and `modified` are assigned by Spring itself:

```java
@Testcontainers
@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop", includeFilters = @Filter(type = ANNOTATION, classes = EnableJpaAuditing.class))
class UserAuditTests {

    @Container
    @ServiceConnection
    private final static MySQLContainer MYSQL = new MySQLContainer("mysql:latest");

    @Autowired
    private UserRepository repository;

    @Test
    @DisplayName("When a user is saved Then created and modified fields are set And createdBy and modifiedBy fields are set to Mr. Auditor")
    void create() {
        var user = new User();

        user.setName("Rashidi Zin");
        user.setUsername("rashidi");

        var createdUser = repository.save(user);

        assertThat(createdUser).extracting("created", "modified").isNotNull();
        assertThat(createdUser).extracting("createdBy", "modifiedBy").containsOnly("Mr. Auditor");
    }

}
```

As mentioned earlier, we did not assign values for `created` and `modified` fields but Spring will assign them for us.
Same goes with when we are updating an entry.

### Update an entity
In the following test we will change the `username` without changing `modified` field. We will expect that `modified`
field will have a recent time as compare to when it was created:

```java
@Testcontainers
@AutoConfigureTestDatabase(replace = NONE)
@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop", includeFilters = @Filter(type = ANNOTATION, classes = EnableJpaAuditing.class))
class UserAuditTests {

    @Container
    @ServiceConnection
    private final static MySQLContainer MYSQL = new MySQLContainer("mysql:latest");

    @Autowired
    private UserRepository repository;

    @Test
    @DisplayName("When a user is updated Then modified field should be updated")
    void update() {
        var user = new User();

        user.setName("Rashidi Zin");
        user.setUsername("rashidi");

        var createdUser = repository.save(user);

        await().atMost(ofSeconds(1)).untilAsserted(() -> {
            createdUser.setUsername("rashidi.zin");

            var modifiedUser = repository.save(createdUser);

            assertThat(modifiedUser.getModified()).isAfter(createdUser.getModified());
        });
    }

}
```

As you can see at our final verification we assert that `modified` field should have a greater value than it
previously had.

## Conclusion
To recap. All we need in order to enable JPA auditing feature in this project are:

- `@EnableJpaAuditing`
- `@EntityListeners`
- `@CreatedBy`
- `@CreatedDate`
- `@LastModifiedBy`
- `@LastModifiedDate`

[1]: http://docs.spring.io/spring-data/jpa/docs/current/reference/html/
[2]: src/main/java/zin/rashidi/boot/data/user/User.java
[3]: http://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.auditing
[4]: src/main/java/zin/rashidi/boot/data/audit/AuditConfiguration.java
[5]: src/test/java/zin/rashidi/boot/data/user/UserAuditTests.java
[6]: https://projects.spring.io/spring-security/
[7]: https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/domain/AuditorAware.html
