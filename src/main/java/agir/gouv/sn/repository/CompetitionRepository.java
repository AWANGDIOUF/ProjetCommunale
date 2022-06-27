package agir.gouv.sn.repository;

import agir.gouv.sn.domain.Competition;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Competition entity.
 */
@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Long> {
    default Optional<Competition> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Competition> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Competition> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct competition from Competition competition left join fetch competition.club",
        countQuery = "select count(distinct competition) from Competition competition"
    )
    Page<Competition> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct competition from Competition competition left join fetch competition.club")
    List<Competition> findAllWithToOneRelationships();

    @Query("select competition from Competition competition left join fetch competition.club where competition.id =:id")
    Optional<Competition> findOneWithToOneRelationships(@Param("id") Long id);
}
