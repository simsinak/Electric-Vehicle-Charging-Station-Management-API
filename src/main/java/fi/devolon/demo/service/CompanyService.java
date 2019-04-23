package fi.devolon.demo.service;

import fi.devolon.demo.exceptions.CompanyNotFoundException;
import fi.devolon.demo.exceptions.MissingValueException;
import fi.devolon.demo.model.Company;
import fi.devolon.demo.repository.CompanyRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CompanyService implements BasicRestService<Company>{

    CompanyRepository companyRepository;


    @Override
    public Company save(Company company) {
        if(company.getParentCompany()!=null){
            Company parentCompany=getEntityByID(company.getParentCompany().getId());
            company.setParentCompany(parentCompany);
            parentCompany.getSubCompanies().add(company);
        }
        return companyRepository.save(company);
    }

    @Override
    public Page<Company> getAllEntities(int page,int limit) {
         return companyRepository.findAll(PageRequest.of(page,limit));
    }

    @Override
    public Company getEntityByID(Long id) {
        if(id==null) throw new MissingValueException("company id must not be null");
        Optional<Company> optionalCompany= companyRepository.findById(id);
        return optionalCompany.orElseThrow(()-> new CompanyNotFoundException(id));
    }

    @Override
    public void deleteEntityByID(Long id) {
        if(companyRepository.existsById(id)){
            companyRepository.deleteById(id);
        }else{
            throw new CompanyNotFoundException(id);
        }
    }

    @Override
    public Company updateEntity(Company company, Long id) {
        return companyRepository.findById(id).map(cmp-> {cmp.setName(company.getName());
                                                Long parentID = null;
                                                if (company.getParentCompany()!=null){
                                                        Company parentCompany = getEntityByID(company.getParentCompany().getId());
                                                        cmp.setParentCompany(parentCompany);
                                                }else{
                                                    cmp.setParentCompany(null);
                                                }
                                                return companyRepository.save(cmp);})
                                                .orElseThrow(()-> new CompanyNotFoundException(id));
    }
}
