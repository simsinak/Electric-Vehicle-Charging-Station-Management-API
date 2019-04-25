package fi.devolon.demo.repository;

import fi.devolon.demo.model.Company;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CompanyRepository extends PagingAndSortingRepository<Company , Long> {
}
