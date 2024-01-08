package fr.limayrac.moimalade.disease.model;

import fr.limayrac.moimalade.name.model.Name;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "disease")
public class DiseaseList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @JoinColumn(name = "fkNameId")
    @OneToOne
    private Name name;
}
