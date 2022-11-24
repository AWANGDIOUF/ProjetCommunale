package sn.projet.communal.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.projet.communal.domain.TypeVaccin;

/**
 * Spring Data SQL repository for the TypeVaccin entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeVaccinRepository extends JpaRepository<TypeVaccin, Long> {}
