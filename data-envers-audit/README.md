# Spring Data Envers: Audit With Entity Revisions
Sample application that demonstrates entity revisions with [Spring Data Envers][1].

# Background
[Spring Data Jpa][2] provides rough audit information. However if you are looking for what are the exact changes being
made to an entity you can do so with [Spring Data Envers][1].

As the name has suggested [Spring Data Envers][1] utilises and simplifies the usage of [Hibernate Envers][3].

# Dependency and Configuration
In order to enable Envers features we will first include **spring-data-envers** as dependency.

```xml
<dependency>
  <groupId>org.springframework.data</groupId>
  <artifactId>spring-data-envers</artifactId>
</dependency>
```

Next is to inform Spring Boot that we would like do enable Envers' features. This can be done by annotating a `@Configuration`
class with `@EnableJpaRepositories(repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class)`.

Example can be seen in [RepositoryConfiguration][4]:

```java
@Configuration
@EnableJpaRepositories(
        repositoryFactoryBeanClass = EnversRevisionRepositoryFactoryBean.class
)
public class RepositoryConfiguration {
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

    @NotBlank
    private String author;

    @NotBlank
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
@SpringBootTest
class BookRepositoryRevisionsTest {

    @Autowired
    private BookRepository repository;
    
    @Test
    void initialRevision() {
        Book book = repository.save(
                             Book.builder().author("Rudyard Kipling").title("Jungle Book").build()
                     );
        
        Revisions<Integer, Book> revisions = repository.findRevisions(book.getId());

        assertThat(revisions)
                .isNotEmpty()
                .allSatisfy(revision -> assertThat(revision.getEntity())
                        .extracting(Book::getId, Book::getAuthor, Book::getTitle)
                        .containsExactly(book.getId(), book.getAuthor(), book.getTitle())
                );
    }
}
```

### Revision Number Will Be Increase and Latest Revision is Available
```java
@SpringBootTest
class BookRepositoryRevisionsTest {

    @Autowired
    private BookRepository repository;
    
    @Test
    void updateIncreasesRevisionNumber() {
        Book book = repository.save(
                             Book.builder().author("Rudyard Kipling").title("Jungle Book").build()
                     );
    
        book.setTitle("If");

        repository.save(book);

        Optional<Revision<Integer, Book>> revision = repository.findLastChangeRevision(book.getId());

        assertThat(revision)
                .isPresent()
                .hasValueSatisfying(rev ->
                        assertThat(rev.getRevisionNumber()).hasValue(2)
                )
                .hasValueSatisfying(rev ->
                        assertThat(rev.getEntity())
                                .extracting(Book::getTitle)
                                .containsOnly("If")
                );
    }
}
```

### Upon Deletion All Entity Information Will be Removed Except its ID
```java
@SpringBootTest
class BookRepositoryRevisionsTest {

    @Autowired
    private BookRepository repository;
    
    @Test
    void deletedItemWillHaveRevisionRetained() {
        Book book = repository.save(
                             Book.builder().author("Rudyard Kipling").title("Jungle Book").build()
                     );

        repository.delete(book);

        Revisions<Integer, Book> revisions = repository.findRevisions(book.getId());

        assertThat(revisions).hasSize(2);

        Iterator<Revision<Integer, Book>> iterator = revisions.iterator();

        Revision<Integer, Book> initialRevision = iterator.next();
        Revision<Integer, Book> finalRevision = iterator.next();

        assertThat(initialRevision)
                .satisfies(rev ->
                        assertThat(rev.getEntity())
                                .extracting(Book::getId, Book::getAuthor, Book::getTitle)
                                .containsExactly(book.getId(), book.getAuthor(), book.getTitle())
                );

        assertThat(finalRevision)
                .satisfies(rev -> assertThat(rev.getEntity())
                        .extracting(Book::getId, Book::getTitle, Book::getAuthor)
                        .containsExactly(book.getId(), null, null)
                );
    }
}
```

All tests above can be found in [BookRepositoryRevisionsTest][8].

[1]: http://projects.spring.io/spring-data-envers/
[2]: https://projects.spring.io/spring-data-jpa/
[3]: http://hibernate.org/orm/envers/
[4]: src/main/java/rz/demo/boot/data/envers/RepositoryConfiguration.java
[5]: src/main/java/rz/demo/boot/data/envers/book/Book.java
[6]: https://github.com/spring-projects/spring-data-commons/blob/master/src/main/java/org/springframework/data/repository/history/RevisionRepository.java
[7]: src/main/java/rz/demo/boot/data/envers/book/BookRepository.java
[8]: src/test/java/rz/demo/boot/data/envers/book/BookRepositoryRevisionsTest.java
