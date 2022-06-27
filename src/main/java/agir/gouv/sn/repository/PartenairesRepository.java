package agir.gouv.sn.repository;

import agir.gouv.sn.domain.Partenaires;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Partenaires entity.
 */
@Repository
public interface PartenairesRepository extends JpaRepository<Partenaires, Long> {
    default Optional<Partenaires> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Partenaires> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Partenaires> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct partenaires from Partenaires partenaires left join fetch partenaires.entrepreneur left join fetch partenaires.eleveur",
        countQuery = "select count(distinct partenaires) from Partenaires partenaires"
    )
    Page<Partenaires> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct partenaires from Partenaires partenaires left join fetch partenaires.entrepreneur left join fetch partenaires.eleveur"
    )
    List<Partenaires> findAllWithToOneRelationships();

    @Query(
        "select partenaires from Partenaires partenaires left join fetch partenaires.entrepreneur left join fetch partenaires.eleveur where partenaires.id =:id"
    )
    Optional<Partenaires> findOneWithToOneRelationships(@Param("id") Long id);
}
