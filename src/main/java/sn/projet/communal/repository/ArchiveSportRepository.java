package sn.projet.communal.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.projet.communal.domain.ArchiveSport;

/**
 * Spring Data SQL repository for the ArchiveSport entity.
 */
@Repository
public interface ArchiveSportRepository extends JpaRepository<ArchiveSport, Long> {
    @Query(
        value = "select distinct archiveSport from ArchiveSport archiveSport left join fetch archiveSport.equipes left join fetch archiveSport.clubs",
        countQuery = "select count(distinct archiveSport) from ArchiveSport archiveSport"
    )
    Page<ArchiveSport> findAllWithEagerRelationships(Pageable pageable);

    @Query(
        "select distinct archiveSport from ArchiveSport archiveSport left join fetch archiveSport.equipes left join fetch archiveSport.clubs"
    )
    List<ArchiveSport> findAllWithEagerRelationships();

    @Query(
        "select archiveSport from ArchiveSport archiveSport left join fetch archiveSport.equipes left join fetch archiveSport.clubs where archiveSport.id =:id"
    )
    Optional<ArchiveSport> findOneWithEagerRelationships(@Param("id") Long id);
}
