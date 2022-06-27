package agir.gouv.sn.repository;

import agir.gouv.sn.domain.CollecteurOdeur;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CollecteurOdeur entity.
 */
@Repository
public interface CollecteurOdeurRepository extends JpaRepository<CollecteurOdeur, Long> {
    default Optional<CollecteurOdeur> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<CollecteurOdeur> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<CollecteurOdeur> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct collecteurOdeur from CollecteurOdeur collecteurOdeur left join fetch collecteurOdeur.quartier",
        countQuery = "select count(distinct collecteurOdeur) from CollecteurOdeur collecteurOdeur"
    )
    Page<CollecteurOdeur> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct collecteurOdeur from CollecteurOdeur collecteurOdeur left join fetch collecteurOdeur.quartier")
    List<CollecteurOdeur> findAllWithToOneRelationships();

    @Query(
        "select collecteurOdeur from CollecteurOdeur collecteurOdeur left join fetch collecteurOdeur.quartier where collecteurOdeur.id =:id"
    )
    Optional<CollecteurOdeur> findOneWithToOneRelationships(@Param("id") Long id);
}
