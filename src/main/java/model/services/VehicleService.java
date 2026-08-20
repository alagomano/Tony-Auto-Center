package model.services;

import model.dao.ServiceOrderDao;
import model.entities.Client;
import model.entities.ServiceOrder;
import model.entities.Vehicle;
import model.exception.ServiceException;
import model.repositories.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    private final ClientService clientService;
    private final VehicleRepository vehicleRepository;
    private final ServiceOrderDao serviceOrderDao;

    public VehicleService(ClientService clientService, VehicleRepository vehicleRepository, ServiceOrderDao serviceOrderDao){
        this.clientService = clientService;
        this.vehicleRepository = vehicleRepository;
        this.serviceOrderDao = serviceOrderDao;
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

    private void updateData(Vehicle vehicleBefore, Vehicle vehicleAfter){
        vehicleBefore.setPlate(vehicleAfter.getPlate());
        vehicleBefore.setClient(vehicleAfter.getClient());
    }

    @Transactional
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
    @Transactional
    public Vehicle registerVehicle(Long clientId, Vehicle vehicle){
        validateID(clientId);
        if(vehicle == null){
            throw new ServiceException("Veículo inválido.");
        }
        Client client = clientService.findClientById(clientId);
        validatePlate(vehicle.getPlate());
        client.addVehicle(vehicle);
        vehicleRepository.save(vehicle);

        return vehicle;
    }
    @Transactional
    public Vehicle updateVehicle(Long vehicleId, Vehicle vehicle){
        validateID(vehicleId);
        validateVehicleExists(vehicle);
        validateVehicle(vehicle);
        Vehicle entityVehicle = findVehicleById(vehicleId);
        updateData(entityVehicle, vehicle);
        vehicleRepository.save(entityVehicle);
        return entityVehicle;
    }
    @Transactional
    public void removeVehicle(Long vehicleId){
        validateID(vehicleId);
        vehicleRepository.deleteById(vehicleId);
    }
    @Transactional
    public Vehicle findVehicleById(Long vehicleId){
        validateID(vehicleId);
        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);
        return vehicle.orElseThrow(() -> new ServiceException("Veículo não encontrado."));
    }
    @Transactional
    public Vehicle findVehicleByPlate(String plate){
        validatePlate(plate);
        Optional<Vehicle> vehicle = vehicleRepository.findByPlate(plate);
        return vehicle.orElseThrow(() -> new ServiceException("Veículo não encontrado."));
    }
    @Transactional
    public List<ServiceOrder> getOrders(Long vehicleId){
        validateID(vehicleId);
        findVehicleById(vehicleId);
        return serviceOrderDao.findByVehicle(vehicleId);
    }
    @Transactional
    public List<Vehicle> getVehicles(){
        return vehicleRepository.findAll();
    }

}
