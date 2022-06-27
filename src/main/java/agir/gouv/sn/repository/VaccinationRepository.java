package agir.gouv.sn.repository;

import agir.gouv.sn.domain.Vaccination;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Vaccination entity.
 */
@Repository
public interface VaccinationRepository extends JpaRepository<Vaccination, Long> {
    default Optional<Vaccination> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Vaccination> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Vaccination> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct vaccination from Vaccination vaccination left join fetch vaccination.typeVaccin",
        countQuery = "select count(distinct vaccination) from Vaccination vaccination"
    )
    Page<Vaccination> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct vaccination from Vaccination vaccination left join fetch vaccination.typeVaccin")
    List<Vaccination> findAllWithToOneRelationships();

    @Query("select vaccination from Vaccination vaccination left join fetch vaccination.typeVaccin where vaccination.id =:id")
    Optional<Vaccination> findOneWithToOneRelationships(@Param("id") Long id);
}
