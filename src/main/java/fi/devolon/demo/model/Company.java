package fi.devolon.demo.model;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import fi.devolon.demo.model.serilizer.View;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({View.Station.class,View.Company.ChildStations.class})
    private Long id;

    @NotNull
    @Size(min = 1)
    @Column(unique = true)
    @EqualsAndHashCode.Include
    @JsonView({View.Station.class,View.Company.ChildStations.class})
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_company_id")
    private Company parentCompany;

    @OneToMany(mappedBy = "parentCompany",fetch = FetchType.LAZY , cascade = CascadeType.ALL )
    @JsonView(View.Company.ChildStations.class)
    private Set<Company> subCompanies = new HashSet<>();
    public Company(@NotNull String name) {
        this.name = name;
    }
    @OneToMany(mappedBy = "company" , fetch = FetchType.LAZY , cascade = CascadeType.ALL , orphanRemoval = true)
    @JsonView(View.Company.ChildStations.class)
    private Set<Station> stations = new HashSet<>();

}
