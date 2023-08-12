package zin.rashidi.boot.graphql.book;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.stereotype.Repository;

import static org.springframework.context.annotation.FilterType.ANNOTATION;
import static org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE;

/**
 * @author Rashidi Zin
 */
@GraphQlTest(controllers = BookResource.class, includeFilters = @Filter(type = ANNOTATION, classes = {Configuration.class, Repository.class}))
class BookResourceTests {

    @Autowired
    private GraphQlTester client;

    @Test
    @DisplayName("Given there are 3 books, when findAll is invoked, then return all books")
    void findAll() {
        client.documentName("books")
                .execute()
                .path("findAll")
                .matchesJson(
                        """
                                        [
                                              {
                                                "title": "Clean Code"
                                              },
                                              {
                                                "title": "Design Patterns"
                                              },
                                              {
                                                "title": "The Hobbit"
                                              }
                                            ]
                                """
                )
        ;
    }

    @Test
    @DisplayName("Given there is a book titled The Hobbit, when findByTitle is invoked, then return the book")
    void findByTitle() {
        client.documentName("books")
                .variable("title", "The Hobbit")
                .execute()
                .path("findByTitle")
                .matchesJson(
                        """
                                {
                                  "title": "The Hobbit",
                                  "isbn": {
                                    "ean": 9780132350884,
                                    "registrationGroup": 978,
                                    "registrant": 0,
                                    "publication": 13235088,
                                    "digit": 4
                                  },
                                  "author": {
                                    "name": {
                                      "first": "J.R.R.",
                                      "last": "Tolkien"
                                    }
                                  }
                                }
                                """
                );
    }

}