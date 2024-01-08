package fr.limayrac.moimalade.testsprocedures.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "commontestsandprocedures")
public class TestsProcedures {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "commonTestsAndProceduresFr")
    private String nomFr;

    @Column(name = "commonTestsAndProceduresEn")
    private String nomEn;
}
