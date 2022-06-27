package agir.gouv.sn.repository;

import agir.gouv.sn.domain.Joueur;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Joueur entity.
 */
@Repository
public interface JoueurRepository extends JpaRepository<Joueur, Long> {
    default Optional<Joueur> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Joueur> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Joueur> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct joueur from Joueur joueur left join fetch joueur.equipe",
        countQuery = "select count(distinct joueur) from Joueur joueur"
    )
    Page<Joueur> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct joueur from Joueur joueur left join fetch joueur.equipe")
    List<Joueur> findAllWithToOneRelationships();

    @Query("select joueur from Joueur joueur left join fetch joueur.equipe where joueur.id =:id")
    Optional<Joueur> findOneWithToOneRelationships(@Param("id") Long id);
}
