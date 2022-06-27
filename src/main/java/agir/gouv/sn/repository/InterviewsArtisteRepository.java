package agir.gouv.sn.repository;

import agir.gouv.sn.domain.InterviewsArtiste;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the InterviewsArtiste entity.
 */
@Repository
public interface InterviewsArtisteRepository extends JpaRepository<InterviewsArtiste, Long> {
    default Optional<InterviewsArtiste> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<InterviewsArtiste> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<InterviewsArtiste> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct interviewsArtiste from InterviewsArtiste interviewsArtiste left join fetch interviewsArtiste.artiste",
        countQuery = "select count(distinct interviewsArtiste) from InterviewsArtiste interviewsArtiste"
    )
    Page<InterviewsArtiste> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct interviewsArtiste from InterviewsArtiste interviewsArtiste left join fetch interviewsArtiste.artiste")
    List<InterviewsArtiste> findAllWithToOneRelationships();

    @Query(
        "select interviewsArtiste from InterviewsArtiste interviewsArtiste left join fetch interviewsArtiste.artiste where interviewsArtiste.id =:id"
    )
    Optional<InterviewsArtiste> findOneWithToOneRelationships(@Param("id") Long id);
}
