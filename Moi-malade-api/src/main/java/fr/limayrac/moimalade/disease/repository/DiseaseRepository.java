package fr.limayrac.moimalade.disease.repository;

import fr.limayrac.moimalade.disease.model.Disease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiseaseRepository extends JpaRepository<Disease, Integer> {

    @Query("""
        SELECT d
        FROM Disease d
        WHERE (d.whois.sex = ?1 OR d.whois.sex IS NULL)
        AND (d.whois.ethnicity LIKE %?2% OR d.whois.ethnicity IS NULL)
        AND (
            ((d.whois.track1Start > 0 AND d.whois.track1End > 0) AND ?3 BETWEEN d.whois.track1Start AND d.whois.track1End)
            OR ((d.whois.track2Start > 0 AND d.whois.track2End > 0) AND ?3 BETWEEN d.whois.track2Start AND d.whois.track2End)
            OR ((d.whois.track3Start > 0 AND d.whois.track3End > 0) AND ?3 BETWEEN d.whois.track3Start AND d.whois.track3End)
            OR (d.whois.track1Start = 0 AND d.whois.track1End = 0
                AND d.whois.track2Start = 0 AND d.whois.track2End = 0
                AND d.whois.track3Start = 0 AND d.whois.track3End = 0)
        )
    """)
    List<Disease> findBySexAndEthnicity(String sex, String ethnicity, Integer age);
}
