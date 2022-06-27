package agir.gouv.sn.repository;

import agir.gouv.sn.domain.Ensegnant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Ensegnant entity.
 */
@Repository
public interface EnsegnantRepository extends JpaRepository<Ensegnant, Long> {
    default Optional<Ensegnant> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Ensegnant> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Ensegnant> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct ensegnant from Ensegnant ensegnant left join fetch ensegnant.etablissement",
        countQuery = "select count(distinct ensegnant) from Ensegnant ensegnant"
    )
    Page<Ensegnant> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct ensegnant from Ensegnant ensegnant left join fetch ensegnant.etablissement")
    List<Ensegnant> findAllWithToOneRelationships();

    @Query("select ensegnant from Ensegnant ensegnant left join fetch ensegnant.etablissement where ensegnant.id =:id")
    Optional<Ensegnant> findOneWithToOneRelationships(@Param("id") Long id);
}
