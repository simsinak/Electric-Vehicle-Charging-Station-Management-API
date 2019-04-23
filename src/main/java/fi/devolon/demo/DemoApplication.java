package fi.devolon.demo;

import fi.devolon.demo.model.Company;
import fi.devolon.demo.model.Station;
import fi.devolon.demo.repository.CompanyRepository;
import fi.devolon.demo.repository.StationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class DemoApplication {




    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner fillData(StationRepository stationRepository,CompanyRepository companyRepository){
        return args -> {
            Company company=new Company("simsinak");
            Company company1=new Company("goodby");
            Company company2=new Company("franks");
            Company company3=new Company("amazon");
            Set<Company>  childs1 = Stream.of(company1 , company2).collect(Collectors.toSet());
            Set<Company>  childs2 = Stream.of(company3).collect(Collectors.toSet());
            company.setSubCompanies(childs1);
            company1.setParentCompany(company);
            company2.setParentCompany(company);
            company2.setSubCompanies(childs2);
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
