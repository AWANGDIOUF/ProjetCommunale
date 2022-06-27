package agir.gouv.sn.repository;

import agir.gouv.sn.domain.DonSang;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DonSang entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DonSangRepository extends JpaRepository<DonSang, Long> {}
