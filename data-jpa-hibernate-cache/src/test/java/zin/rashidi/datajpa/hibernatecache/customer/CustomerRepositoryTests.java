package zin.rashidi.datajpa.hibernatecache.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import zin.rashidi.datajpa.hibernatecache.TestcontainersConfiguration;

/**
 * @author Rashidi Zin
 */
@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@Import(TestcontainersConfiguration.class)
class CustomerRepositoryTests {

    @Autowired
    private CustomerRepository customers;

}