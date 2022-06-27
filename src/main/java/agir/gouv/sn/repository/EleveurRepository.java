package agir.gouv.sn.repository;

import agir.gouv.sn.domain.Eleveur;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Eleveur entity.
 */
@Repository
public interface EleveurRepository extends JpaRepository<Eleveur, Long> {
    default Optional<Eleveur> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Eleveur> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Eleveur> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct eleveur from Eleveur eleveur left join fetch eleveur.quartier",
        countQuery = "select count(distinct eleveur) from Eleveur eleveur"
    )
    Page<Eleveur> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct eleveur from Eleveur eleveur left join fetch eleveur.quartier")
    List<Eleveur> findAllWithToOneRelationships();

    @Query("select eleveur from Eleveur eleveur left join fetch eleveur.quartier where eleveur.id =:id")
    Optional<Eleveur> findOneWithToOneRelationships(@Param("id") Long id);
}
