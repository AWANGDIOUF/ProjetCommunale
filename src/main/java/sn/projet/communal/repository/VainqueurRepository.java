package sn.projet.communal.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.projet.communal.domain.Vainqueur;

/**
 * Spring Data SQL repository for the Vainqueur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VainqueurRepository extends JpaRepository<Vainqueur, Long> {}
