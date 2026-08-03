package application.tests;

import application.Main;
import model.entities.Client;
import model.exception.DbException;
import model.exception.DomainException;
import model.exception.ServiceException;
import model.services.ClientService;

import java.util.Collection;
import java.util.Scanner;

public class TestClient {
    private static final ClientService clientService = new ClientService();
    private static final Scanner scanner = Main.SCANNER;

    private static void register(){
        Client client = new Client();
        System.out.print("Nome: ");
        client.setName(scanner.nextLine());
        System.out.print("CPF: ");
        client.setCpf(scanner.nextLine());
        System.out.print("Telefone: ");
        client.setPhone(scanner.nextLine());
        System.out.print("Endereço: ");
        client.setAddress(scanner.nextLine());
        clientService.registerClient(client);
        System.out.println("Cliente cadastrado com Sucesso.");
    }

    private static Client findByCpf(String cpf){
        return clientService.findClientByCpf(cpf);
    }

    private static void findAll(){
        System.out.println("Todos os clientes cadastrados: ");
        Collection<Client> clients = clientService.getClients();
        clients.forEach(System.out::println);
    }

    private static void update(String cpf){
        Client client = findByCpf(cpf);

        int option = 0;
        while (option != 4) {
            System.out.println("""
                    Qual campo deseja atualizar?
                        1. Nome
                        2. Telefone
                        3. Endereço
                        4. Voltar
                    ==============================
                    """);
            System.out.print("Escolha uma opção: ");
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    System.out.print("Digite o novo Nome:");
                    String name = scanner.nextLine();
                    client.setName(name);
                    clientService.updateClient(client);
                    System.out.println("Nome atualizado.");
                    System.out.println();
                    break;
                case 2:
                    System.out.print("Digite o novo número de telefone: ");
                    String phone = scanner.nextLine();
                    client.setPhone(phone);
                    clientService.updateClient(client);
                    System.out.println("Telefone atualizado.");
                    System.out.println();
                    break;
                case 3:
                    System.out.print("Digite o novo endereço: ");
                    String address = scanner.nextLine();
                    client.setAddress(address);
                    clientService.updateClient(client);
                    System.out.println("Endereço atualizado.");
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

    private static void remove(String cpf){
        Client client = clientService.findClientByCpf(cpf);
        System.out.println("Cliente removido: " + client);
        clientService.removeClient(client.getId());

    }

    public static void client(){
        String cpf;
        int option = 0;
        while (option != 6) {
            System.out.println("""
                    ==== Menu Cliente ====
                        1. Cadastrar
                        2. Buscar por CPF
                        3. Listar todos
                        4. Atualizar
                        5. Excluir
                        6. Voltar
                    ========================
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
                    System.out.print("Digite o CPF: ");
                    cpf = scanner.nextLine();
                    System.out.println(findByCpf(cpf));
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
                    System.out.print("Digite o CPF: ");
                    cpf = scanner.nextLine();
                    update(cpf);
                    System.out.println("Precione enter para continuar.");
                    scanner.nextLine();
                    System.out.println();
                    break;
                case 5:
                    System.out.print("Digite o CPF: ");
                    cpf = scanner.nextLine();
                    remove(cpf);
                    System.out.println("Precione enter para continuar.");
                    scanner.nextLine();
                    System.out.println();
                    break;
                case 6:
                    System.out.println("Voltando para o Menu Principal...");
                    break;
                default:
                    break;
            }
        }

    }
}
