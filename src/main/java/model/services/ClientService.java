package model.services;

import model.dao.ClientDao;
import model.dao.DaoFactory;
import model.entities.Client;
import model.exception.ServiceException;

import java.util.Collection;

public class ClientService {

    private final ClientDao clientDao = DaoFactory.createClientDao();

    private void validatedClient(Client client){
        if(client == null){
            throw new ServiceException("Cliente inválido.");
        }
        if(client.getName() == null || client.getName().isBlank()){
            throw new ServiceException("Nome do cliente inválido.");
        }
        if(client.getCpf() == null || client.getCpf().isBlank()){
            throw new ServiceException("CPF do cliente inválido.");
        }
        if(client.getPhone() == null || client.getPhone().isBlank()){
            throw new ServiceException("Telefone do cliente inválido.");
        }
    }

    public void registerClient(Client client){
        validatedClient(client);
        clientDao.insert(client);
    }

    public Client findClient(String cpf){
        if(cpf == null || cpf.isBlank()){
            throw new ServiceException("CPF inválido.");
        }
        Client client = clientDao.findByCpf(cpf);

        if (client == null){
            throw new ServiceException("Cliente não encontrado.");
        }

        return client;
    }

    public void removeClient(Client client){
        if(client == null){
            throw new ServiceException("Cliente inválido.");
        } if (client.getId() == null){
            throw new ServiceException("Cliente com id inválido.");
        }

        clientDao.deleteById(client.getId());

    }

    public Collection<Client> getClients(){
        return clientDao.findAll();
    }

}
