package application.config;

import model.entities.Client;
import model.entities.ServiceItem;
import model.entities.ServiceOrder;
import model.entities.Vehicle;
import model.services.ClientService;
import model.services.ServiceOrderService;
import model.services.VehicleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {
    private ClientService clientService;
    private VehicleService vehicleService;
    private ServiceOrderService serviceOrderService;

    public TestConfig(ClientService clientService, VehicleService vehicleService, ServiceOrderService serviceOrderService) {
        this.clientService = clientService;
        this.vehicleService = vehicleService;
        this.serviceOrderService = serviceOrderService;
    }

    @Override
    public void run(String... args) throws Exception {

        /*
         * ==========================================================
         * 1. TESTES DE CLIENTES
         * ==========================================================
         */

        System.out.println("\n========== CLIENTES ==========");

        Client c1 = new Client(
                "Nicolas Brayan",
                "123.123.123-10",
                "21",
                "Murim"
        );

        Client c2 = new Client(
                "Alex Red",
                "123.123.123-11",
                "82",
                "Roça"
        );

        Client c3 = new Client(
                "Bob Blue",
                "123.123.123-12",
                "79",
                "Casa de vó"
        );

        // CREATE
        clientService.registerClient(c1);
        clientService.registerClient(c2);
        clientService.registerClient(c3);

        // READ - por ID
        Client findById = clientService.findClientById(c1.getId());

        // READ - por CPF
        Client findByCpf = clientService.findClientByCpf("123.123.123-11");

        System.out.println("Cliente por ID:");
        System.out.println(findById);

        System.out.println("Cliente por CPF:");
        System.out.println(findByCpf);

        // UPDATE
        c3 = clientService.findClientById(c3.getId());
        c3.setName("Alex Red Brown");
        clientService.updateClient(c3);

        System.out.println("Cliente atualizado:");
        System.out.println(clientService.findClientById(c3.getId()));

        // READ - todos
        System.out.println("Todos os clientes:");
        clientService.getClients().forEach(System.out::println);

        /*
         * ==========================================================
         * 2. TESTES DE VEÍCULOS
         * ==========================================================
         */

        System.out.println("\n========== VEÍCULOS ==========");

        Vehicle v1 = new Vehicle(
                "ABC123",
                "Fiat",
                "Uno fire 1.0",
                2008,
                c2
        );

        Vehicle v2 = new Vehicle(
                "ABC012",
                "Toyota",
                "Corolla",
                2020,
                c3
        );

        Vehicle v3 = new Vehicle(
                "ADS021",
                "Renault",
                "Etios 1.6",
                2012,
                c3
        );

        Vehicle v4 = new Vehicle(
                "TEST01",
                "Renault",
                "Etios 1.6",
                2012,
                c2
        );

        // CREATE
        vehicleService.registerVehicle(c2.getId(), v1);
        vehicleService.registerVehicle(c3.getId(), v2);
        vehicleService.registerVehicle(c3.getId(), v3);
        vehicleService.registerVehicle(c2.getId(), v4);

        // READ - por ID
        Vehicle findVehicleById =
                vehicleService.findVehicleById(v1.getId());

        // READ - por placa
        Vehicle findVehicleByPlate =
                vehicleService.findVehicleByPlate("ADS021");

        System.out.println("Veículo por ID:");
        System.out.println(findVehicleById);

        System.out.println("Veículo por placa:");
        System.out.println(findVehicleByPlate);

        // UPDATE
        v4 = vehicleService.findVehicleById(v4.getId());
        v4.setYear(2026);
        vehicleService.updateVehicle(v4);

        System.out.println("Veículo atualizado:");
        System.out.println(vehicleService.findVehicleById(v4.getId()));

        // READ - veículos de um cliente
        System.out.println("Veículos do cliente " + c3.getId() + ":");
        clientService.getVehiclesByClient(c3.getId())
                .forEach(System.out::println);

        // READ - todos
        System.out.println("Todos os veículos:");
        vehicleService.getVehicles()
                .forEach(System.out::println);

        /*
         * ==========================================================
         * 3. TESTES DE ORDENS DE SERVIÇO
         * ==========================================================
         */

        System.out.println("\n========== ORDENS DE SERVIÇO ==========");

        // CREATE
        ServiceOrder order1 = vehicleService.openServiceOrder(
                v3.getId(),
                "Troca de Óleo",
                "Revisão completa: Pedido do Cliente."
        );

        ServiceOrder orderDeleteTest = vehicleService.openServiceOrder(
                v2.getId(),
                "Problema para remoção",
                ""
        );

        System.out.println("Ordem criada:");
        System.out.println(order1);

        // READ - por ID
        order1 = serviceOrderService.findServiceOrderById(
                order1.getId()
        );

        System.out.println("Ordem por ID:");
        System.out.println(order1);

        // READ - ordens do veículo
        System.out.println("Ordens do veículo " + v3.getId() + ":");

        vehicleService.getOrders(v3.getId())
                .forEach(System.out::println);

        // UPDATE
        order1.setObservations(
                "Revisão Completa: Pedido do Cliente."
        );

        serviceOrderService.updateServiceOrder(order1);

        order1 = serviceOrderService.findServiceOrderById(
                order1.getId()
        );

        System.out.println("Ordem atualizada:");
        System.out.println(order1);

        // DELETE
        System.out.println("Removendo segunda ordem:");

        serviceOrderService.deleteServiceOrderById(
                orderDeleteTest.getId()
        );

        System.out.println("Ordem removida.");

        /*
         * ==========================================================
         * 4. TESTES DE ITENS DA ORDEM
         * ==========================================================
         */

        System.out.println("\n========== ITENS DA ORDEM ==========");

        ServiceItem i1 = new ServiceItem(
                "Jogo de cabo de vela",
                1,
                BigDecimal.valueOf(35),
                order1
        );

        ServiceItem i2 = new ServiceItem(
                "Óleo 5w40 Lubrax",
                4,
                BigDecimal.valueOf(25),
                order1
        );

        ServiceItem i3 = new ServiceItem("Filtro de Óleo",1, BigDecimal.valueOf(15), order1);

        // CREATE
        serviceOrderService.addItemToOrder(order1.getId(), i1);
        serviceOrderService.addItemToOrder(order1.getId(), i2);
        serviceOrderService.addItemToOrder(order1.getId(), i3);

        // READ - todos os itens
        System.out.println("Itens da ordem:");
        i1 = serviceOrderService.findServiceItemById(order1.getId(),1L);

        serviceOrderService.getItemsByOrder(order1.getId()).forEach(System.out::println);

        // READ - item específico
        ServiceItem findItemById = serviceOrderService.findServiceItemById(order1.getId(), i1.getId());

        System.out.println("Item encontrado:");
        System.out.println(findItemById);

        // UPDATE
        findItemById.setUnitValue(BigDecimal.valueOf(30));

        serviceOrderService.updateServiceItem(order1.getId(), findItemById);

        findItemById = serviceOrderService.findServiceItemById(order1.getId(), findItemById.getId());

        System.out.println("Item atualizado:");
        System.out.println(findItemById);

        // DELETE
        i3 = serviceOrderService.findServiceItemById(order1.getId(), 3L);
        ServiceItem itemToDelete = serviceOrderService.findServiceItemById(order1.getId(), i3.getId());

        serviceOrderService.deleteServiceItemById(order1.getId(), itemToDelete.getId());

        System.out.println("Item removido.");

        /*
         * ==========================================================
         * 5. CICLO DE VIDA DA ORDEM
         * ==========================================================
         */

        System.out.println("\n========== CICLO DA ORDEM ==========");

        // START
        serviceOrderService.startServiceOrder(order1.getId());

        order1 = serviceOrderService.findServiceOrderById(order1.getId());

        System.out.println("Ordem iniciada:");
        System.out.println(order1);

        // TOTAL
        System.out.println("Total a pagar: " + order1.getTotalValue());

        // CLOSE
        serviceOrderService.closeServiceOrder(order1.getId());

        order1 = serviceOrderService.findServiceOrderById(order1.getId());

        System.out.println("Ordem fechada:");
        System.out.println(order1);

        // DELIVER
        serviceOrderService.deliverServiceOrder(order1.getId());

        order1 = serviceOrderService.findServiceOrderById(order1.getId());

        System.out.println("Ordem entregue:");
        System.out.println(order1);

        /*
         * ==========================================================
         * 6. CONSULTA FINAL
         * ==========================================================
         */

        System.out.println("\n========== CONSULTA FINAL ==========");

        System.out.println("Todas as ordens:");

        serviceOrderService.findAll().forEach(System.out::println);
    }
}