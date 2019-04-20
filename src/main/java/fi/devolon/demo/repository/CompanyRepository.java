package fi.devolon.demo.repository;

import fi.devolon.demo.model.Company;
import org.springframework.data.repository.CrudRepository;

public interface CompanyRepository extends CrudRepository<Company , Long> {
}
