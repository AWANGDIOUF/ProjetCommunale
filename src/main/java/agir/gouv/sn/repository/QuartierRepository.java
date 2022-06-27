package agir.gouv.sn.repository;

import agir.gouv.sn.domain.Quartier;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Quartier entity.
 */
@Repository
public interface QuartierRepository extends JpaRepository<Quartier, Long> {
    default Optional<Quartier> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Quartier> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Quartier> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct quartier from Quartier quartier left join fetch quartier.artiste",
        countQuery = "select count(distinct quartier) from Quartier quartier"
    )
    Page<Quartier> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct quartier from Quartier quartier left join fetch quartier.artiste")
    List<Quartier> findAllWithToOneRelationships();

    @Query("select quartier from Quartier quartier left join fetch quartier.artiste where quartier.id =:id")
    Optional<Quartier> findOneWithToOneRelationships(@Param("id") Long id);
}
