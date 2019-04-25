package fi.devolon.demo;

import fi.devolon.demo.exceptions.RestTemplateErrorHandler;
import fi.devolon.demo.model.Company;
import fi.devolon.demo.model.Station;
import fi.devolon.demo.repository.CompanyRepository;
import fi.devolon.demo.repository.StationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class DemoApplication {




    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }



    @Bean
    public RestTemplate restTemplate(RestTemplateErrorHandler restTemplateErrorHandler){
        RestTemplate restTemplate = new RestTemplate();
        HttpComponentsClientHttpRequestFactory requestFactory=new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(7000);
        requestFactory.setReadTimeout(7000);
        restTemplate.setRequestFactory(requestFactory);
        restTemplate.setErrorHandler(restTemplateErrorHandler);
        return restTemplate;
    }

    @Bean
    public CommandLineRunner fillData(StationRepository stationRepository,CompanyRepository companyRepository){
        return args -> {
            Company company=new Company("Simsinak");
            Company company1=new Company("goodbye");
            Company company2=new Company("franks");
            Company company3=new Company("amazon");
            Set<Company>  children1 = Stream.of(company1 , company2).collect(Collectors.toSet());
            Set<Company>  children2 = Stream.of(company3).collect(Collectors.toSet());
            company.setSubCompanies(children1);
            company1.setParentCompany(company);
            company2.setParentCompany(company);
            company2.setSubCompanies(children2);
            company3.setParentCompany(company2);

            Station station=new Station("A" , 35.714242 , 51.372921);
            Station station2=new Station("B" , 35.732183 , 51.371489); //1.99 km
            Station station3=new Station("C" , 35.763619 , 51.296647); //8.81 km
            Set<Station> stations1=Stream.of(station , station2).collect(Collectors.toSet());
            Set<Station> stations2=Stream.of(station3).collect(Collectors.toSet());

            station.setCompany(company);
            station2.setCompany(company);
            station3.setCompany(company3);

            company.setStations(stations1);
            company3.setStations(stations2);

            companyRepository.save(company);

        };
    }

}
