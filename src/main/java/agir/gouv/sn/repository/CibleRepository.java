package agir.gouv.sn.repository;

import agir.gouv.sn.domain.Cible;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Cible entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CibleRepository extends JpaRepository<Cible, Long> {}
