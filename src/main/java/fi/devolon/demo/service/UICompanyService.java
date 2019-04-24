package fi.devolon.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.devolon.demo.controller.ControllerUtility;
import fi.devolon.demo.model.Company;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final String getSingleCompanyURI="/api/companies/{id}";
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
            JsonNode node = objectMapper.readTree(responseText).get("message");
            if (node!=null) redirectAttributes.addFlashAttribute("error",node.asText());
            else redirectAttributes.addFlashAttribute("error",UNKNOWN_ERROR);
            return false;
        }

    }

    public boolean getSingleCompany(HttpServletRequest request,Model model , RedirectAttributes redirectAttributes , int id) throws IOException {
        String uri = makeUrl(request,getSingleCompanyURI);
        ResponseEntity<String> response = restTemplate.getForEntity(uri , String.class, id);
        String responseText = response.getBody();
        if (response.getStatusCode()== HttpStatus.OK) {
            Company company=objectMapper.readValue(responseText , Company.class);
            model.addAttribute("company", company);
            return true;
        }else {
            JsonNode node = objectMapper.readTree(responseText).get("message");
            if (node!=null) redirectAttributes.addFlashAttribute("error",node.asText());
            else redirectAttributes.addFlashAttribute("error",UNKNOWN_ERROR);
            return false;
        }

    }


    private String makeUrl(HttpServletRequest request, String uri){
        return request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+uri;
    }
}
