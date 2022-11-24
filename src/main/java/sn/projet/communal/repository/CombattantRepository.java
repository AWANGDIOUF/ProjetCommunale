package sn.projet.communal.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.projet.communal.domain.Combattant;

/**
 * Spring Data SQL repository for the Combattant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CombattantRepository extends JpaRepository<Combattant, Long> {}
