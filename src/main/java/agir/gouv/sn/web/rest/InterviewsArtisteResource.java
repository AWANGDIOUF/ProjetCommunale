package agir.gouv.sn.web.rest;

import agir.gouv.sn.domain.InterviewsArtiste;
import agir.gouv.sn.repository.InterviewsArtisteRepository;
import agir.gouv.sn.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link agir.gouv.sn.domain.InterviewsArtiste}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class InterviewsArtisteResource {

    private final Logger log = LoggerFactory.getLogger(InterviewsArtisteResource.class);

    private static final String ENTITY_NAME = "interviewsArtiste";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InterviewsArtisteRepository interviewsArtisteRepository;

    public InterviewsArtisteResource(InterviewsArtisteRepository interviewsArtisteRepository) {
        this.interviewsArtisteRepository = interviewsArtisteRepository;
    }

    /**
     * {@code POST  /interviews-artistes} : Create a new interviewsArtiste.
     *
     * @param interviewsArtiste the interviewsArtiste to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new interviewsArtiste, or with status {@code 400 (Bad Request)} if the interviewsArtiste has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/interviews-artistes")
    public ResponseEntity<InterviewsArtiste> createInterviewsArtiste(@RequestBody InterviewsArtiste interviewsArtiste)
        throws URISyntaxException {
        log.debug("REST request to save InterviewsArtiste : {}", interviewsArtiste);
        if (interviewsArtiste.getId() != null) {
            throw new BadRequestAlertException("A new interviewsArtiste cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InterviewsArtiste result = interviewsArtisteRepository.save(interviewsArtiste);
        return ResponseEntity
            .created(new URI("/api/interviews-artistes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /interviews-artistes/:id} : Updates an existing interviewsArtiste.
     *
     * @param id the id of the interviewsArtiste to save.
     * @param interviewsArtiste the interviewsArtiste to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated interviewsArtiste,
     * or with status {@code 400 (Bad Request)} if the interviewsArtiste is not valid,
     * or with status {@code 500 (Internal Server Error)} if the interviewsArtiste couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/interviews-artistes/{id}")
    public ResponseEntity<InterviewsArtiste> updateInterviewsArtiste(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InterviewsArtiste interviewsArtiste
    ) throws URISyntaxException {
        log.debug("REST request to update InterviewsArtiste : {}, {}", id, interviewsArtiste);
        if (interviewsArtiste.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, interviewsArtiste.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!interviewsArtisteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InterviewsArtiste result = interviewsArtisteRepository.save(interviewsArtiste);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, interviewsArtiste.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /interviews-artistes/:id} : Partial updates given fields of an existing interviewsArtiste, field will ignore if it is null
     *
     * @param id the id of the interviewsArtiste to save.
     * @param interviewsArtiste the interviewsArtiste to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated interviewsArtiste,
     * or with status {@code 400 (Bad Request)} if the interviewsArtiste is not valid,
     * or with status {@code 404 (Not Found)} if the interviewsArtiste is not found,
     * or with status {@code 500 (Internal Server Error)} if the interviewsArtiste couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/interviews-artistes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InterviewsArtiste> partialUpdateInterviewsArtiste(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody InterviewsArtiste interviewsArtiste
    ) throws URISyntaxException {
        log.debug("REST request to partial update InterviewsArtiste partially : {}, {}", id, interviewsArtiste);
        if (interviewsArtiste.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, interviewsArtiste.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!interviewsArtisteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InterviewsArtiste> result = interviewsArtisteRepository
            .findById(interviewsArtiste.getId())
            .map(existingInterviewsArtiste -> {
                if (interviewsArtiste.getTitre() != null) {
                    existingInterviewsArtiste.setTitre(interviewsArtiste.getTitre());
                }
                if (interviewsArtiste.getDescription() != null) {
                    existingInterviewsArtiste.setDescription(interviewsArtiste.getDescription());
                }
                if (interviewsArtiste.getLien() != null) {
                    existingInterviewsArtiste.setLien(interviewsArtiste.getLien());
                }

                return existingInterviewsArtiste;
            })
            .map(interviewsArtisteRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, interviewsArtiste.getId().toString())
        );
    }

    /**
     * {@code GET  /interviews-artistes} : get all the interviewsArtistes.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of interviewsArtistes in body.
     */
    @GetMapping("/interviews-artistes")
    public List<InterviewsArtiste> getAllInterviewsArtistes(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all InterviewsArtistes");
        return interviewsArtisteRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /interviews-artistes/:id} : get the "id" interviewsArtiste.
     *
     * @param id the id of the interviewsArtiste to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the interviewsArtiste, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/interviews-artistes/{id}")
    public ResponseEntity<InterviewsArtiste> getInterviewsArtiste(@PathVariable Long id) {
        log.debug("REST request to get InterviewsArtiste : {}", id);
        Optional<InterviewsArtiste> interviewsArtiste = interviewsArtisteRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(interviewsArtiste);
    }

    /**
     * {@code DELETE  /interviews-artistes/:id} : delete the "id" interviewsArtiste.
     *
     * @param id the id of the interviewsArtiste to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/interviews-artistes/{id}")
    public ResponseEntity<Void> deleteInterviewsArtiste(@PathVariable Long id) {
        log.debug("REST request to delete InterviewsArtiste : {}", id);
        interviewsArtisteRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
