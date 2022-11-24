package sn.projet.communal.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.projet.communal.domain.Cible;

/**
 * Spring Data SQL repository for the Cible entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CibleRepository extends JpaRepository<Cible, Long> {}
