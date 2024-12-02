package pl.com.kozak.telemetry.creditwise.creditcards;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
public class CreditCardsApplications {

    public static void main(String[] args) {
        SpringApplication.run(CreditCardsApplications.class, args);
    }

}
