package fi.devolon.demo.service;



public interface BasicRestService<T> {

    T save(T entity);

    Iterable<T> getAllEntities();

    T getEntityByID(Long id);

    void deleteEntityByID(Long id);

    T updateEntity(T entity, Long id);
}
