package fi.devolon.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import fi.devolon.demo.model.Company;
import fi.devolon.demo.model.serilizer.AllCompaniesSerializer;
import fi.devolon.demo.model.serilizer.SingleCompanySerializer;
import fi.devolon.demo.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api")
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CompanyController {
    CompanyService companyService;


    @GetMapping("/companies")
    public void getAllCompanies(HttpServletResponse response) throws IOException {
        Iterable<Company> companies= companyService.getAllEntities();
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().registerModule(new SimpleModule().addSerializer(companies.getClass(),new AllCompaniesSerializer())).writeValue(response.getWriter() , companies);
    }

    @GetMapping("/companies/{id}")
    public void getSingleCompany(@PathVariable long id , HttpServletResponse response) throws IOException{
        Company company = companyService.getEntityByID(id);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().registerModule(new SimpleModule().addSerializer(company.getClass(),new SingleCompanySerializer())).writeValue(response.getWriter() , company);
    }
    @DeleteMapping("/companies/{id}")
    public void deleteCompany(@PathVariable long id){
        companyService.deleteEntityByID(id);
    }
    @PutMapping("/companies/{id}")
    public Company updateCompany(@RequestBody Company company,@PathVariable long id){
        return companyService.updateEntity(company , id);
    }
    @PostMapping("/companies/")
    public Company newCompany(@RequestBody Company company){
        return companyService.save(company);
    }

}
