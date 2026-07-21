package model.services;

import model.entities.Client;
import model.exception.ServiceException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ClientService {

    private Map<String, Client> clients = new HashMap<>();

    public void registerClient(Client client){
        if(clients.containsKey(client.getCpf())){
            throw new ServiceException("Cliente já existe.");
        }

        clients.put(client.getCpf(), client);
    }

    public Client findClient(String cpf){
        Client client = clients.get(cpf);
        if(client == null){
            throw new ServiceException("Cliente não encontrado");
        }
        return client;
    }

    public void removeClient(Client client){
        if(!clients.containsKey(client.getCpf())){
            throw new ServiceException("Cliente não se encontra da lista.");
        }

        clients.remove(client.getCpf());

    }

    public Collection<Client> getClients(){
        return new ArrayList<>(clients.values());
    }

}
