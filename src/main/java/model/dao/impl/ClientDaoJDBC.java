package model.dao.impl;

import model.dao.ClientDao;
import model.entities.Client;

import java.sql.Connection;
import java.util.List;

public class ClientDaoJDBC implements ClientDao {
    private Connection connection;
    public ClientDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Client findByCpf(String cpf) {
        return null;
    }

    @Override
    public void insert(Client entity) {

    }

    @Override
    public void update(Client entity) {

    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public Client findById(Long aLong) {
        return null;
    }

    @Override
    public List<Client> findAll() {
        return null;
    }
}
