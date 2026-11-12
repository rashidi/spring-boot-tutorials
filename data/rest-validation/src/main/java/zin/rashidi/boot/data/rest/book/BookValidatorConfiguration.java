package zin.rashidi.boot.data.rest.book;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

/**
 * @author Rashidi Zin
 */
@Configuration
class BookValidatorConfiguration implements RepositoryRestConfigurer {

    @Override
    public void configureValidatingRepositoryEventListener(ValidatingRepositoryEventListener validatingListener) {
        validatingListener.addValidator("beforeCreate", new BeforeCreateBookValidator());
    }

}
