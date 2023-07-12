# Spring Data Envers: Audit With Entity Revisions
Sample application that demonstrates entity revisions with [Spring Data Envers][1].

# Background
[Spring Data Jpa][2] provides rough audit information. However if you are looking for what are the exact changes being
made to an entity you can do so with [Spring Data Envers][1].

As the name has suggested [Spring Data Envers][1] utilises and simplifies the usage of [Hibernate Envers][3].

# Dependency and Configuration
In order to enable Envers features we will first include **spring-data-envers** as dependency.

```groovy
implementation 'org.springframework.data:spring-data-envers'
```

Next is to inform Spring Boot that we would like do enable Envers' features. This can be done by annotating a `@Configuration`
class with `@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)`.

Example can be seen in [RepositoryConfiguration][4]:

```java
@Configuration
@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)
class RepositoryConfiguration {
}
```

## Enable Entity Audit
By annotating an `@Entity` with `@Audited`, we are informing Spring that we would like respective entity to be audited.
The following example shows that we want all activities related to [Book][5] to be audited:

```java
@Entity
@Audited
public class Book {

    @Id
    @GeneratedValue
    private Long id;

    private String author;

    private String title;
}
```

Next is to extend a `Repository` class in order to allow us to utilise audit revision features. This can be done by extending
[RevisionRepository][6] interface to our `Repository` class. An example can be seen in [BookRepository][7]:

```java
public interface BookRepository extends JpaRepository<Book, Long>, RevisionRepository<Book, Long, Integer> {

}
```

## Verification
We will be utilising on `@SpringBootTest` to verify that our implementation works.

### Upon Creation an Initial Revision is Created
```java
@Testcontainers
@SpringBootTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
class BookAuditRevisionTests {

    @Container
    @ServiceConnection
    private static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:latest");

    @Autowired
    private BookRepository repository;

    @Test
    @DisplayName("When a book is created, then a revision information is available with revision number 1")
    void create() {
        var book = new Book();

        book.setTitle("The Jungle Book");
        book.setAuthor("Rudyard Kipling");

        var createdBook = repository.save(book);

        var revisions = repository.findRevisions(createdBook.getId());

        assertThat(revisions)
                .hasSize(1)
                .first()
                .extracting(Revision::getRevisionNumber)
                .returns(1, Optional::get);
    }
    
}
```

### Revision Number Will Be Increase and Latest Revision is Available
```java
@Testcontainers
@SpringBootTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
class BookAuditRevisionTests {

    @Container
    @ServiceConnection
    private static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:latest");

    @Autowired
    private BookRepository repository;

    @Test
    @DisplayName("When a book is modified, then a revision number will increase")
    void modify() {
        var book = new Book();

        book.setTitle("The Jungle Book");
        book.setAuthor("Rudyard Kipling");

        var createdBook = repository.save(book);

        createdBook.setTitle("If");

        repository.save(createdBook);

        var revisions = repository.findRevisions(createdBook.getId());

        assertThat(revisions)
                .hasSize(2)
                .last()
                .extracting(Revision::getRevisionNumber)
                .extracting(Optional::get).is(matching(greaterThan(1)));
    }

}
```

### Upon Deletion All Entity Information Will be Removed Except its ID
```java
@Testcontainers
@SpringBootTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
class BookAuditRevisionTests {

    @Container
    @ServiceConnection
    private static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:latest");

    @Autowired
    private BookRepository repository;
    
    @Test
    @DisplayName("When a book is removed, then only ID information is available")
    void remove() {
        var book = new Book();

        book.setTitle("The Jungle Book");
        book.setAuthor("Rudyard Kipling");

        var createdBook = repository.save(book);

        repository.delete(createdBook);

        var revision = repository.findLastChangeRevision(createdBook.getId());

        assertThat(revision).get()
                .extracting(Revision::getEntity)
                .extracting("id", "title", "author")
                .containsOnly(createdBook.getId(), null, null);
    }

}
```

All tests above can be found in [BookAuditRevisionTests][8].

[1]: http://projects.spring.io/spring-data-envers/
[2]: https://projects.spring.io/spring-data-jpa/
[3]: http://hibernate.org/orm/envers/
[4]: src/main/java/zin/rashidi/boot/data/envers/repository/RepositoryConfiguration.java
[5]: src/main/java/zin/rashidi/boot/data/envers/book/Book.java
[6]: https://github.com/spring-projects/spring-data-commons/blob/master/src/main/java/org/springframework/data/repository/history/RevisionRepository.java
[7]: src/main/java/zin/rashidi/boot/data/envers/book/BookRepository.java
[8]: src/test/java/zin/rashidi/boot/data/envers/BookAuditRevisionTests.java
