package model.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import model.dao.VehicleDao;
import model.entities.Vehicle;
import model.exception.DbException;

import java.util.List;

public class VehicleDaoJPA implements VehicleDao {
    private final EntityManager entityManager;
    public VehicleDaoJPA(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public void insert(Vehicle vehicle) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.persist(vehicle);
            transaction.commit();
        }catch (PersistenceException e){
            if(transaction.isActive()){
                transaction.rollback();
            }
            throw new DbException("Erro ao inserir Veículo.", e);
        }
    }

    @Override
    public void update(Vehicle vehicle) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.merge(vehicle);
            transaction.commit();
        }catch (PersistenceException e){
            if(transaction.isActive()){
                transaction.rollback();
            }
            throw new DbException("Erro ao atualizar informações do veículo.", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            Vehicle vehicle = entityManager.find(Vehicle.class, id);
            if (vehicle == null) {
                throw new DbException("Veículo não encontrado");
            }

            transaction.begin();
            entityManager.remove(vehicle);
            transaction.commit();
        }catch (PersistenceException e){
            if(transaction.isActive()){
                transaction.rollback();
            }
            throw new DbException("Erro ao deletar veículo.", e);
        }
    }

    @Override
    public Vehicle findById(Long id) {
        try {
            return entityManager.find(Vehicle.class, id);
        }catch (PersistenceException e){
            throw new DbException("Erro ao buscar veículo.", e);
        }
    }

    @Override
    public List<Vehicle> findAll() {
        try {
            String jpql = """
                    SELECT v FROM Vehicle v
                    """;
            return entityManager.createQuery(jpql, Vehicle.class).getResultList();
        }catch (PersistenceException e){
            throw new DbException("Erro ao listar veículos.", e);
        }
    }

    @Override
    public Vehicle findByPlate(String plate) {
        try {
            String jpql = """
                    SELECT v FROM Vehicle v
                    WHERE v.plate = :plate
                    """;
            return entityManager.createQuery(jpql, Vehicle.class).setParameter("plate", plate).getSingleResult();

        }catch (NoResultException e){
            return null;
        }catch (PersistenceException e){
            throw new DbException("Erro ao buscar veículo por placa.", e);
        }
    }

    @Override
    public List<Vehicle> findByClient(Long clientId) {
        try {
            String jpql = """
                    SELECT v FROM Vehicle v
                    WHERE v.client.id = :id
                    """;
            return entityManager.createQuery(jpql, Vehicle.class).setParameter("id", clientId).getResultList();

        }catch (PersistenceException e){
            throw new DbException("Erro ao buscar veículos do cliente.", e);
        }
    }
}
