package zin.rashidi.boot.data.jpa.transactional.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * @author Rashidi Zin
 */
@Configuration(proxyBeanMethods = false)
class ReadOnlyEntityManagerFactoryConfiguration {

    @Qualifier("readOnly")
    @Bean(defaultCandidate = false)
    @ConfigurationProperties("spring.data.jpa.read-only")
    public JpaProperties readOnlyJpaProperties() {
        return new JpaProperties();
    }

    private JpaVendorAdapter readOnlyJpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

}
