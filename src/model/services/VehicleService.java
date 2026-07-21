package model.services;

import model.entities.Client;
import model.entities.ServiceOrder;
import model.entities.Vehicle;
import model.enums.OrderStatus;
import model.exception.ServiceException;

public class VehicleService {

    private final ClientService clientService;

    public VehicleService(ClientService clientService){
        this.clientService = clientService;
    }

    public void registerVehicle(String cpf, Vehicle vehicle){
        Client client = clientService.findClient(cpf);
        client.addVehicle(vehicle);
    }

    public Vehicle findVehicle(String cpf, String plate){
        Client client = clientService.findClient(cpf);
        Vehicle vehicle = client.getVehicle(plate);
        if (vehicle == null){
            throw new ServiceException("Veículo não encontrado.");
        }
        return vehicle;
    }

    public ServiceOrder openServiceOrder(String cpf, String plate, String description, String observation){
        Vehicle vehicle = findVehicle(cpf, plate);

        boolean verificationOS = vehicle.getHistory().stream().anyMatch(os -> os.getStatus() == OrderStatus.OPEN || os.getStatus() == OrderStatus.IN_PROGRESS);

        if (verificationOS){
            throw new ServiceException("Veículo já possui uma ordem de serviço ativa.");
        }

        return vehicle.openServiceOrder(description, observation);

    }

}
