package pl.com.kozak.telemetry.creditwise.identityregistry.documents;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DocumentsConfiguration {

    @Bean
    DocumentFacade documentFacade() {
        return new DocumentFacade();
    }

}
