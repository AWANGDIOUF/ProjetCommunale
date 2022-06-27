package agir.gouv.sn.repository;

import agir.gouv.sn.domain.DomaineActivite;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DomaineActivite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DomaineActiviteRepository extends JpaRepository<DomaineActivite, Long> {}
