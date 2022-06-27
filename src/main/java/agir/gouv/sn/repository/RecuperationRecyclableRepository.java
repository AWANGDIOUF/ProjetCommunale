package agir.gouv.sn.repository;

import agir.gouv.sn.domain.RecuperationRecyclable;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RecuperationRecyclable entity.
 */
@Repository
public interface RecuperationRecyclableRepository extends JpaRepository<RecuperationRecyclable, Long> {
    default Optional<RecuperationRecyclable> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<RecuperationRecyclable> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<RecuperationRecyclable> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct recuperationRecyclable from RecuperationRecyclable recuperationRecyclable left join fetch recuperationRecyclable.quartier",
        countQuery = "select count(distinct recuperationRecyclable) from RecuperationRecyclable recuperationRecyclable"
    )
    Page<RecuperationRecyclable> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct recuperationRecyclable from RecuperationRecyclable recuperationRecyclable left join fetch recuperationRecyclable.quartier"
    )
    List<RecuperationRecyclable> findAllWithToOneRelationships();

    @Query(
        "select recuperationRecyclable from RecuperationRecyclable recuperationRecyclable left join fetch recuperationRecyclable.quartier where recuperationRecyclable.id =:id"
    )
    Optional<RecuperationRecyclable> findOneWithToOneRelationships(@Param("id") Long id);
}
