package application;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

@SpringBootApplication(scanBasePackages = {
        "application",
        "model"
})
@EntityScan("model.entities")
public class TonyAutoCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(TonyAutoCenterApplication.class, args);
    }
}