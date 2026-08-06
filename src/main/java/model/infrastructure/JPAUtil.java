package model.infrastructure;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
    private static final EntityManagerFactory emf =  Persistence.createEntityManagerFactory("tony-auto-center");
    public static EntityManager getEntityManager(){
        return emf.createEntityManager();
    }

    public void closeFactory(){
        if(emf.isOpen()) {
            emf.close();
        }
    }

}
