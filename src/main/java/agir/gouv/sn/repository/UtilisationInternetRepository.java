package agir.gouv.sn.repository;

import agir.gouv.sn.domain.UtilisationInternet;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UtilisationInternet entity.
 */
@Repository
public interface UtilisationInternetRepository extends JpaRepository<UtilisationInternet, Long> {
    default Optional<UtilisationInternet> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<UtilisationInternet> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<UtilisationInternet> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct utilisationInternet from UtilisationInternet utilisationInternet left join fetch utilisationInternet.logiciel",
        countQuery = "select count(distinct utilisationInternet) from UtilisationInternet utilisationInternet"
    )
    Page<UtilisationInternet> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct utilisationInternet from UtilisationInternet utilisationInternet left join fetch utilisationInternet.logiciel")
    List<UtilisationInternet> findAllWithToOneRelationships();

    @Query(
        "select utilisationInternet from UtilisationInternet utilisationInternet left join fetch utilisationInternet.logiciel where utilisationInternet.id =:id"
    )
    Optional<UtilisationInternet> findOneWithToOneRelationships(@Param("id") Long id);
}
