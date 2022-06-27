package agir.gouv.sn.repository;

import agir.gouv.sn.domain.Donneur;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Donneur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DonneurRepository extends JpaRepository<Donneur, Long> {}
