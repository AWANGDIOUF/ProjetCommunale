package agir.gouv.sn.repository;

import agir.gouv.sn.domain.Vidange;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Vidange entity.
 */
@Repository
public interface VidangeRepository extends JpaRepository<Vidange, Long> {
    default Optional<Vidange> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Vidange> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Vidange> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct vidange from Vidange vidange left join fetch vidange.quartier",
        countQuery = "select count(distinct vidange) from Vidange vidange"
    )
    Page<Vidange> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct vidange from Vidange vidange left join fetch vidange.quartier")
    List<Vidange> findAllWithToOneRelationships();

    @Query("select vidange from Vidange vidange left join fetch vidange.quartier where vidange.id =:id")
    Optional<Vidange> findOneWithToOneRelationships(@Param("id") Long id);
}
