package agir.gouv.sn.repository;

import agir.gouv.sn.domain.Artiste;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Artiste entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArtisteRepository extends JpaRepository<Artiste, Long> {}
