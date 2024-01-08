package fr.limayrac.moimalade.disease.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.limayrac.moimalade.description.model.Description;
import fr.limayrac.moimalade.medications.model.Medications;
import fr.limayrac.moimalade.medicationsdescription.model.MedicationDescription;
import fr.limayrac.moimalade.name.model.Name;
import fr.limayrac.moimalade.symptomsdescription.model.SymptomDescription;
import fr.limayrac.moimalade.testsprocedures.model.TestsProcedures;
import fr.limayrac.moimalade.whois.model.Whois;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "disease")
public class Disease {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @JoinColumn(name = "fkNameId")
    @OneToOne
    private Name name;

    @JoinColumn(name = "fkDescriptionId")
    @OneToOne
    private Description description;

    @JoinColumn(name = "fkCommonMedicationsDesc")
    @OneToOne
    private MedicationDescription medicationDescription;

    @JoinColumn(name = "fkSymptomsDesc")
    @OneToOne
    private SymptomDescription symptomDescription;

    @JoinColumn(name = "fkWhoisatriskDesc")
    @OneToOne
    private Whois whois;

    @ManyToMany
    @JoinTable(name = "tjdiseasetestprocedure",
            joinColumns = @JoinColumn(name = "fkDiseaseId"),
            inverseJoinColumns = @JoinColumn(name = "fkTestProcedure"))
    private List<TestsProcedures> testsProceduresList = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "tjdiseasemedication",
            joinColumns = @JoinColumn(name = "fkDiseaseId"),
            inverseJoinColumns = @JoinColumn(name = "fkMedicationId"))
    private List<Medications> medicationsList = new ArrayList<>();

    @JsonIgnoreProperties("disease")
    @OneToMany(mappedBy = "disease")
    private List<DiseaseSymptom> diseaseSymptoms;

    @Transient
    private Integer relevanceScore;

    @Transient
    private Double relevanceScore2;

    @Transient
    private Double percentage;
}