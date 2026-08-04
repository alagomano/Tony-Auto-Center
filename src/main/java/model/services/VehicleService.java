package model.services;

import model.dao.DaoFactory;
import model.dao.ServiceOrderDao;
import model.dao.VehicleDao;
import model.entities.Client;
import model.entities.ServiceOrder;
import model.entities.Vehicle;
import model.enums.OrderStatus;
import model.exception.ServiceException;

import java.util.List;

public class VehicleService {

    private final ClientService clientService = new ClientService();
    private final VehicleDao vehicleDao = DaoFactory.createVehicleDao();
    private final ServiceOrderDao serviceOrderDao = DaoFactory.createServiceOrderDao();

    public VehicleService(){
    }

    private void validateVehicleExists(Vehicle vehicle){
        if(vehicle == null){
            throw new ServiceException("Veículo não encontrado.");
        }
    }

    private void validateVehicle(Vehicle vehicle){
        if(vehicle.getClient() == null){
            throw new ServiceException("Veículo com cliente inválido.");
        }
        if(vehicle.getPlate() == null || vehicle.getPlate().isBlank()){
            throw new ServiceException("Placa do veículo inválida.");
        }
    }

    private void validateID(Long id){
        if (id == null){
            throw new ServiceException("Id inválido.");
        }
    }

    private void validatePlate(String plate){
        if (plate == null || plate.isBlank()){
            throw new ServiceException("Placa inválida.");
        }
    }

    public ServiceOrder openServiceOrder(Long vehicleId, String descriptionProblem, String observations){
        validateID(vehicleId);
        Vehicle vehicle = findVehicleById(vehicleId);

        if (vehicle.hasActiveServiceOrder()){
            throw new ServiceException("Veículo já possui uma ordem de serviço ativa.");
        }

        ServiceOrder order = vehicle.openServiceOrder(descriptionProblem, observations);
        serviceOrderDao.insert(order);
        return order;
    }

    public void registerVehicle(Long clientId, Vehicle vehicle){
        validateID(clientId);
        if(vehicle == null){
            throw new ServiceException("Veículo inválido.");
        }
        Client client = clientService.findClientById(clientId);
        vehicle.setClient(client);
        validateVehicle(vehicle);
        client.addVehicle(vehicle);
        vehicleDao.insert(vehicle);
    }

    public void updateVehicle(Vehicle vehicle){
        validateVehicleExists(vehicle);
        validateID(vehicle.getId());
        validateVehicle(vehicle);
        vehicleDao.update(vehicle);
    }

    public void removeVehicle(Long vehicleId){
        validateID(vehicleId);
        vehicleDao.deleteById(vehicleId);
    }

    public Vehicle findVehicleById(Long vehicleId){
        validateID(vehicleId);
        Vehicle vehicle = vehicleDao.findById(vehicleId);
        validateVehicleExists(vehicle);

        return vehicle;
    }
    public Vehicle findVehicleByPlate(String plate){
        validatePlate(plate);
        Vehicle vehicle = vehicleDao.findByPlate(plate);
        validateVehicleExists(vehicle);

        return vehicle;
    }

    public List<ServiceOrder> getOrders(Long vehicleId){
        validateID(vehicleId);
        findVehicleById(vehicleId);
        return serviceOrderDao.findByVehicle(vehicleId);
    }

    public List<Vehicle> getVehicles(){
        return vehicleDao.findAll();
    }

}
