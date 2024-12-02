package pl.com.kozak.telemetry.creditwise.identityregistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class IdentityRegistryApplication {

    public static void main(String[] args) {
        SpringApplication.run(IdentityRegistryApplication.class, args);
    }

}
