package fi.devolon.demo.controller;


import fi.devolon.demo.model.Company;
import fi.devolon.demo.service.UICompanyService;
import fi.devolon.demo.service.UIStationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UIController {


    UICompanyService companyService;
    UIStationService stationService;


    @GetMapping("/")
    public String home(Model model){
        return "index";
    }


    @PostMapping(value = "/all-companies")
    public String allCompanies(HttpServletRequest request, @RequestParam(name = "page",defaultValue = "0") int page, @RequestParam(name = "limit",defaultValue = "30") int limit, RedirectAttributes redirectAttributes,Model model) throws IOException {
        boolean result=companyService.getAllCompanies(request,model,redirectAttributes,page,limit);
        if (result) {
            return "all-companies";
        }else {
            return "redirect:/";
        }
    }
    @PostMapping(value = "/single-company")
    public String singleCompany(HttpServletRequest request, RedirectAttributes redirectAttributes,Model model , @RequestParam(name="id" , defaultValue = "0") long id) throws IOException {
        boolean result=companyService.getSingleCompany(request,model,redirectAttributes,id);
        if (result) {
            return "single-company";
        }else {
            return "redirect:/";
        }
    }
    @PostMapping(value = "/delete-company")
    public String deleteCompany(HttpServletRequest request, RedirectAttributes redirectAttributes , @RequestParam(name="id" , defaultValue = "0") long id) throws IOException {
        companyService.deleteSingleCompany(request,redirectAttributes,id);
        return "redirect:/";
    }
    @PostMapping(value = "/update-company")
    public String updateCompany(HttpServletRequest request,Model model, RedirectAttributes redirectAttributes , @RequestParam(name="id" , defaultValue = "0") long id, @RequestParam(name="name") String name, @RequestParam(name="parentid" , required = false) Long parentID) throws IOException {
        boolean result=companyService.updateSingleCompany(request,model,redirectAttributes,id,name,parentID);
        if (result) {
            return "single-company";
        }else {
            return "redirect:/";
        }
    }
    @PostMapping(value = "/create-company")
    public String createCompany(HttpServletRequest request,Model model, RedirectAttributes redirectAttributes, @RequestParam(name="name") String name, @RequestParam(name="parentid" , required = false) Long parentID) throws IOException {
        boolean result=companyService.createSingleCompany(request,model,redirectAttributes,name,parentID);
        if (result) {
            return "single-company";
        }else {
            return "redirect:/";
        }
    }



    @PostMapping(value = "/all-stations")
    public String allStations(HttpServletRequest request, @RequestParam(name = "page",defaultValue = "0") int page, @RequestParam(name = "limit",defaultValue = "30") int limit, RedirectAttributes redirectAttributes,Model model) throws IOException {
        boolean result=stationService.getAllStations(request,model,redirectAttributes,page,limit);
        if (result) {
            return "all-stations";
        }else {
            return "redirect:/";
        }
    }
}
