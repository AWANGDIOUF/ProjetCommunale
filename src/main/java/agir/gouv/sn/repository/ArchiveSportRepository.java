package agir.gouv.sn.repository;

import agir.gouv.sn.domain.ArchiveSport;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ArchiveSport entity.
 */
@Repository
public interface ArchiveSportRepository extends JpaRepository<ArchiveSport, Long> {
    default Optional<ArchiveSport> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ArchiveSport> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ArchiveSport> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct archiveSport from ArchiveSport archiveSport left join fetch archiveSport.equipe left join fetch archiveSport.club",
        countQuery = "select count(distinct archiveSport) from ArchiveSport archiveSport"
    )
    Page<ArchiveSport> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct archiveSport from ArchiveSport archiveSport left join fetch archiveSport.equipe left join fetch archiveSport.club"
    )
    List<ArchiveSport> findAllWithToOneRelationships();

    @Query(
        "select archiveSport from ArchiveSport archiveSport left join fetch archiveSport.equipe left join fetch archiveSport.club where archiveSport.id =:id"
    )
    Optional<ArchiveSport> findOneWithToOneRelationships(@Param("id") Long id);
}
