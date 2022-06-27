package agir.gouv.sn.repository;

import agir.gouv.sn.domain.DemandeInterview;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DemandeInterview entity.
 */
@Repository
public interface DemandeInterviewRepository extends JpaRepository<DemandeInterview, Long> {
    default Optional<DemandeInterview> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<DemandeInterview> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<DemandeInterview> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct demandeInterview from DemandeInterview demandeInterview left join fetch demandeInterview.entrepreneur",
        countQuery = "select count(distinct demandeInterview) from DemandeInterview demandeInterview"
    )
    Page<DemandeInterview> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct demandeInterview from DemandeInterview demandeInterview left join fetch demandeInterview.entrepreneur")
    List<DemandeInterview> findAllWithToOneRelationships();

    @Query(
        "select demandeInterview from DemandeInterview demandeInterview left join fetch demandeInterview.entrepreneur where demandeInterview.id =:id"
    )
    Optional<DemandeInterview> findOneWithToOneRelationships(@Param("id") Long id);
}
