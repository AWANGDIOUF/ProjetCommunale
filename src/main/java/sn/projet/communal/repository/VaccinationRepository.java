package sn.projet.communal.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.projet.communal.domain.Vaccination;

/**
 * Spring Data SQL repository for the Vaccination entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VaccinationRepository extends JpaRepository<Vaccination, Long> {}
