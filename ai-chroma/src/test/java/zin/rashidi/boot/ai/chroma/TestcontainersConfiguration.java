package zin.rashidi.boot.ai.chroma;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.chromadb.ChromaDBContainer;

/**
 * @author Rashidi Zin
 */
@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    public ChromaDBContainer chromaDBContainer() {
        return new ChromaDBContainer("chromadb/chroma:0.5.20");
    }

}
