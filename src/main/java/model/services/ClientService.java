package model.services;

import model.entities.Client;
import model.entities.Vehicle;
import model.exception.ServiceException;
import model.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

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

    private void updateData(Client clientBefore, Client clientAfter){
        clientBefore.setName(clientAfter.getName());
        clientBefore.setPhone(clientAfter.getPhone());
        clientBefore.setAddress(clientAfter.getAddress());
    }
    @Transactional
    public Client registerClient(Client client){
        validateClient(client);
        return clientRepository.save(client);
    }
    @Transactional
    public Client updateClient(Long clientId, Client client){
        Client entityClient = findClientById(clientId);
        updateData(entityClient, client);
        return clientRepository.save(entityClient);
    }
    @Transactional
    public void removeClient(Long clientId){
        validateID(clientId);
        if(!clientRepository.existsById(clientId)){
            throw new ServiceException("Cliente não encontrado.");
        }
        clientRepository.deleteById(clientId);
    }
    @Transactional
    public Client findClientByCpf(String cpf){
        validateCPF(cpf);
        Optional<Client> client = clientRepository.findByCpf(cpf);
        return client.orElseThrow(() -> new ServiceException("Cliente não encontrado"));
    }
    @Transactional
    public Client findClientById(Long clientId){
        validateID(clientId);
        Optional<Client> client = clientRepository.findById(clientId);
        return client.orElseThrow(() -> new ServiceException("Cliente não encontrado"));
    }
    @Transactional
    public Collection<Vehicle> getVehiclesByClient(Long clientId){
        validateID(clientId);
        Client client = findClientById(clientId);

        return client.getVehicles();
    }
    @Transactional
    public List<Client> getClients(){
        return clientRepository.findAll();
    }

}
