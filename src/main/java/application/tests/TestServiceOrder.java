package application.tests;

import application.Main;
import model.entities.ServiceItem;
import model.entities.ServiceOrder;
import model.entities.Vehicle;
import model.exception.ServiceException;
import model.services.ServiceOrderService;
import model.services.VehicleService;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Scanner;

public class TestServiceOrder {
    private static final Scanner scanner = Main.SCANNER;
    private static final VehicleService vehicleService = new VehicleService();
    private static final ServiceOrderService serviceOrderService = new ServiceOrderService();

    private static void status(Long serviceOrderId, int option){
        switch (option) {
            case 10:
                serviceOrderService.startServiceOrder(serviceOrderId);
                System.out.println("Pronto, Pressione Enter.");
                System.out.println();
                break;
            case 11:
                serviceOrderService.closeServiceOrder(serviceOrderId);
                System.out.println("Pronto, Pressione Enter.");
                System.out.println();
                break;
            case 12:
                serviceOrderService.deliverServiceOrder(serviceOrderId);
                System.out.println("Pronto, Pressione Enter.");
                System.out.println();
                break;
            default:
                System.out.println("Opção inválida.");
                break;
        }

    }

    private static void updateStatus(String plate, int option){
        Vehicle vehicle = vehicleService.findVehicle(plate);

        System.out.println("Lista de ordens do veículo de placa: " + vehicle.getPlate());
        vehicleService.getOrders(vehicle.getId()).forEach(System.out::println);

        System.out.println();
        System.out.print("Em qual ordem de serviço deseja alterar o status (informe o id da ordem): ");
        ServiceOrder order = serviceOrderService.findServiceOrderById(scanner.nextLong());
        scanner.nextLine();

        status(order.getId(), option);
        order = serviceOrderService.findServiceOrderById(order.getId());
        System.out.println("ordem atualizada.");
        System.out.println(order);

    }

    private static void register(){
        try {
            ServiceOrder order = new ServiceOrder();
            System.out.print("Descrição do problema: ");
            order.setProblemDescription(scanner.nextLine());
            System.out.print("Observação: ");
            order.setObservations(scanner.nextLine());

            System.out.println();

            System.out.print("Placa do Veículo (Veículo tem que está cadastrado): ");
            String plate = scanner.nextLine();

            Vehicle vehicle = vehicleService.findVehicle(plate);

            order = serviceOrderService.createServiceOrder(vehicle, order);
            System.out.println("Ordem se serviço cadastrada com Sucesso.");
            System.out.println(order);
        }catch (ServiceException e){
            throw new ServiceException("Não foi possível cadastrar ordem de serviço.");
        }
    }

    private static void update(Long orderId){
        ServiceOrder order = serviceOrderService.findServiceOrderById(orderId);
        int option = 0;
        while (option != 3) {
            System.out.println("""
                    Qual campo deseja atualizar?
                        1. Descrição do Problema
                        2. Observações
                        3. Voltar
                    ==============================
                    """);
            System.out.print("Escolha uma opção: ");
            option = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (option) {
                    case 1:
                        System.out.print("Digite a nova Decrição do Problema:");
                        order.setProblemDescription(scanner.nextLine());
                        serviceOrderService.updateServiceOrder(order);
                        System.out.println("Descrição do problema atualizada.");
                        System.out.println();
                        break;
                    case 2:
                        System.out.print("Digite a nova Observação: ");
                        order.setObservations(scanner.nextLine());
                        serviceOrderService.updateServiceOrder(order);
                        System.out.println("Observações atualizadas.");
                        System.out.println();
                        break;
                    case 3:
                        System.out.println("Voltando...");
                        System.out.println();
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            }catch (ServiceException e){
                throw new ServiceException("Não foi possível atualizar a ordem de serviço.");
            }
        }
    }

    private static void remove(Long serviceOrderId){
        try {
            ServiceOrder order = serviceOrderService.findServiceOrderById(serviceOrderId);
            serviceOrderService.deleteServiceOrderById(serviceOrderId);
            System.out.println("Removido com sucesso.");
            System.out.println(order);
        }catch (ServiceException e){
            throw new ServiceException("Não foi possível remover ordem de serviço.");
        }
    }

    private static void findOrdersByPlate(String plate){
        Vehicle vehicle = vehicleService.findVehicle(plate);
        Collection<ServiceOrder> orders = vehicleService.getOrders(vehicle.getId());
        System.out.println("Lista de orderns de Serviço na ordem da data de entrada: ");
        orders.forEach(System.out::println);
    }

    private static void listOrders(){
        Collection<ServiceOrder> orders = serviceOrderService.findAll();
        orders.forEach(System.out::println);
    }

    private static void addItem(String plate){
        try {
            Vehicle vehicle = vehicleService.findVehicle(plate);

            System.out.println("Lista de ordens do veículo de placa: " + vehicle.getPlate());
            vehicleService.getOrders(vehicle.getId()).forEach(System.out::println);

            System.out.println();
            System.out.print("Em qual ordem de serviço deseja adicionar item(informe o id da ordem): ");
            ServiceOrder order = serviceOrderService.findServiceOrderById(scanner.nextLong());
            scanner.nextLine();

            ServiceItem item = new ServiceItem();
            System.out.print("Descrição do item (Ex:Óleo 5w30 Lubrax): ");
            item.setDescription(scanner.nextLine());
            System.out.print("Quantidade do item: ");
            item.setQuantity(scanner.nextInt());
            scanner.nextLine();
            System.out.print("Valor unitário do item: ");
            item.setUnitValue(BigDecimal.valueOf(scanner.nextDouble()));
            scanner.nextLine();

            serviceOrderService.addItemToOrder(order.getId(), item);

            order = serviceOrderService.findServiceOrderById(order.getId());
            System.out.println("Lista de itens da ordem de serviço " + order.getProblemDescription());
            order.getItems().forEach(System.out::println);
            System.out.println("Total a pagar: " + order.getTotalValue());
            System.out.println();
        }catch (ServiceException e){
            throw new ServiceException("Não foi possível adicionar item à ordem.");
        }
    }

    private static void updateItem(String plate){
        try {
            Vehicle vehicle = vehicleService.findVehicle(plate);

            System.out.println("Lista de ordens do veículo de placa: " + vehicle.getPlate());
            vehicleService.getOrders(vehicle.getId()).forEach(System.out::println);

            System.out.println();
            System.out.print("Em qual ordem de serviço deseja atualizar o item(informe o id da ordem): ");
            ServiceOrder order = serviceOrderService.findServiceOrderById(scanner.nextLong());

            System.out.println();

            System.out.println("Lista de itens da ordem de id: " + order.getId());
            System.out.println("===== " + order.getProblemDescription() + " =====");
            serviceOrderService.getItemsByOrder(order.getId()).forEach(System.out::println);

            System.out.println();
            System.out.print("Digite o id do item ao qual deseja atualizar: ");
            Long itemId = scanner.nextLong();
            ServiceItem item = serviceOrderService.findServiceItemById(order.getId(), itemId);
            int option = 0;
            while (option != 4) {
                System.out.println("""
                        Qual campo deseja atualizar?
                            1. Descrição do item
                            2. Quantidade
                            3. Valor unitário
                            4. Voltar
                        ==============================
                        """);
                System.out.print("Escolha uma opção: ");
                option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1:
                        System.out.print("Descrição do item (Ex:Óleo 5w30 Lubrax): ");
                        item.setDescription(scanner.nextLine());
                        serviceOrderService.updateServiceItem(order.getId(), item);
                        System.out.println("Descrição do problema atualizada.");
                        System.out.println();
                        break;
                    case 2:
                        System.out.print("Quantidade do item: ");
                        item.setQuantity(scanner.nextInt());
                        scanner.nextLine();
                        serviceOrderService.updateServiceItem(order.getId(), item);
                        System.out.println("Quatidade atualizada.");
                        System.out.println();
                        break;
                    case 3:
                        System.out.print("Valor unitário do item: ");
                        item.setUnitValue(BigDecimal.valueOf(scanner.nextDouble()));
                        serviceOrderService.updateServiceItem(order.getId(), item);
                        System.out.println("Valor unitário atualizado...");
                        System.out.println();
                        break;
                    case 4:
                        System.out.println("Voltando...");
                        break;
                    default:
                        System.out.println("Opção inválida.");
                }
            }

            System.out.println("Nova Lista de itens da ordem de serviço " + order.getProblemDescription());
            order = serviceOrderService.findServiceOrderById(order.getId());
            order.getItems().forEach(System.out::println);
            System.out.println();
        }catch (ServiceException e){
            throw new ServiceException("Não foi possível atualizar item.");
        }
    }

    private static void itemsByServiceOrder(String plate){
        Vehicle vehicle = vehicleService.findVehicle(plate);

        System.out.println("Lista de ordens do veículo de placa: " + vehicle.getPlate());
        vehicleService.getOrders(vehicle.getId()).forEach(System.out::println);

        System.out.println();
        System.out.print("Digite em qual ordem de serviço deseja visualizar os itens (informe o id da ordem): ");
        ServiceOrder order = serviceOrderService.findServiceOrderById(scanner.nextLong());
        scanner.nextLine();

        System.out.println();

        System.out.println("Lista de itens da ordem de id: " + order.getId());
        System.out.println("===== " + order.getProblemDescription() + " =====");
        serviceOrderService.getItemsByOrder(order.getId()).forEach(System.out::println);
    }

    private static void removeItem(String plate){
        Vehicle vehicle = vehicleService.findVehicle(plate);

        System.out.println("Lista de ordens do veículo de placa: " + vehicle.getPlate());
        vehicleService.getOrders(vehicle.getId()).forEach(System.out::println);

        System.out.println();
        System.out.print("Em qual ordem de serviço deseja remover o item(informe o id da ordem): ");
        ServiceOrder order = serviceOrderService.findServiceOrderById(scanner.nextLong());
        scanner.nextLine();

        System.out.println();

        System.out.println("Lista de itens da ordem de id: " + order.getId());
        System.out.println("===== " + order.getProblemDescription() + " =====");
        serviceOrderService.getItemsByOrder(order.getId()).forEach(System.out::println);

        System.out.println();
        System.out.print("Digite o id do item ao qual deseja remover: ");
        Long itemId = scanner.nextLong();
        ServiceItem item = serviceOrderService.findServiceItemById(order.getId(), itemId);

        serviceOrderService.deleteServiceItemById(order.getId(), item.getId());
        System.out.println("Item removido: " + item);
    }

    public static void serviceOrder(){
        String plate;
        int option = 0;
        while (option != 13) {
            System.out.println("""
                    =========== Menu de Ordens de Serviço ============
                                     1. Criar ordem
                                     2. Buscar ordens por placa
                                     3. Listar todas as ordens
                                     4. Atualizar ordem por ID
                                     5. Excluir ordem por ID
                                     
                                     6. Adicionar item
                                     7. Atualizar item
                                     8. Excluir item
                                     9. Listar itens por
                                     ordem de serviço
                                     
                                     
                                     10. Iniciar ordem
                                     11. Fechar ordem
                                     12. Entregar ordem
                                     
                                     13. Voltar
                    ===================================================
                    """);
            System.out.print("Escolha uma opção: ");
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option){
                case 1:
                    register();
                    System.out.println("Precione enter para continuar.");
                    scanner.nextLine();
                    System.out.println();
                    break;
                case 2:
                    System.out.print("Digite a Placa do automóvel: ");
                    plate = scanner.nextLine();
                    findOrdersByPlate(plate);
                    System.out.println("Precione enter para continuar.");
                    scanner.nextLine();
                    System.out.println();
                    break;
                case 3:
                    System.out.println("Todas as ordens de serviço: ");
                    listOrders();
                    System.out.println();
                    System.out.println("Precione enter para continuar.");
                    scanner.nextLine();
                    System.out.println();
                    break;
                case 4:
                    System.out.print("Digite o id: ");
                    Long id = scanner.nextLong();
                    update(id);
                    System.out.println("Precione enter para continuar.");
                    scanner.nextLine();
                    System.out.println();
                    break;
                case 5:
                    System.out.print("Digite o id: ");
                    id = scanner.nextLong();
                    remove(id);
                    System.out.println("Precione enter para continuar.");
                    scanner.nextLine();
                    System.out.println();
                    break;
                case 6:
                    System.out.print("Digite a Placa do automóvel: ");
                    plate = scanner.nextLine();
                    addItem(plate);
                    System.out.println("Precione enter para continuar.");
                    scanner.nextLine();
                    break;
                case 7:
                    System.out.print("Digite a Placa do automóvel: ");
                    plate = scanner.nextLine();
                    updateItem(plate);
                    System.out.println("Precione enter para continuar.");
                    scanner.nextLine();
                    break;
                case 8:
                    System.out.print("Digite a Placa do automóvel: ");
                    plate = scanner.nextLine();
                    removeItem(plate);
                    System.out.println("Precione enter para continuar.");
                    scanner.nextLine();
                    break;
                case 9:
                    System.out.print("Digite a Placa do automóvel: ");
                    plate = scanner.nextLine();
                    itemsByServiceOrder(plate);
                    System.out.println("Precione enter para continuar.");
                    scanner.nextLine();
                    break;
                case 10:
                    System.out.print("Digite a Placa do automóvel: ");
                    plate = scanner.nextLine();
                    updateStatus(plate, option);
                    System.out.println("Precione enter para continuar.");
                    scanner.nextLine();
                    break;
                case 11:
                    System.out.print("Digite a Placa do automóvel: ");
                    plate = scanner.nextLine();
                    updateStatus(plate, option);
                    System.out.println("Precione enter para continuar.");
                    scanner.nextLine();
                    break;
                case 12:
                    System.out.print("Digite a Placa do automóvel: ");
                    plate = scanner.nextLine();
                    updateStatus(plate, option);
                    System.out.println("Precione enter para continuar.");
                    scanner.nextLine();
                    break;
                case 13:
                    System.out.println("Voltando para o Menu Principal...");
                    break;
                default:
                    System.out.println("Opção inválida.");
                    break;
            }
        }

    }

}
