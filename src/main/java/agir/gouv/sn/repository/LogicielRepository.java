package agir.gouv.sn.repository;

import agir.gouv.sn.domain.Logiciel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Logiciel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LogicielRepository extends JpaRepository<Logiciel, Long> {}
