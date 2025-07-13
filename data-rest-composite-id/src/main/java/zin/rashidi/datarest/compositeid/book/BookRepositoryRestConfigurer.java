package zin.rashidi.datarest.compositeid.book;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

/**
 * @author Rashidi Zin
 */
@Configuration
class BookRepositoryRestConfigurer implements RepositoryRestConfigurer {

    @Override
    public void configureConversionService(ConfigurableConversionService conversionService) {
        conversionService.addConverter(new AuthorIdReferencedConverter());
    }

}
