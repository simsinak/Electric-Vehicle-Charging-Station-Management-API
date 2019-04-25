package fi.devolon.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.devolon.demo.controller.ControllerUtility;
import fi.devolon.demo.model.Company;
import fi.devolon.demo.model.Station;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UIStationService {

    private final String getAllStationsURI="/api/stations?page={page}&limit={limit}";
    private final String singleStationURI="/api/stations/{id}";
    private final String createStationURI="/api/stations";
    private final String aroundStationURI="/api/stations/around?latitude={latitude}&longitude={longitude}&distance={distance}";
    private final static String UNKNOWN_ERROR = "An Unknown Error Happened! Please Try Again Later.";


    RestTemplate restTemplate;
    ObjectMapper objectMapper;

    public boolean getAllStations(HttpServletRequest request,Model model , RedirectAttributes redirectAttributes , int page , int limit) throws IOException {
        String uri = makeUrl(request,getAllStationsURI);
        ResponseEntity<String> response = restTemplate.getForEntity(uri , String.class, page,limit);
        String responseText = response.getBody();
        if (response.getStatusCode()== HttpStatus.OK) {
            Station[] companies=objectMapper.readValue(responseText , Station[].class);
            List<String> links= ControllerUtility.findLinks(response.getHeaders() , request.getRequestURI());
            model.addAttribute("stations", companies);
            model.addAttribute("prev",links.get(0));
            model.addAttribute("next",links.get(1));
            return true;
        }else {
            return generateErrorMessage(redirectAttributes, responseText);
        }

    }

    public boolean getSingleStation(HttpServletRequest request,Model model , RedirectAttributes redirectAttributes , long id) throws IOException {
        String uri = makeUrl(request,singleStationURI);
        ResponseEntity<String> response = restTemplate.getForEntity(uri , String.class, id);
        String responseText = response.getBody();
        if (response.getStatusCode()== HttpStatus.OK) {
            Station station=objectMapper.readValue(responseText , Station.class);
            model.addAttribute("stations", new Station[]{station});
            return true;
        }else {
            return generateErrorMessage(redirectAttributes, responseText);
        }

    }
    public void deleteSingleStation(HttpServletRequest request, RedirectAttributes redirectAttributes , long id) throws IOException {
        String uri = makeUrl(request,singleStationURI);
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.DELETE,HttpEntity.EMPTY,String.class,id);
        String responseText = response.getBody();
        if (response.getStatusCode()== HttpStatus.OK) {
            redirectAttributes.addFlashAttribute("error", "The Station Is Deleted If You Do Not Believe It Try to Retrieve It!");
        }else {
            JsonNode node = objectMapper.readTree(responseText).get("message");
            if (node!=null) redirectAttributes.addFlashAttribute("error",node.asText());
            else redirectAttributes.addFlashAttribute("error",UNKNOWN_ERROR);
        }

    }

    public boolean updateSingleStation(HttpServletRequest request,Model model , RedirectAttributes redirectAttributes , long id , String name ,Double latitude, Double longitude ,Long companyID,HttpMethod httpMethod) throws IOException {
        String uri = makeUrl(request,singleStationURI);
        HttpHeaders headers=new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_JSON_VALUE);
        Company company = null;
        if(companyID!=null) {
            company=new Company();
            company.setId(companyID);
        }
        Station station=new Station(id,name.isEmpty()?null:name,latitude,longitude,company,0);
        HttpEntity<Company> httpEntity=new HttpEntity(station,headers);
        ResponseEntity<String> response = restTemplate.exchange(uri , httpMethod,httpEntity,String.class,id);
        String responseText = response.getBody();
        if (response.getStatusCode()== HttpStatus.OK) {
            station=objectMapper.readValue(responseText , Station.class);
            model.addAttribute("stations", new Station[]{station});
            return true;
        }else {
            return generateErrorMessage(redirectAttributes, responseText);
        }

    }

    public boolean createSingleStation(HttpServletRequest request,Model model , RedirectAttributes redirectAttributes , String name ,Double latitude, Double longitude ,Long companyID) throws IOException {
        String uri = makeUrl(request,createStationURI);
        Company company = null;
        if(companyID!=null) {
            company=new Company();
            company.setId(companyID);
        }
        Station station=new Station(null,name,latitude,longitude,company,0);
        ResponseEntity<String> response=restTemplate.postForEntity(uri,station,String.class);
        String responseText = response.getBody();
        if (response.getStatusCode()== HttpStatus.CREATED) {
            station=objectMapper.readValue(responseText , Station.class);
            model.addAttribute("stations", new Station[]{station});
            return true;
        }else {
            return generateErrorMessage(redirectAttributes, responseText);
        }

    }

    public boolean getAllStationsAround(HttpServletRequest request,Model model , RedirectAttributes redirectAttributes , Double latitude, Double longitude,Double distance) throws IOException {
        String uri = makeUrl(request,aroundStationURI);
        ResponseEntity<String> response = restTemplate.getForEntity(uri , String.class,latitude,longitude,distance);
        String responseText = response.getBody();
        if (response.getStatusCode()== HttpStatus.OK) {
            Station[] companies=objectMapper.readValue(responseText , Station[].class);
            model.addAttribute("stations", companies);
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
