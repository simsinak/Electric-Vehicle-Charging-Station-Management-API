package fi.devolon.demo.service;

import fi.devolon.demo.exceptions.CompanyNotFoundException;
import fi.devolon.demo.model.Company;
import fi.devolon.demo.repository.CompanyRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CompanyService implements BasicRestService<Company>{

    CompanyRepository companyRepository;


    @Override
    public Company save(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public Iterable<Company> getAllEntities() {
        return companyRepository.findAll();
    }

    @Override
    public Company getEntityByID(long id) {
        Optional<Company> company= companyRepository.findById(id);
        return company.orElseThrow(()-> new CompanyNotFoundException(id));
    }

    @Override
    public void deleteEntityByID(long id) {
        companyRepository.deleteById(id);
    }

    @Override
    public Company updateEntity(Company company, Long id) {
        return companyRepository.findById(id).map(cmp-> {cmp.setName(company.getName());
                                                Long parentID = null;
                                                if (company.getParentCompany()!=null && (parentID=company.getParentCompany().getId())!=null){
                                                    Optional<Company> parentCompany = companyRepository.findById(parentID);
                                                    if(parentCompany.isPresent()){
                                                        cmp.setParentCompany(parentCompany.get());
                                                    }else{
                                                        throw new CompanyNotFoundException(parentID);
                                                    }
                                                }else{
                                                    cmp.setParentCompany(null);
                                                }
                                                return companyRepository.save(cmp);})
                                                .orElseThrow(()-> new CompanyNotFoundException(id));
    }
}
