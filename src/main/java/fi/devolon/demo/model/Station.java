package fi.devolon.demo.model;


import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    @Size(min = 1)
    @EqualsAndHashCode.Include
    String name;

    @NotNull
    @DecimalMin("-90")
    @DecimalMax("+90")
    @EqualsAndHashCode.Include
    Double latitude;

    @NotNull
    @DecimalMin("-180")
    @DecimalMax("+180")
    @EqualsAndHashCode.Include
    Double longitude;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "company_id")
    Company company;

    public Station(@NotNull String name, @NotNull Double latitude, @NotNull Double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
