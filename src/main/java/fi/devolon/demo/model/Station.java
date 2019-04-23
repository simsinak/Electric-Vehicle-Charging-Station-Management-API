package fi.devolon.demo.model;



import com.fasterxml.jackson.annotation.JsonView;
import fi.devolon.demo.model.serilizer.View;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.Station.class)
    Long id;

    @NotNull
    @Size(min = 1)
    @EqualsAndHashCode.Include
    @JsonView(View.Station.class)
    String name;

    @NotNull
    @DecimalMin("-90")
    @DecimalMax("+90")
    @EqualsAndHashCode.Include
    @JsonView(View.Station.class)
    Double latitude;

    @NotNull
    @DecimalMin("-180")
    @DecimalMax("+180")
    @EqualsAndHashCode.Include
    @JsonView(View.Station.class)
    Double longitude;

    @NotNull
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "company_id")
    @JsonView(View.Station.class)
    Company company;

    @Transient
    @JsonView(View.StationWithDistance.class)

    double distance;



    public Station(@NotNull String name, @NotNull Double latitude, @NotNull Double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Station(Long id,@NotNull @Size(min = 1) String name, @NotNull @DecimalMin("-90") @DecimalMax("+90") Double latitude, @NotNull @DecimalMin("-180") @DecimalMax("+180") Double longitude, @NotNull Company company, double distance) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.company = company;
        this.distance = distance;
    }

    public double getDistance() {
        BigDecimal bigDecimal=new BigDecimal(distance).setScale(2, RoundingMode.FLOOR);
        return bigDecimal.doubleValue();
    }

    @Override
    public String toString() {
        return name+" "+latitude+" "+longitude+" "+distance;
    }


}
