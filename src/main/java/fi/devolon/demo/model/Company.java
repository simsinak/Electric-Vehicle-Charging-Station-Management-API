package fi.devolon.demo.model;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1)
    @Column(unique = true)
    @EqualsAndHashCode.Include
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_company_id")
    private Company parentCompany;

    @OneToMany(mappedBy = "parentCompany",fetch = FetchType.LAZY , cascade = CascadeType.ALL )
    private Set<Company> subCompanies = new HashSet<>();
    public Company(@NotNull String name) {
        this.name = name;
    }
    @OneToMany(mappedBy = "company" , fetch = FetchType.LAZY , cascade = CascadeType.ALL , orphanRemoval = true)
    private Set<Station> stations = new HashSet<>();

}
