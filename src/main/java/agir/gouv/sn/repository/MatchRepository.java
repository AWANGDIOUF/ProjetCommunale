package agir.gouv.sn.repository;

import agir.gouv.sn.domain.Match;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Match entity.
 */
@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    default Optional<Match> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Match> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Match> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct jhiMatch from Match jhiMatch left join fetch jhiMatch.equipe",
        countQuery = "select count(distinct jhiMatch) from Match jhiMatch"
    )
    Page<Match> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct jhiMatch from Match jhiMatch left join fetch jhiMatch.equipe")
    List<Match> findAllWithToOneRelationships();

    @Query("select jhiMatch from Match jhiMatch left join fetch jhiMatch.equipe where jhiMatch.id =:id")
    Optional<Match> findOneWithToOneRelationships(@Param("id") Long id);
}
