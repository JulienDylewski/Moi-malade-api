package fr.limayrac.moimalade.disease.model;

import fr.limayrac.moimalade.symptoms.model.Symptoms;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tjdiseasesymptoms")
public class DiseaseSymptom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "fkDiseaseId")
    private Disease disease;

    @ManyToOne
    @JoinColumn(name = "fkSymptomId")
    private Symptoms symptom;

    @Column(name = "painIndex")
    private Integer painIndex;
}
