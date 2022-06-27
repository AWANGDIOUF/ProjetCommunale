package agir.gouv.sn.repository;

import agir.gouv.sn.domain.ResultatExamen;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ResultatExamen entity.
 */
@Repository
public interface ResultatExamenRepository extends JpaRepository<ResultatExamen, Long> {
    default Optional<ResultatExamen> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ResultatExamen> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ResultatExamen> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct resultatExamen from ResultatExamen resultatExamen left join fetch resultatExamen.etablissement",
        countQuery = "select count(distinct resultatExamen) from ResultatExamen resultatExamen"
    )
    Page<ResultatExamen> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct resultatExamen from ResultatExamen resultatExamen left join fetch resultatExamen.etablissement")
    List<ResultatExamen> findAllWithToOneRelationships();

    @Query(
        "select resultatExamen from ResultatExamen resultatExamen left join fetch resultatExamen.etablissement where resultatExamen.id =:id"
    )
    Optional<ResultatExamen> findOneWithToOneRelationships(@Param("id") Long id);
}
