package application;

import model.entities.Client;
import model.entities.ServiceItem;
import model.entities.ServiceOrder;
import model.entities.Vehicle;
import model.services.ClientService;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {

            ClientService clientService = new ClientService();

            Client client = new Client(
                    "Nicolas Brayan",
                    "12345678900",
                    "(21)99999-9999",
                    "Rio de Janeiro"
            );


        clientService.registerClient(client);


        System.out.println();

            Vehicle vehicle = new Vehicle(
                    "ABC1D23",
                    "Honda",
                    "Civic",
                    2020,
                    55000
            );

            client.addVehicle(vehicle);



            ServiceOrder order = vehicle.openServiceOrder(
                    "Motor fazendo barulho",
                    "Cliente informou ruído ao acelerar"
            );

            order.addService(new ServiceItem(
                    "Troca de óleo",
                    1,
                    new BigDecimal("150.00")
            ));

            order.addService(new ServiceItem(
                    "Troca do filtro",
                    3,
                    new BigDecimal("50.00")
            ));

            order.close();

            System.out.println(order.toString());

    }
}