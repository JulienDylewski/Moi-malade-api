package fr.limayrac.moimalade.symptoms.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.limayrac.moimalade.disease.model.DiseaseSymptom;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "symptoms")
public class Symptoms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "symptomsFr")
    private String nomFr;

    @Column(name = "symptomsEn")
    private String nomEn;

//    @JsonIgnore
//    @OneToMany(mappedBy = "symptom")
//    private Set<DiseaseSymptom> diseaseSymptoms;
}
