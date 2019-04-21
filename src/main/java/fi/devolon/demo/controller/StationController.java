package fi.devolon.demo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import fi.devolon.demo.model.Company;
import fi.devolon.demo.model.Station;
import fi.devolon.demo.model.serilizer.View;
import fi.devolon.demo.service.StationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/api")
public class StationController {


    StationService stationService;

    @GetMapping("/stations")
    @JsonView(View.Station.class)
    public Iterable<Station> getAllStations()  {
        return stationService.getAllEntities();
    }

    @GetMapping("/stations/{id}")
    @JsonView(View.Station.class)
    public Station getSingleStation(@PathVariable long id){
        return stationService.getEntityByID(id);
    }
    @DeleteMapping("/stations/{id}")
    public void deleteStation(@PathVariable long id){
        stationService.deleteEntityByID(id);
    }
    @PutMapping("/stations/{id}")
    @JsonView(View.Station.class)
    public Station updateStation(@RequestBody Station station, @PathVariable long id){
        return stationService.updateEntity(station , id);
    }
    @PostMapping("/stations")
    @JsonView(View.Station.class)
    public Station newStation(@RequestBody Station station){
        return stationService.save(station);
    }


}
