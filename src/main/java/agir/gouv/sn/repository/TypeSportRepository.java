package agir.gouv.sn.repository;

import agir.gouv.sn.domain.TypeSport;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TypeSport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeSportRepository extends JpaRepository<TypeSport, Long> {}
