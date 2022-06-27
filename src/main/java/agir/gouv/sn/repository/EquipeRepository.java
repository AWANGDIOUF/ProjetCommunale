package agir.gouv.sn.repository;

import agir.gouv.sn.domain.Equipe;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Equipe entity.
 */
@Repository
public interface EquipeRepository extends JpaRepository<Equipe, Long> {
    default Optional<Equipe> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Equipe> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Equipe> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct equipe from Equipe equipe left join fetch equipe.typeSport left join fetch equipe.quartier",
        countQuery = "select count(distinct equipe) from Equipe equipe"
    )
    Page<Equipe> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct equipe from Equipe equipe left join fetch equipe.typeSport left join fetch equipe.quartier")
    List<Equipe> findAllWithToOneRelationships();

    @Query("select equipe from Equipe equipe left join fetch equipe.typeSport left join fetch equipe.quartier where equipe.id =:id")
    Optional<Equipe> findOneWithToOneRelationships(@Param("id") Long id);
}
