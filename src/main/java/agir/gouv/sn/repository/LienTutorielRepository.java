package agir.gouv.sn.repository;

import agir.gouv.sn.domain.LienTutoriel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the LienTutoriel entity.
 */
@Repository
public interface LienTutorielRepository extends JpaRepository<LienTutoriel, Long> {
    default Optional<LienTutoriel> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<LienTutoriel> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<LienTutoriel> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct lienTutoriel from LienTutoriel lienTutoriel left join fetch lienTutoriel.enseignant",
        countQuery = "select count(distinct lienTutoriel) from LienTutoriel lienTutoriel"
    )
    Page<LienTutoriel> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct lienTutoriel from LienTutoriel lienTutoriel left join fetch lienTutoriel.enseignant")
    List<LienTutoriel> findAllWithToOneRelationships();

    @Query("select lienTutoriel from LienTutoriel lienTutoriel left join fetch lienTutoriel.enseignant where lienTutoriel.id =:id")
    Optional<LienTutoriel> findOneWithToOneRelationships(@Param("id") Long id);
}
