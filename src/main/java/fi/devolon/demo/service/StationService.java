package fi.devolon.demo.service;

import fi.devolon.demo.exceptions.MissingValueException;
import fi.devolon.demo.exceptions.StationNotFoundException;
import fi.devolon.demo.model.Company;
import fi.devolon.demo.model.Station;
import fi.devolon.demo.repository.StationRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class StationService implements BasicRestService<Station> {

    StationRepository stationRepository;
    CompanyService companyService;
    @Override
    public Station save(Station station) {
        Company company=companyService.getEntityByID(station.getCompany().getId());
        if (company.getStations().contains(station)) throw new DataIntegrityViolationException("Values Are Duplicate");
        company.getStations().add(station);
        station.setCompany(company);
        return stationRepository.save(station);
    }

    @Override
    public Iterable<Station> getAllEntities() {
        return stationRepository.findAll();
    }

    @Override
    public Station getEntityByID(Long id) {
        if(id==null) throw new MissingValueException("station id must not be null");
        Optional<Station> station= stationRepository.findById(id);
        return station.orElseThrow(()-> new StationNotFoundException(id));
    }

    @Override
    public void deleteEntityByID(Long id) {
        if(stationRepository.existsById(id)) {
            stationRepository.deleteById(id);
        }
        else{
            throw new StationNotFoundException(id);
        }
    }

    @Override
    public Station updateEntity(Station station, Long id) {
        return stationRepository.findById(id).map(stn-> {stn.setName(station.getName());
            stn.setLatitude(station.getLatitude());
            stn.setLongitude(station.getLongitude());
            Company company=companyService.getEntityByID(station.getCompany().getId());
            stn.setCompany(company);
            company.getStations().add(stn);
            System.out.println(stn.getName());
            System.out.println(stn.getLongitude());
            return stationRepository.save(stn);})
                .orElseThrow(()-> new StationNotFoundException(id));
    }
}
