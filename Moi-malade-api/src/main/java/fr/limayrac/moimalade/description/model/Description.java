package fr.limayrac.moimalade.description.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "description")
public class Description {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "descriptionFr")
    private String nomFr;

    @Column(name = "descriptionEn")
    private String nomEn;
}