package model.dao.impl;

import jakarta.persistence.*;
import model.dao.ClientDao;
import model.entities.Client;
import model.exception.DbException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ClientDaoJPA implements ClientDao {
    @PersistenceContext
    private final EntityManager entityManager;
    public ClientDaoJPA(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Client findByCpf(String cpf) {
        try {
            String jpql = """
                    SELECT client FROM Client client
                    WHERE client.cpf = :cpf
                    """;
            return entityManager.createQuery(jpql, Client.class).setParameter("cpf", cpf).getSingleResult();

        }catch (NoResultException e){
            return null;
        }
        catch (PersistenceException e){
            throw new DbException("Erro ao buscar cliente por cpf.", e);
        }
    }

    @Override
    public void insert(Client client) {
        try {
            entityManager.persist(client);
        }catch (PersistenceException e){
            throw new DbException("Erro ao inserir cliente.", e);
        }
    }

    @Override
    public void update(Client client) {
        try {
            entityManager.merge(client);
        }catch (PersistenceException e){
            throw new DbException("Erro ao atualizar dados do cliente.", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            Client client = entityManager.find(Client.class, id);
            if (client == null) {
                throw new DbException("Cliente não encontrado");
            }
            entityManager.remove(client);
        }catch (PersistenceException e){
            throw new DbException("Erro ao deletar cliente.", e);
        }
    }

    @Override
    public Client findById(Long id) {
        try {
            return entityManager.find(Client.class, id);
        }catch (PersistenceException e){
            throw new DbException("Erro ao buscar cliente.", e);
        }
    }

    @Override
    public List<Client> findAll() {
        try {
            String jpql = """
                    SELECT client FROM Client client
                    """;
            return entityManager.createQuery(jpql, Client.class).getResultList();
        }catch (PersistenceException e){
            throw new DbException("Erro ao listar clientes.", e);
        }
    }
}
