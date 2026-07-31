package model.services;

import model.dao.DaoFactory;
import model.dao.ServiceOrderDao;
import model.dao.VehicleDao;
import model.entities.Client;
import model.entities.ServiceOrder;
import model.entities.Vehicle;
import model.exception.ServiceException;

import java.util.List;

public class VehicleService {

    private final ClientService clientService = new ClientService();
    private final VehicleDao vehicleDao = DaoFactory.createVehicleDao();
    private final ServiceOrderDao serviceOrderDao = DaoFactory.createServiceOrderDao();

    public VehicleService(){
    }

    private void validateVehicle(Vehicle vehicle){
        if(vehicle == null){
            throw new ServiceException("Veículo inválido.");
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

    public void addOrderToVehicle(Long vehicleId, ServiceOrder order){
        if (vehicleId == null){
            throw new ServiceException("Id do veículo inválido.");
        } if (order == null){
            throw new ServiceException("Order inválida.");
        }
        Vehicle vehicle = findVehicleById(vehicleId);

        vehicle.addServiceOrder(order);
        serviceOrderDao.insert(order);
    }

    public void registerVehicle(String cpf, Vehicle vehicle){
        validateVehicle(vehicle);
        Client client = clientService.findClientByCpf(cpf);
        vehicle.setClient(client);
        vehicleDao.insert(vehicle);
    }

    public void updateVehicle(Vehicle vehicle){
        if(vehicle == null){
            throw new ServiceException("Veículo inválido.");
        } if (vehicle.getId() == null){
            throw new ServiceException("ID do veículo inválido.");
        }
        vehicleDao.update(vehicle);
    }

    public void removeVehicle(Vehicle vehicle){
        if(vehicle == null){
            throw new ServiceException("Veículo inválido.");
        } if (vehicle.getId() == null){
            throw new ServiceException("ID do veículo inválido.");
        }
        vehicleDao.deleteById(vehicle.getId());
    }

    public Vehicle findVehicleById(Long vehicleId){
        validateID(vehicleId);
        Vehicle vehicle = vehicleDao.findById(vehicleId);
        validateVehicle(vehicle);

        return vehicle;
    }
    public Vehicle findVehicle(String plate){
        validatePlate(plate);
        Vehicle vehicle = vehicleDao.findByPlate(plate);
        validateVehicle(vehicle);

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
