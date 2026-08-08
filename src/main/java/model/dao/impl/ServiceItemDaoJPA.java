package model.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import model.dao.ServiceItemDao;
import model.entities.ServiceItem;
import model.exception.DbException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ServiceItemDaoJPA implements ServiceItemDao {
    private final EntityManager entityManager;
    public ServiceItemDaoJPA(EntityManager entityManager){
        this.entityManager = entityManager;
    }
    @Override
    public void insert(ServiceItem serviceItem) {
        try {
            entityManager.persist(serviceItem);
        }catch (PersistenceException e){
            throw new DbException("Erro ao inserir item.", e);
        }
    }

    @Override
    public void update(ServiceItem serviceItem) {
        try {
            entityManager.merge(serviceItem);
        }catch (PersistenceException e){
            throw new DbException("Erro ao atualizar informações do item.", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            ServiceItem serviceItem = entityManager.find(ServiceItem.class, id);
            if (serviceItem == null) {
                throw new DbException("Item não encontrado.");
            }
            entityManager.remove(serviceItem);
        }catch (PersistenceException e){
            throw new DbException("Erro ao deletar item.", e);
        }
    }

    @Override
    public ServiceItem findById(Long id) {
        try {
            return entityManager.find(ServiceItem.class, id);
        }catch (PersistenceException e){
            throw new DbException("Erro ao buscar item.", e);
        }
    }

    @Override
    public List<ServiceItem> findAll() {
        try {
            String jpql = """
                    SELECT si FROM ServiceItem si
                    """;
            return entityManager.createQuery(jpql, ServiceItem.class).getResultList();
        }catch (PersistenceException e){
            throw new DbException("Erro ao listar itens.", e);
        }
    }

    @Override
    public List<ServiceItem> findByServiceOrder(Long serviceOrderId) {
        try {
            String jpql = """
                    SELECT si FROM ServiceItem si
                    WHERE si.serviceOrder.id = :id
                    """;
            return entityManager.createQuery(jpql, ServiceItem.class).setParameter("id", serviceOrderId).getResultList();

        }catch (PersistenceException e){
            throw new DbException("Erro ao buscar itens da ordem de serviço.", e);
        }
    }
}
