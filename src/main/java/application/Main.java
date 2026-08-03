package application;

import application.tests.TestClient;
import application.tests.TestServiceOrder;
import application.tests.TestVehicle;
import model.exception.DomainException;

import java.util.Scanner;


public class Main { public static final Scanner SCANNER = new Scanner(System.in);
    public static void main(String[] args) {

        int option = 0;

        while (option != 4) {
            System.out.println("""
                    ==== Menu Principal ====
                        1. Cliente
                        2. Veículo
                        3. Ordem de Serviço
                        4. Sair
                    ========================
                    """);
            System.out.print("Escolha uma opção: ");
            option = SCANNER.nextInt();
            SCANNER.nextLine();
            try {
                switch (option) {
                    case 1:
                        TestClient.client();
                        break;
                    case 2:
                        TestVehicle.vehicle();
                        break;
                    case 3:
                        TestServiceOrder.serviceOrder();
                        break;
                    case 4:
                        System.out.println("Saindo...");
                        break;
                    default:
                        break;
                }
            }catch (DomainException e){
                System.out.println("Erro: " + e.getMessage());
            }
        }

        SCANNER.close();
    }



}