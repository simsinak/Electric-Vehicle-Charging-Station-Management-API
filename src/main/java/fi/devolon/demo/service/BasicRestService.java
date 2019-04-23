package fi.devolon.demo.service;


import org.springframework.data.domain.Page;

public interface BasicRestService<T> {

    T save(T entity);

    Page<T> getAllEntities(int page, int limit);

    T getEntityByID(Long id);

    void deleteEntityByID(Long id);

    T updateEntity(T entity, Long id);
}
