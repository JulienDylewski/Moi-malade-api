package fr.limayrac.moimalade.name.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "name")
public class Name {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nameFr")
    private String nomFr;

    @Column(name = "nameEn")
    private String nomEn;
}
