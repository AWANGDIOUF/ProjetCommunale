package agir.gouv.sn.repository;

import agir.gouv.sn.domain.Don;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Don entity.
 */
@Repository
public interface DonRepository extends JpaRepository<Don, Long> {
    default Optional<Don> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Don> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Don> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select distinct don from Don don left join fetch don.donneur", countQuery = "select count(distinct don) from Don don")
    Page<Don> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct don from Don don left join fetch don.donneur")
    List<Don> findAllWithToOneRelationships();

    @Query("select don from Don don left join fetch don.donneur where don.id =:id")
    Optional<Don> findOneWithToOneRelationships(@Param("id") Long id);
}
