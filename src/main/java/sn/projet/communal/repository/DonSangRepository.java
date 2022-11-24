package sn.projet.communal.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.projet.communal.domain.DonSang;

/**
 * Spring Data SQL repository for the DonSang entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DonSangRepository extends JpaRepository<DonSang, Long> {}
