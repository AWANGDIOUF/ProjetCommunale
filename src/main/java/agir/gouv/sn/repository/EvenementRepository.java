package agir.gouv.sn.repository;

import agir.gouv.sn.domain.Evenement;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Evenement entity.
 */
@Repository
public interface EvenementRepository extends JpaRepository<Evenement, Long> {
    default Optional<Evenement> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Evenement> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Evenement> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct evenement from Evenement evenement left join fetch evenement.artiste",
        countQuery = "select count(distinct evenement) from Evenement evenement"
    )
    Page<Evenement> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct evenement from Evenement evenement left join fetch evenement.artiste")
    List<Evenement> findAllWithToOneRelationships();

    @Query("select evenement from Evenement evenement left join fetch evenement.artiste where evenement.id =:id")
    Optional<Evenement> findOneWithToOneRelationships(@Param("id") Long id);
}
