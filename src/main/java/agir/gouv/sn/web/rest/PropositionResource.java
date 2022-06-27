package agir.gouv.sn.web.rest;

import agir.gouv.sn.domain.Proposition;
import agir.gouv.sn.repository.PropositionRepository;
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
 * REST controller for managing {@link agir.gouv.sn.domain.Proposition}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PropositionResource {

    private final Logger log = LoggerFactory.getLogger(PropositionResource.class);

    private static final String ENTITY_NAME = "proposition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PropositionRepository propositionRepository;

    public PropositionResource(PropositionRepository propositionRepository) {
        this.propositionRepository = propositionRepository;
    }

    /**
     * {@code POST  /propositions} : Create a new proposition.
     *
     * @param proposition the proposition to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new proposition, or with status {@code 400 (Bad Request)} if the proposition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/propositions")
    public ResponseEntity<Proposition> createProposition(@RequestBody Proposition proposition) throws URISyntaxException {
        log.debug("REST request to save Proposition : {}", proposition);
        if (proposition.getId() != null) {
            throw new BadRequestAlertException("A new proposition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Proposition result = propositionRepository.save(proposition);
        return ResponseEntity
            .created(new URI("/api/propositions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /propositions/:id} : Updates an existing proposition.
     *
     * @param id the id of the proposition to save.
     * @param proposition the proposition to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated proposition,
     * or with status {@code 400 (Bad Request)} if the proposition is not valid,
     * or with status {@code 500 (Internal Server Error)} if the proposition couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/propositions/{id}")
    public ResponseEntity<Proposition> updateProposition(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Proposition proposition
    ) throws URISyntaxException {
        log.debug("REST request to update Proposition : {}, {}", id, proposition);
        if (proposition.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, proposition.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!propositionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Proposition result = propositionRepository.save(proposition);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, proposition.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /propositions/:id} : Partial updates given fields of an existing proposition, field will ignore if it is null
     *
     * @param id the id of the proposition to save.
     * @param proposition the proposition to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated proposition,
     * or with status {@code 400 (Bad Request)} if the proposition is not valid,
     * or with status {@code 404 (Not Found)} if the proposition is not found,
     * or with status {@code 500 (Internal Server Error)} if the proposition couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/propositions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Proposition> partialUpdateProposition(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Proposition proposition
    ) throws URISyntaxException {
        log.debug("REST request to partial update Proposition partially : {}, {}", id, proposition);
        if (proposition.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, proposition.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!propositionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Proposition> result = propositionRepository
            .findById(proposition.getId())
            .map(existingProposition -> {
                if (proposition.getDescription() != null) {
                    existingProposition.setDescription(proposition.getDescription());
                }

                return existingProposition;
            })
            .map(propositionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, proposition.getId().toString())
        );
    }

    /**
     * {@code GET  /propositions} : get all the propositions.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of propositions in body.
     */
    @GetMapping("/propositions")
    public List<Proposition> getAllPropositions(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Propositions");
        return propositionRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /propositions/:id} : get the "id" proposition.
     *
     * @param id the id of the proposition to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the proposition, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/propositions/{id}")
    public ResponseEntity<Proposition> getProposition(@PathVariable Long id) {
        log.debug("REST request to get Proposition : {}", id);
        Optional<Proposition> proposition = propositionRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(proposition);
    }

    /**
     * {@code DELETE  /propositions/:id} : delete the "id" proposition.
     *
     * @param id the id of the proposition to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/propositions/{id}")
    public ResponseEntity<Void> deleteProposition(@PathVariable Long id) {
        log.debug("REST request to delete Proposition : {}", id);
        propositionRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
