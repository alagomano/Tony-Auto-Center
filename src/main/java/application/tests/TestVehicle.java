package application.tests;

import application.Main;
import model.entities.Client;
import model.entities.ServiceOrder;
import model.entities.Vehicle;
import model.exception.ServiceException;
import model.services.ClientService;
import model.services.VehicleService;

import java.util.Collection;
import java.util.Scanner;

public class TestVehicle {
    private static final Scanner scanner = Main.SCANNER;
    private static final VehicleService vehicleService = new VehicleService();
    private static final ClientService clientService = new ClientService();

    private static void register(){
        Vehicle vehicle = new Vehicle();
        System.out.print("Placa: ");
        vehicle.setPlate(scanner.nextLine());
        System.out.print("Marca (Fiat, Renault,...): ");
        vehicle.setBrand(scanner.nextLine());
        System.out.print("Modelo: ");
        vehicle.setModel(scanner.nextLine());
        System.out.print("Ano do Carro: ");
        vehicle.setYear(scanner.nextInt());
        scanner.nextLine();

        System.out.println();
        System.out.print("Digite CPF do Dono do carro (Deve ser um cliente cadastrado): ");
        String cpf = scanner.nextLine();

        Client client = clientService.findClientByCpf(cpf);
        vehicleService.registerVehicle(client.getId(), vehicle);
        System.out.println("Veículo cadastrado com Sucesso.");

    }

    private static void update(String plate){
        Vehicle vehicle = findByPlate(plate);
        int option = 0;
        while (option != 4) {
            System.out.println("""
                    Qual campo deseja atualizar?
                        1. Marca
                        2. Modelo
                        3. Ano do Carro
                        4. Voltar
                    ==============================
                    """);
            System.out.print("Escolha uma opção: ");
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    System.out.print("Digite o nova Marca:");
                    String brand = scanner.nextLine();
                    vehicle.setBrand(brand);
                    vehicleService.updateVehicle(vehicle);
                    System.out.println("Marca atualizado.");
                    System.out.println();
                    break;
                case 2:
                    System.out.print("Digite o novo Modelo: ");
                    String model = scanner.nextLine();
                    vehicle.setModel(model);
                    vehicleService.updateVehicle(vehicle);
                    System.out.println("Modelo atualizado.");
                    System.out.println();
                    break;
                case 3:
                    System.out.print("Digite o novo Ano do Carro: ");
                    int year = scanner.nextInt();
                    scanner.nextLine();
                    vehicle.setYear(year);
                    vehicleService.updateVehicle(vehicle);
                    System.out.println("Ano do Carro atualizado.");
                    System.out.println();
                    break;
                case 4:
                    System.out.println("Voltando...");
                    System.out.println();
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private static void remove(String plate){
        Vehicle vehicle = findByPlate(plate);
        vehicleService.removeVehicle(vehicle.getId());
        System.out.println("Veículo remoivo com sucesso.");
        System.out.println(vehicle);
    }
    private static Vehicle findByPlate(String plate){
        return vehicleService.findVehicleByPlate(plate);
    }

    private static void findAll(){
        System.out.println("Todos os veículos cadastrados: ");
        Collection<Vehicle> vehicles = vehicleService.getVehicles();
        vehicles.forEach(System.out::println);
    }

    public static void listOrders(String plate){
        Vehicle vehicle = findByPlate(plate);
        Collection<ServiceOrder> orders = vehicleService.getOrders(vehicle.getId());
        System.out.println("Lista de Ordens de Serviço. Placa do Veículo: " + vehicle.getPlate());
        orders.forEach(System.out::println);
    }

    public static void vehicle(){
        String plate;
        int option = 0;
        while (option != 7) {
            System.out.println("""
                    =========== Menu Veículo ============
                            1. Cadastrar
                            2. Buscar por Placa
                            3. Listar todos
                            4. Atualizar
                            5. Excluir
                            6. Listar ordens de Serviço
                            7. Voltar
                    =====================================
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
                    System.out.println(findByPlate(plate));
                    System.out.println("Precione enter para continuar.");
                    scanner.nextLine();
                    System.out.println();
                    break;
                case 3:
                    findAll();
                    System.out.println("Precione enter para continuar.");
                    scanner.nextLine();
                    System.out.println();
                    break;
                case 4:
                    System.out.print("Digite a placa: ");
                    plate = scanner.nextLine();
                    update(plate);
                    System.out.println("Precione enter para continuar.");
                    scanner.nextLine();
                    System.out.println();
                    break;
                case 5:
                    System.out.print("Digite a placa: ");
                    plate = scanner.nextLine();
                    remove(plate);
                    System.out.println("Precione enter para continuar.");
                    scanner.nextLine();
                    System.out.println();
                    break;
                case 6:
                    System.out.println("Digite a placa: ");
                    plate = scanner.nextLine();
                    listOrders(plate);
                    System.out.println("Precione enter para continuar.");
                    scanner.nextLine();
                    break;
                case 7:
                    System.out.println("Voltando para o Menu Principal...");
                    break;
                default:
                    break;
            }
        }

    }

}
