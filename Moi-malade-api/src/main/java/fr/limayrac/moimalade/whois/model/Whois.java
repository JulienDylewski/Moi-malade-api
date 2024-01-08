package fr.limayrac.moimalade.whois.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "whoisatriskdesc")
public class Whois {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "whoIsAtRiskDescFr")
    private String descFr;

    @Column(name = "whoIsAtRiskDescEn")
    private String descEn;

    @Column(name = "sex")
    private String sex;

    @Column(name = "ethnicity")
    private String ethnicity;

    @Column(name = "track1Start")
    private Integer track1Start;

    @Column(name = "track1End")
    private Integer track1End;

    @Column(name = "track2Start")
    private Integer track2Start;

    @Column(name = "track2End")
    private Integer track2End;

    @Column(name = "track3Start")
    private Integer track3Start;

    @Column(name = "track3End")
    private Integer track3End;
}