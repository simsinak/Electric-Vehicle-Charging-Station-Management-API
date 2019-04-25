package fi.devolon.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import fi.devolon.demo.controller.CompanyController;
import fi.devolon.demo.model.Company;
import fi.devolon.demo.model.Station;
import fi.devolon.demo.service.CompanyService;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class DemoApplicationTests {
    private static final ObjectMapper om = new ObjectMapper();

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    CompanyController companyController;

    @MockBean()
    private CompanyService companyService;
    @Test
    public void contextLoads() {
    }
    @Test
    public void getSingleCompany() throws JSONException {
        Set<Station> stations=new HashSet<Station>();
        Mockito.when(companyService.getEntityByID(1L)).thenReturn(new Company(1L,"simsinak",null,new HashSet<Company>(),stations));
        JSONAssert.assertEquals("{\"id\":1,\"name\":\"simsinak\"}", restTemplate.getForObject("http://localhost:" + port + "/api/companies/1",String.class),false);
    }

}
