package fr.limayrac.moimalade.medications.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "commonmedications")
public class Medications {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "commonMedicationsFr")
    private String nomFr;

    @Column(name = "commonMedicationsEn")
    private String nomEn;
}
