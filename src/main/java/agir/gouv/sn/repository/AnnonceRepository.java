package agir.gouv.sn.repository;

import agir.gouv.sn.domain.Annonce;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Annonce entity.
 */
@Repository
public interface AnnonceRepository extends JpaRepository<Annonce, Long> {
    default Optional<Annonce> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Annonce> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Annonce> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct annonce from Annonce annonce left join fetch annonce.don left join fetch annonce.beneficiaire",
        countQuery = "select count(distinct annonce) from Annonce annonce"
    )
    Page<Annonce> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct annonce from Annonce annonce left join fetch annonce.don left join fetch annonce.beneficiaire")
    List<Annonce> findAllWithToOneRelationships();

    @Query("select annonce from Annonce annonce left join fetch annonce.don left join fetch annonce.beneficiaire where annonce.id =:id")
    Optional<Annonce> findOneWithToOneRelationships(@Param("id") Long id);
}
