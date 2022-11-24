package sn.projet.communal.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.projet.communal.domain.Match;

/**
 * Spring Data SQL repository for the Match entity.
 */
@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    @Query(
        value = "select distinct jhiMatch from Match jhiMatch left join fetch jhiMatch.equipes",
        countQuery = "select count(distinct jhiMatch) from Match jhiMatch"
    )
    Page<Match> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct jhiMatch from Match jhiMatch left join fetch jhiMatch.equipes")
    List<Match> findAllWithEagerRelationships();

    @Query("select jhiMatch from Match jhiMatch left join fetch jhiMatch.equipes where jhiMatch.id =:id")
    Optional<Match> findOneWithEagerRelationships(@Param("id") Long id);
}
