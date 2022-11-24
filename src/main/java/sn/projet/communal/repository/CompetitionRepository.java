package sn.projet.communal.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.projet.communal.domain.Competition;

/**
 * Spring Data SQL repository for the Competition entity.
 */
@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Long> {
    @Query(
        value = "select distinct competition from Competition competition left join fetch competition.clubs",
        countQuery = "select count(distinct competition) from Competition competition"
    )
    Page<Competition> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct competition from Competition competition left join fetch competition.clubs")
    List<Competition> findAllWithEagerRelationships();

    @Query("select competition from Competition competition left join fetch competition.clubs where competition.id =:id")
    Optional<Competition> findOneWithEagerRelationships(@Param("id") Long id);
}
