package model.services;

import model.dtos.VehicleCreateDTO;
import model.dtos.VehicleUpdateDTO;
import model.entities.Client;
import model.entities.ServiceOrder;
import model.entities.Vehicle;
import model.exception.ResourceNotFoundException;
import model.exception.ServiceException;
import model.repositories.ServiceOrderRepository;
import model.repositories.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    private final ClientService clientService;
    private final VehicleRepository vehicleRepository;
    private final ServiceOrderRepository serviceOrderRepository;

    public VehicleService(ClientService clientService, VehicleRepository vehicleRepository, ServiceOrderRepository serviceOrderRepository){
        this.clientService = clientService;
        this.vehicleRepository = vehicleRepository;
        this.serviceOrderRepository = serviceOrderRepository;
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

    @Transactional
    public ServiceOrder openServiceOrder(Long vehicleId, String descriptionProblem, String observations){
        validateID(vehicleId);
        Vehicle vehicle = findVehicleById(vehicleId);

        if (vehicle.hasActiveServiceOrder()){
            throw new ServiceException("Veículo já possui uma ordem de serviço ativa.");
        }

        ServiceOrder order = vehicle.openServiceOrder(descriptionProblem, observations);
        serviceOrderRepository.save(order);
        return order;
    }
    @Transactional
    public Vehicle registerVehicle(VehicleCreateDTO dto){
        Vehicle vehicle = new Vehicle();
        vehicle.setPlate(dto.getPlate());
        vehicle.setBrand(dto.getBrand());
        vehicle.setModel(dto.getModel());
        vehicle.setYear(dto.getYear());

        Client client = clientService.findClientByCpf(dto.getClientCpf());
        vehicle.setClient(client);

        validateVehicle(vehicle);
        client.addVehicle(vehicle);
        vehicleRepository.save(vehicle);

        return vehicle;
    }
    @Transactional
    public Vehicle updateVehicle(Long vehicleId, VehicleUpdateDTO dto){
        String clientCpf = dto.getClientCpf();
        Client client = clientService.findClientByCpf(clientCpf);

        Vehicle entityVehicle = findVehicleById(vehicleId);
        entityVehicle.setPlate(dto.getPlate());
        entityVehicle.setClient(client);

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
        return vehicle.orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado."));
    }
    @Transactional
    public Vehicle findVehicleByPlate(String plate){
        validatePlate(plate);
        Optional<Vehicle> vehicle = vehicleRepository.findByPlate(plate);
        return vehicle.orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado."));
    }
    @Transactional
    public Optional<ServiceOrder> getOrders(Long vehicleId){
        validateID(vehicleId);
        findVehicleById(vehicleId);
        return serviceOrderRepository.findByVehicleId(vehicleId);
    }
    @Transactional
    public List<Vehicle> getVehicles(){
        return vehicleRepository.findAll();
    }

}
