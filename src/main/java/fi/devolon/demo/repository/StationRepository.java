package fi.devolon.demo.repository;

import fi.devolon.demo.model.Station;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface StationRepository extends CrudRepository<Station , Long> {

    @Query(value = "SELECT new fi.devolon.demo.model.Station(s.id , s.name, s.latitude, s.longitude, s.company , (6371.0 * acos(cos(radians(:slat)) * cos( radians( s.latitude ) ) * cos( radians( s.longitude ) - radians(:slon) ) + sin ( radians(:slat) ) * sin( radians( s.latitude )))) AS distance) \n" +
            "FROM Station s\n" +
            "WHERE s.latitude BETWEEN (:slat-(:dist/111.1)) AND (:slat+(:dist/111.1))\n"+
            "AND s.longitude BETWEEN (:slon-(:dist/abs(cos( radians( :slat ))*111.1))) AND (:slon+(:dist/abs(cos( radians( :slat ))*111.1)))\n"+
            "GROUP BY s.id,s.name\n"+
            "HAVING (6371.0 * acos(cos(radians(:slat)) * cos( radians( s.latitude ) ) * cos( radians( s.longitude ) - radians(:slon) ) + sin ( radians(:slat) ) * sin( radians( s.latitude )))) < :dist \n"+
            "ORDER BY distance")
    Iterable<Station> findAllStationsFromLocationWithDistance(@Param("slat") double slat, @Param("slon") double slon, @Param("dist") double dist);
}
