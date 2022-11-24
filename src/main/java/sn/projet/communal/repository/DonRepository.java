package sn.projet.communal.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import sn.projet.communal.domain.Don;

/**
 * Spring Data SQL repository for the Don entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DonRepository extends JpaRepository<Don, Long> {}
