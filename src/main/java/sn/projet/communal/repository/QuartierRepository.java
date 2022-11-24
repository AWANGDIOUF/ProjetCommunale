package sn.projet.communal.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.projet.communal.domain.Quartier;

/**
 * Spring Data SQL repository for the Quartier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuartierRepository extends JpaRepository<Quartier, Long> {}
