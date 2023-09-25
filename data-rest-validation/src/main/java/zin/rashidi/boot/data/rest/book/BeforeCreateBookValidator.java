package zin.rashidi.boot.data.rest.book;

import static zin.rashidi.boot.data.rest.book.Author.Status.INACTIVE;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author Rashidi Zin
 */
class BeforeCreateBookValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Book.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Book book = (Book) target;

        if (book.getAuthor().getStatus() == INACTIVE) {
            errors.rejectValue("author", "author.inactive", "Author is inactive");
        }

    }

}
