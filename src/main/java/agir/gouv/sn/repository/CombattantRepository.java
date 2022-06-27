package agir.gouv.sn.repository;

import agir.gouv.sn.domain.Combattant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Combattant entity.
 */
@Repository
public interface CombattantRepository extends JpaRepository<Combattant, Long> {
    default Optional<Combattant> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Combattant> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Combattant> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct combattant from Combattant combattant left join fetch combattant.club",
        countQuery = "select count(distinct combattant) from Combattant combattant"
    )
    Page<Combattant> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct combattant from Combattant combattant left join fetch combattant.club")
    List<Combattant> findAllWithToOneRelationships();

    @Query("select combattant from Combattant combattant left join fetch combattant.club where combattant.id =:id")
    Optional<Combattant> findOneWithToOneRelationships(@Param("id") Long id);
}
