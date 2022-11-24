package sn.projet.communal.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.projet.communal.domain.Beneficiaire;

/**
 * Spring Data SQL repository for the Beneficiaire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BeneficiaireRepository extends JpaRepository<Beneficiaire, Long> {}
