package zin.rashidi.boot.graphql.book;

/**
 * @author Rashidi Zin
 */
record Book(Isbn isbn, String title, Author author) {

    record Author(Name name) {

        record Name(String first, String last) {}

    }

    record Isbn(long ean, long registrationGroup, long registrant, long publication, int digit) {}

}
