package zin.rashidi.datarest.compositeid.book;

import org.springframework.data.rest.webmvc.spi.BackendIdConverter;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author Rashidi Zin
 */
@Component
class BookIdConverter implements BackendIdConverter {

    @Override
    public Serializable fromRequestId(String id, Class<?> entityType) {
        return new Book.Isbn(id);
    }

    @Override
    public String toRequestId(Serializable id, Class<?> entityType) {
        return id.toString();
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Book.class.isAssignableFrom(aClass);
    }

}
