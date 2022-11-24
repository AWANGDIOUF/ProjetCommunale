package sn.projet.communal.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.projet.communal.domain.Annonce;

/**
 * Spring Data SQL repository for the Annonce entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnnonceRepository extends JpaRepository<Annonce, Long> {}
