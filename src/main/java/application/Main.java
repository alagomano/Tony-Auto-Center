package application;

import model.entities.Client;
import model.entities.ServiceItem;
import model.entities.ServiceOrder;
import model.entities.Vehicle;
import model.enums.OrderStatus;
import model.exception.ServiceException;
import model.services.ClientService;
import model.services.VehicleService;

import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) {

        ClientService clientService = new ClientService();
        VehicleService vehicleService = new VehicleService(clientService);

        System.out.println("===== TESTE 1 - CADASTRO DE CLIENTE =====");

        Client client = new Client(
                "Nicolas Brayan",
                "12345678900",
                "21999999999",
                "Rio de Janeiro"
        );

        clientService.registerClient(client);

        System.out.println("Cliente cadastrado com sucesso!");



        System.out.println("\n===== TESTE 2 - CPF DUPLICADO =====");

        try {
            clientService.registerClient(client);
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }



        System.out.println("\n===== TESTE 3 - CADASTRO DE VEÍCULO =====");

        Vehicle civic = new Vehicle(
                "ABC1D23",
                "Honda",
                "Civic",
                2020
        );

        vehicleService.registerVehicle(
                "12345678900",
                civic
        );

        System.out.println("Veículo cadastrado!");



        System.out.println("\n===== TESTE 4 - PLACA DUPLICADA =====");

        try {
            vehicleService.registerVehicle(
                    "12345678900",
                    civic
            );
        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }



        System.out.println("\n===== TESTE 5 - BUSCA DE VEÍCULO =====");

        Vehicle vehicle = vehicleService.findVehicle(
                "12345678900",
                "ABC1D23"
        );

        System.out.println(vehicle.getPlate());



        System.out.println("\n===== TESTE 6 - ABERTURA DE OS =====");

        ServiceOrder order =
                vehicleService.openServiceOrder(
                        "12345678900",
                        "ABC1D23",
                        "Motor falhando",
                        "Cliente relatou perda de potência"
                );

        System.out.println("OS aberta!");



        System.out.println("\n===== TESTE 7 - ADICIONAR SERVIÇOS =====");

        order.addService(
                new ServiceItem(
                        "Troca de óleo",
                        1,
                        new BigDecimal("150.00")
                )
        );

        order.addService(
                new ServiceItem(
                        "Filtro de óleo",
                        1,
                        new BigDecimal("50.00")
                )
        );

        System.out.println("Total: R$ " + order.calculateTotal());



        System.out.println("\n===== TESTE 8 - ABRIR SEGUNDA OS =====");

        try {

            vehicleService.openServiceOrder(
                    "12345678900",
                    "ABC1D23",
                    "Freio",
                    "Pastilha gasta"
            );

        } catch (ServiceException e) {

            System.out.println(e.getMessage());

        }



        System.out.println("\n===== TESTE 9 - ALTERAR STATUS =====");

        order.update(OrderStatus.IN_PROGRESS);

        System.out.println("Status alterado para IN_PROGRESS");



        System.out.println("\n===== TESTE 10 - FECHAR OS =====");

        order.close();

        System.out.println("OS finalizada!");



        System.out.println("\n===== TESTE 11 - ENTREGAR OS =====");

        order.update(OrderStatus.DELIVERED);

        System.out.println("OS entregue!");



        System.out.println("\n===== TESTE 12 - ALTERAR OS ENTREGUE =====");

        try {

            order.update(OrderStatus.OPEN);

        } catch (ServiceException e) {

            System.out.println(e.getMessage());

        }



        System.out.println("\n===== TESTE 13 - HISTÓRICO =====");

        System.out.println(
                vehicle.getHistory().size()
        );



        System.out.println("\n===== TESTE 14 - VEÍCULO INEXISTENTE =====");

        try {

            vehicleService.findVehicle(
                    "12345678900",
                    "XYZ9999"
            );

        } catch (ServiceException e) {

            System.out.println(e.getMessage());

        }



        System.out.println("\n===== TESTE 15 - CLIENTE INEXISTENTE =====");

        try {

            clientService.findClient(
                    "00000000000"
            );

        } catch (ServiceException e) {

            System.out.println(e.getMessage());

        }



        System.out.println("\n===== FIM DOS TESTES =====");
    }
}