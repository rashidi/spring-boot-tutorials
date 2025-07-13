package zin.rashidi.datarest.compositeid.book;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

/**
 * @author Rashidi Zin
 */
@ReadingConverter
class AuthorIdReferencedConverter implements Converter<String, Author.Id> {

    @Override
    public Author.Id convert(String source) {
        return new Author.Id().id(Long.parseLong(source));
    }

}
