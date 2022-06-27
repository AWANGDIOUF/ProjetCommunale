package agir.gouv.sn.repository;

import agir.gouv.sn.domain.Entrepreneur;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Entrepreneur entity.
 */
@Repository
public interface EntrepreneurRepository extends JpaRepository<Entrepreneur, Long> {
    default Optional<Entrepreneur> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Entrepreneur> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Entrepreneur> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct entrepreneur from Entrepreneur entrepreneur left join fetch entrepreneur.entreprenariat left join fetch entrepreneur.domaineActivite",
        countQuery = "select count(distinct entrepreneur) from Entrepreneur entrepreneur"
    )
    Page<Entrepreneur> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct entrepreneur from Entrepreneur entrepreneur left join fetch entrepreneur.entreprenariat left join fetch entrepreneur.domaineActivite"
    )
    List<Entrepreneur> findAllWithToOneRelationships();

    @Query(
        "select entrepreneur from Entrepreneur entrepreneur left join fetch entrepreneur.entreprenariat left join fetch entrepreneur.domaineActivite where entrepreneur.id =:id"
    )
    Optional<Entrepreneur> findOneWithToOneRelationships(@Param("id") Long id);
}
