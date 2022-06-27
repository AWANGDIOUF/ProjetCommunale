package agir.gouv.sn.repository;

import agir.gouv.sn.domain.Vainqueur;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Vainqueur entity.
 */
@Repository
public interface VainqueurRepository extends JpaRepository<Vainqueur, Long> {
    default Optional<Vainqueur> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Vainqueur> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Vainqueur> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct vainqueur from Vainqueur vainqueur left join fetch vainqueur.competition left join fetch vainqueur.combattant",
        countQuery = "select count(distinct vainqueur) from Vainqueur vainqueur"
    )
    Page<Vainqueur> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct vainqueur from Vainqueur vainqueur left join fetch vainqueur.competition left join fetch vainqueur.combattant")
    List<Vainqueur> findAllWithToOneRelationships();

    @Query(
        "select vainqueur from Vainqueur vainqueur left join fetch vainqueur.competition left join fetch vainqueur.combattant where vainqueur.id =:id"
    )
    Optional<Vainqueur> findOneWithToOneRelationships(@Param("id") Long id);
}
