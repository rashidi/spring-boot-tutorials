package zin.rashidi.datarest.compositeid.book;

import org.springframework.data.rest.webmvc.spi.BackendIdConverter;
import org.springframework.stereotype.Component;

import java.io.Serializable;

import static java.lang.Long.parseLong;

/**
 * @author Rashidi Zin
 */
@Component
class AuthorIdConverter implements BackendIdConverter {

    @Override
    public Serializable fromRequestId(String id, Class<?> entityType) {
        return new Author.Id().id(parseLong(id));
    }

    @Override
    public String toRequestId(Serializable id, Class<?> entityType) {
        return ((Author.Id) id).id().toString();
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Author.class.isAssignableFrom(aClass);
    }

}
