package fi.devolon.demo.repository;

import fi.devolon.demo.model.Station;
import org.springframework.data.repository.CrudRepository;

public interface StationRepository extends CrudRepository<Station , Long> {
}
