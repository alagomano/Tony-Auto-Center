package model.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import model.dao.ServiceOrderDao;
import model.entities.ServiceOrder;
import model.exception.DbException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ServiceOrderDaoJPA implements ServiceOrderDao {
    private final EntityManager entityManager;
    public ServiceOrderDaoJPA(EntityManager entityManager){
        this.entityManager = entityManager;
    }
    @Override
    public void insert(ServiceOrder serviceOrder) {
        try {
            entityManager.persist(serviceOrder);
        }catch (PersistenceException e){
            throw new DbException("Erro ao inserir ordem de serviço.", e);
        }
    }

    @Override
    public void update(ServiceOrder serviceOrder) {
        try {
            entityManager.merge(serviceOrder);
        }catch (PersistenceException e){
            throw new DbException("Erro ao atualizar informações da ordem de serviço.", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        try {
            ServiceOrder serviceOrder = entityManager.find(ServiceOrder.class, id);
            if (serviceOrder == null) {
                throw new DbException("Ordem de serviço não encontrada.");
            }
            entityManager.remove(serviceOrder);
        }catch (PersistenceException e){
            throw new DbException("Erro ao deletar ordeem de serviço.", e);
        }
    }

    @Override
    public ServiceOrder findById(Long id) {
        try {
            return entityManager.find(ServiceOrder.class, id);
        }catch (PersistenceException e){
            throw new DbException("Erro ao buscar ordem de serviço.", e);
        }
    }

    @Override
    public List<ServiceOrder> findAll() {
        try {
            String jpql = """
                    SELECT so FROM ServiceOrder so
                    """;
            return entityManager.createQuery(jpql, ServiceOrder.class).getResultList();
        }catch (PersistenceException e){
            throw new DbException("Erro ao listar Ordens de serviço.", e);
        }
    }

    @Override
    public List<ServiceOrder> findByVehicle(Long vehicleId) {
        try {
            String jpql = """
                    SELECT so FROM ServiceOrder so
                    WHERE so.vehicle.id = :id
                    """;
            return entityManager.createQuery(jpql, ServiceOrder.class).setParameter("id", vehicleId).getResultList();

        }catch (PersistenceException e){
            throw new DbException("Erro ao buscar ordens de serviço do veículo.", e);
        }
    }
}
