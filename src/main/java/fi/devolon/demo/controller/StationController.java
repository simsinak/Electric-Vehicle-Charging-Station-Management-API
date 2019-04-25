package fi.devolon.demo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import fi.devolon.demo.exceptions.ResourceNotFoundException;
import fi.devolon.demo.model.Station;
import fi.devolon.demo.model.serilizer.View;
import fi.devolon.demo.service.StationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.util.Map;

@RestController
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Validated
@RequestMapping("/api")
public class StationController {


    StationService stationService;

    @GetMapping("/stations")
    @JsonView(View.Station.class)
    public ResponseEntity<Iterable<Station>> getAllStations(@RequestParam(name = "page",defaultValue = "0") int page, @RequestParam(name = "limit",defaultValue = "30") int limit , UriComponentsBuilder uriComponentsBuilder)  {
        Page<Station> stations = stationService.getAllEntities(page,limit);
        if (page >= stations.getTotalPages()) throw new ResourceNotFoundException("Resource Not Found");
        HttpHeaders headers=new HttpHeaders();
        headers.add(HttpHeaders.LINK,ControllerUtility.linkMaker(uriComponentsBuilder,limit,stations , "api/stations"));
        return ResponseEntity.ok().headers(headers).body(stations.getContent());
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
    public Station updateStation(@Valid @RequestBody Station station, @PathVariable long id){
        return stationService.updateEntity(station , id);
    }
    @PostMapping("/stations")
    @JsonView(View.Station.class)
    @ResponseStatus(HttpStatus.CREATED)
    public Station newStation(@Valid @RequestBody Station station){
        return stationService.save(station);
    }

    @PatchMapping("/stations/{id}")
    @JsonView(View.Station.class)
    public Station patchStation(@RequestBody Map<String, String> map, @PathVariable long id){
        return stationService.patchEntity(map,id);
    }
    @GetMapping("/stations/around")
    @JsonView(View.StationWithDistance.class)
    public Iterable<Station> getAroundStations(@RequestParam(name = "latitude") @NotNull @DecimalMin(value = "-90", message = "latitude must be greater than or equal to -90") @DecimalMax(value = "+90", message = "latitude must be less than or equal to +90") double slat,
                                           @RequestParam(name = "longitude")    @NotNull @DecimalMin(value = "-180" , message = "longitude must be greater than or equal to -180") @DecimalMax(value = "+180" , message = "longitude must be less than or equal to +180") double slon,
                                           @RequestParam(name = "distance") @NotNull  double dist){
        return stationService.findAllStationsFromLocationWithDistance(slat, slon , dist);
    }


}
