package msa.external;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ExternalApplication {
	public static void main(String[] args) {
		SpringApplication.run(ExternalApplication.class, args);
	}
}
