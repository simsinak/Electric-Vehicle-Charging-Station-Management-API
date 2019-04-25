package fi.devolon.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.devolon.demo.controller.ControllerUtility;
import fi.devolon.demo.model.Company;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UICompanyService {

    private final String getAllCompaniesURI="/api/companies?page={page}&limit={limit}";
    private final String singleCompanyURI="/api/companies/{id}";
    private final String createCompanyURI="/api/companies";
    private final String allChildernURI="/api/companies/{id}/stations";
    private final static String UNKNOWN_ERROR = "An Unknown Error Happened! Please Try Again Later.";


    RestTemplate restTemplate;
    ServletContext servletContext;
    ObjectMapper objectMapper;

    public boolean getAllCompanies(HttpServletRequest request,Model model , RedirectAttributes redirectAttributes , int page , int limit) throws IOException {
        String uri = makeUrl(request,getAllCompaniesURI);
        ResponseEntity<String> response = restTemplate.getForEntity(uri , String.class, page,limit);
        String responseText = response.getBody();
        if (response.getStatusCode()== HttpStatus.OK) {
            Company[] companies=objectMapper.readValue(responseText , Company[].class);
            List<String> links= ControllerUtility.findLinks(response.getHeaders() , request.getRequestURI());
            model.addAttribute("companies", companies);
            model.addAttribute("prev",links.get(0));
            model.addAttribute("next",links.get(1));


            return true;
        }else {
            return generateErrorMessage(redirectAttributes, responseText);
        }

    }

    public boolean getSingleCompany(HttpServletRequest request,Model model , RedirectAttributes redirectAttributes , long id) throws IOException {
        String uri = makeUrl(request,singleCompanyURI);
        ResponseEntity<String> response = restTemplate.getForEntity(uri , String.class, id);
        String responseText = response.getBody();
        if (response.getStatusCode()== HttpStatus.OK) {
            Company company=objectMapper.readValue(responseText , Company.class);
            model.addAttribute("company", company);
            return true;
        }else {
            return generateErrorMessage(redirectAttributes, responseText);
        }

    }
    public void deleteSingleCompany(HttpServletRequest request, RedirectAttributes redirectAttributes , long id) throws IOException {
        String uri = makeUrl(request,singleCompanyURI);
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.DELETE,HttpEntity.EMPTY,String.class,id);
        String responseText = response.getBody();
        if (response.getStatusCode()== HttpStatus.OK) {
            redirectAttributes.addFlashAttribute("error", "The Company Is Deleted If You Do Not Believe It Try to Retrieve It!");
        }else {
            JsonNode node = objectMapper.readTree(responseText).get("message");
            if (node!=null) redirectAttributes.addFlashAttribute("error",node.asText());
            else redirectAttributes.addFlashAttribute("error",UNKNOWN_ERROR);
        }

    }

    public boolean updateSingleCompany(HttpServletRequest request,Model model , RedirectAttributes redirectAttributes , long id , String name , Long parentID) throws IOException {
        String uri = makeUrl(request,singleCompanyURI);
        HttpHeaders headers=new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE);
        Company parentCompany = null;
        if(parentID!=null) {
            parentCompany=new Company();
            parentCompany.setId(parentID);
        }
        Company company=new Company(id,name,parentCompany,null,null);
        HttpEntity<Company> httpEntity=new HttpEntity(company,headers);
        ResponseEntity<String> response = restTemplate.exchange(uri , HttpMethod.PUT,httpEntity,String.class,id);
        String responseText = response.getBody();
        if (response.getStatusCode()== HttpStatus.OK) {
            return getSingleCompany(request,model,redirectAttributes,id);
        }else {
            return generateErrorMessage(redirectAttributes, responseText);
        }

    }

    public boolean createSingleCompany(HttpServletRequest request,Model model , RedirectAttributes redirectAttributes , String name , Long parentID) throws IOException {
        String uri = makeUrl(request,createCompanyURI);
        Company parentCompany = null;
        if(parentID!=null) {
            parentCompany=new Company();
            parentCompany.setId(parentID);
        }
        Company company=new Company(name);
        company.setParentCompany(parentCompany);
        ResponseEntity<String> response=restTemplate.postForEntity(uri,company,String.class);
        String responseText = response.getBody();
        if (response.getStatusCode()== HttpStatus.CREATED) {
            Company cmp=objectMapper.readValue(responseText , Company.class);
            model.addAttribute("company", cmp);
            return true;
        }else {
            return generateErrorMessage(redirectAttributes, responseText);
        }

    }

    public boolean getAllStations(HttpServletRequest request,Model model , RedirectAttributes redirectAttributes , long id) throws IOException {
        String uri = makeUrl(request,allChildernURI);
        ResponseEntity<String> response = restTemplate.getForEntity(uri , String.class, id);
        String responseText = response.getBody();
        if (response.getStatusCode()== HttpStatus.OK) {
            Company company=objectMapper.readValue(responseText , Company.class);
            model.addAttribute("company", company);
            return true;
        }else {
            return generateErrorMessage(redirectAttributes, responseText);
        }

    }

    private boolean generateErrorMessage(RedirectAttributes redirectAttributes, String responseText) throws IOException {
        JsonNode node = objectMapper.readTree(responseText).get("message");
        if (node != null) redirectAttributes.addFlashAttribute("error", node.asText());
        else redirectAttributes.addFlashAttribute("error", UNKNOWN_ERROR);
        return false;
    }
    private String makeUrl(HttpServletRequest request, String uri){
        return request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+uri;
    }
}
