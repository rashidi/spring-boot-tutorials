package zin.rashidi.boot.jdbi.book;

/**
 * @author Rashidi Zin
 */
class Book {

    private final String isbn;
    private final String title;
    private final Author author;

    Book(String isbn, String title, Author author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
    }

    String isbn() {
        return isbn;
    }

    String title() {
        return title;
    }

    Author author() {
        return author;
    }

}
