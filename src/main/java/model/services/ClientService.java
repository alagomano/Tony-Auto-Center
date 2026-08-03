package model.services;

import model.dao.ClientDao;
import model.dao.DaoFactory;
import model.dao.VehicleDao;
import model.entities.Client;
import model.entities.Vehicle;
import model.exception.ServiceException;

import java.util.Collection;
import java.util.List;

public class ClientService {

    private final ClientDao clientDao = DaoFactory.createClientDao();
    private final VehicleDao vehicleDao = DaoFactory.createVehicleDao();

    private void validateClient(Client client){
        if(client == null){
            throw new ServiceException("Cliente inválido.");
        }
        if(client.getName() == null || client.getName().isBlank()){
            throw new ServiceException("Nome do cliente inválido.");
        }
        if(client.getCpf() == null || client.getCpf().isBlank()){
            throw new ServiceException("CPF do cliente inválido.");
        }
    }

    private void validateClientExists(Client client){
        if(client == null){
            throw new ServiceException("Cliente não encontrado.");
        }
    }

    private void validateCPF(String cpf){
        if(cpf == null || cpf.isBlank()){
            throw new ServiceException("CPF inválido.");
        }
    }

    private void validateID(Long id){
        if (id == null){
            throw new ServiceException("ID inválido.");
        }
    }

    private void validateVehicleExists(Vehicle vehicle){
        if (vehicle == null){
            throw new ServiceException("Veículo não encontrado.");
        }
    }

    private void validatePlate(String plate){
        if(plate == null || plate.isBlank()){
            throw new ServiceException("Placa inválida.");
        }
    }

    public void addVehicleToClient(Long clientId, Vehicle vehicle){
        validateID(clientId);
        validateVehicleExists(vehicle);
        Client client = findClientById(clientId);

        client.addVehicle(vehicle);
        vehicleDao.insert(vehicle);
    }

    public void registerClient(Client client){
        validateClient(client);
        clientDao.insert(client);
    }

    public void updateClient(Client client){
        validateClientExists(client);
        validateID(client.getId());
        clientDao.update(client);
    }

    public void removeClient(Long clientId){
        validateID(clientId);
        clientDao.deleteById(clientId);
    }

    public Client findClientByCpf(String cpf){
        validateCPF(cpf);
        Client client = clientDao.findByCpf(cpf);
        validateClientExists(client);
        return findClientById(client.getId());
    }
    public Client findClientById(Long clientId){
        validateID(clientId);
        Client client = clientDao.findById(clientId);
        validateClientExists(client);

        List<Vehicle> vehicles = vehicleDao.findByClient(clientId);
        vehicles.forEach(client::addVehicle);

        return client;
    }

    public void removeVehicle(Long clientId, String plate){
        validatePlate(plate);
        Client client = findClientById(clientId);
        Vehicle vehicle = vehicleDao.findByPlate(plate);
        validateVehicleExists(vehicle);

        client.removeVehicle(vehicle.getPlate());
        vehicleDao.deleteById(vehicle.getId());

    }

    public Collection<Vehicle> getVehiclesByClient(Long clientId){
        validateID(clientId);
        Client client = findClientById(clientId);

        return client.getVehicles();
    }

    public List<Client> getClients(){
        return clientDao.findAll();
    }

}
