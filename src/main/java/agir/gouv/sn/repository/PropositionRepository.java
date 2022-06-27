package agir.gouv.sn.repository;

import agir.gouv.sn.domain.Proposition;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Proposition entity.
 */
@Repository
public interface PropositionRepository extends JpaRepository<Proposition, Long> {
    default Optional<Proposition> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Proposition> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Proposition> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct proposition from Proposition proposition left join fetch proposition.enseignant",
        countQuery = "select count(distinct proposition) from Proposition proposition"
    )
    Page<Proposition> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct proposition from Proposition proposition left join fetch proposition.enseignant")
    List<Proposition> findAllWithToOneRelationships();

    @Query("select proposition from Proposition proposition left join fetch proposition.enseignant where proposition.id =:id")
    Optional<Proposition> findOneWithToOneRelationships(@Param("id") Long id);
}
