package agir.gouv.sn.repository;

import agir.gouv.sn.domain.SensibiisationInternet;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SensibiisationInternet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SensibiisationInternetRepository extends JpaRepository<SensibiisationInternet, Long> {}
