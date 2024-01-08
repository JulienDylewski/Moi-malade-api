package fr.limayrac.moimalade.symptomsdescription.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "symptomsdescription")
public class SymptomDescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "symptomsDescFr")
    private String nomFr;

    @Column(name = "symptomsDescEn")
    private String nomEn;
}
