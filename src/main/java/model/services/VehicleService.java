package model.services;

import model.dao.DaoFactory;
import model.dao.ServiceOrderDao;
import model.dao.VehicleDao;
import model.entities.Client;
import model.entities.Vehicle;
import model.exception.ServiceException;

public class VehicleService {

    private final ClientService clientService;
    private final VehicleDao vehicleDao = DaoFactory.createVehicleDao();
    private final ServiceOrderDao serviceOrderDao = DaoFactory.createServiceOrderDao();

    public VehicleService(ClientService clientService){
        this.clientService = clientService;
    }

    private void validateVehicle(Vehicle vehicle){
        if(vehicle == null){
            throw new ServiceException("Veículo inválido.");
        }
    }

    private void validatePlate(String plate){
        if (plate == null || plate.isBlank()){
            throw new ServiceException("Placa inválida.");
        }
    }

    public void registerVehicle(String cpf, Vehicle vehicle){
        validateVehicle(vehicle);
        Client client = clientService.findClient(cpf);
        vehicle.setClient(client);
        vehicleDao.insert(vehicle);
    }

    public Vehicle findVehicle(String plate){
        validatePlate(plate);
        Vehicle vehicle = vehicleDao.findByPlate(plate);
        validateVehicle(vehicle);

        return vehicle;
    }



}
