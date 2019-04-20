package fi.devolon.demo.service;



public interface BasicRestService<T> {

    T save(T company);

    Iterable<T> getAllEntities();

    T getEntityByID(long id);

    void deleteEntityByID(long id);

    T updateEntity(T entity, Long id);
}
