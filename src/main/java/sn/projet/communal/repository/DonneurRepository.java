package sn.projet.communal.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.projet.communal.domain.Donneur;

/**
 * Spring Data SQL repository for the Donneur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DonneurRepository extends JpaRepository<Donneur, Long> {}
