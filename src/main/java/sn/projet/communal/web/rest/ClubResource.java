package sn.projet.communal.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sn.projet.communal.domain.Club;
import sn.projet.communal.repository.ClubRepository;
import sn.projet.communal.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.projet.communal.domain.Club}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ClubResource {

    private final Logger log = LoggerFactory.getLogger(ClubResource.class);

    private static final String ENTITY_NAME = "club";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClubRepository clubRepository;

    public ClubResource(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    /**
     * {@code POST  /clubs} : Create a new club.
     *
     * @param club the club to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new club, or with status {@code 400 (Bad Request)} if the club has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/clubs")
    public ResponseEntity<Club> createClub(@RequestBody Club club) throws URISyntaxException {
        log.debug("REST request to save Club : {}", club);
        if (club.getId() != null) {
            throw new BadRequestAlertException("A new club cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Club result = clubRepository.save(club);
        return ResponseEntity
            .created(new URI("/api/clubs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /clubs/:id} : Updates an existing club.
     *
     * @param id the id of the club to save.
     * @param club the club to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated club,
     * or with status {@code 400 (Bad Request)} if the club is not valid,
     * or with status {@code 500 (Internal Server Error)} if the club couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/clubs/{id}")
    public ResponseEntity<Club> updateClub(@PathVariable(value = "id", required = false) final Long id, @RequestBody Club club)
        throws URISyntaxException {
        log.debug("REST request to update Club : {}, {}", id, club);
        if (club.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, club.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clubRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Club result = clubRepository.save(club);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, club.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /clubs/:id} : Partial updates given fields of an existing club, field will ignore if it is null
     *
     * @param id the id of the club to save.
     * @param club the club to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated club,
     * or with status {@code 400 (Bad Request)} if the club is not valid,
     * or with status {@code 404 (Not Found)} if the club is not found,
     * or with status {@code 500 (Internal Server Error)} if the club couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/clubs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Club> partialUpdateClub(@PathVariable(value = "id", required = false) final Long id, @RequestBody Club club)
        throws URISyntaxException {
        log.debug("REST request to partial update Club partially : {}, {}", id, club);
        if (club.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, club.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clubRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Club> result = clubRepository
            .findById(club.getId())
            .map(
                existingClub -> {
                    if (club.getNomClub() != null) {
                        existingClub.setNomClub(club.getNomClub());
                    }
                    if (club.getDateCreation() != null) {
                        existingClub.setDateCreation(club.getDateCreation());
                    }
                    if (club.getLogo() != null) {
                        existingClub.setLogo(club.getLogo());
                    }
                    if (club.getLogoContentType() != null) {
                        existingClub.setLogoContentType(club.getLogoContentType());
                    }
                    if (club.getDiscipline() != null) {
                        existingClub.setDiscipline(club.getDiscipline());
                    }

                    return existingClub;
                }
            )
            .map(clubRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, club.getId().toString())
        );
    }

    /**
     * {@code GET  /clubs} : get all the clubs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clubs in body.
     */
    @GetMapping("/clubs")
    public ResponseEntity<List<Club>> getAllClubs(Pageable pageable) {
        log.debug("REST request to get a page of Clubs");
        Page<Club> page = clubRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /clubs/:id} : get the "id" club.
     *
     * @param id the id of the club to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the club, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/clubs/{id}")
    public ResponseEntity<Club> getClub(@PathVariable Long id) {
        log.debug("REST request to get Club : {}", id);
        Optional<Club> club = clubRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(club);
    }

    /**
     * {@code DELETE  /clubs/:id} : delete the "id" club.
     *
     * @param id the id of the club to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/clubs/{id}")
    public ResponseEntity<Void> deleteClub(@PathVariable Long id) {
        log.debug("REST request to delete Club : {}", id);
        clubRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
