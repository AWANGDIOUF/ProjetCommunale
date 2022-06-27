package agir.gouv.sn.repository;

import agir.gouv.sn.domain.Beneficiaire;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Beneficiaire entity.
 */
@Repository
public interface BeneficiaireRepository extends JpaRepository<Beneficiaire, Long> {
    default Optional<Beneficiaire> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Beneficiaire> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Beneficiaire> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct beneficiaire from Beneficiaire beneficiaire left join fetch beneficiaire.quartier",
        countQuery = "select count(distinct beneficiaire) from Beneficiaire beneficiaire"
    )
    Page<Beneficiaire> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct beneficiaire from Beneficiaire beneficiaire left join fetch beneficiaire.quartier")
    List<Beneficiaire> findAllWithToOneRelationships();

    @Query("select beneficiaire from Beneficiaire beneficiaire left join fetch beneficiaire.quartier where beneficiaire.id =:id")
    Optional<Beneficiaire> findOneWithToOneRelationships(@Param("id") Long id);
}
