package model.dao;

import java.util.List;

public interface Dao <T ,ID>{
    void insert(T entity);
    void update(T entity);
    void deleteById(ID id);
    T findById(ID id);
    List<T> findAll();
}
