package agir.gouv.sn.repository;

import agir.gouv.sn.domain.TypeVaccin;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TypeVaccin entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeVaccinRepository extends JpaRepository<TypeVaccin, Long> {}
