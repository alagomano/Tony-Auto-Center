package model.dao;

import model.entities.Client;

public interface ClientDao extends Dao<Client, Long> {
    Client findByCpf(String cpf);
}
