package fr.limayrac.moimalade.medicationsdescription.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "commonmedicationsdescription")
public class MedicationDescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "commonMedicationsDescriptionFr")
    private String descriptionFr;

    @Column(name = "commonMedicationsDescriptionEn")
    private String descriptionEn;
}
