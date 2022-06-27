package agir.gouv.sn.repository;

import agir.gouv.sn.domain.ActivitePolitique;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ActivitePolitique entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActivitePolitiqueRepository extends JpaRepository<ActivitePolitique, Long> {}
