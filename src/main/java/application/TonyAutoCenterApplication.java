package application;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "application",
        "model"
})
@EntityScan("model.entities")
@EnableJpaRepositories(basePackages = "model.repositories")
public class TonyAutoCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(TonyAutoCenterApplication.class, args);
    }
}