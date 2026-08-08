package model.services;

import model.dao.ClientDao;
import model.entities.Client;
import model.entities.Vehicle;
import model.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class ClientService {

    private final ClientDao clientDao;
    @Autowired
    public ClientService(ClientDao clientDao) {
        this.clientDao = clientDao;
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
    @Transactional
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
        return client;
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
