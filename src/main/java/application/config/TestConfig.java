package application.config;

import model.entities.Client;
import model.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {
    private ClientService clientService;
    public TestConfig(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public void run(String... args) throws Exception {
        Client c1 = new Client("Nicolas Brayan", "123.123.123-10", "21", "Murim");
        Client c2 = new Client("Alex Red", "123.123.123-11", "82", "Roça");
        Client c3 = new Client("Bob Blue", "123.123.123-12", "79", "Casa de vó");

        clientService.registerClient(c1);
        clientService.registerClient(c2);
        clientService.registerClient(c3);

    }
}
