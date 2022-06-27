package agir.gouv.sn.repository;

import agir.gouv.sn.domain.Entreprenariat;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Entreprenariat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntreprenariatRepository extends JpaRepository<Entreprenariat, Long> {}
