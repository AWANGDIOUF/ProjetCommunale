package sn.projet.communal.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.projet.communal.domain.TypeSport;

/**
 * Spring Data SQL repository for the TypeSport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeSportRepository extends JpaRepository<TypeSport, Long> {}
