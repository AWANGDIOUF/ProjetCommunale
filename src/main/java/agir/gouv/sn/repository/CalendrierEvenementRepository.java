package agir.gouv.sn.repository;

import agir.gouv.sn.domain.CalendrierEvenement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CalendrierEvenement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CalendrierEvenementRepository extends JpaRepository<CalendrierEvenement, Long> {}
