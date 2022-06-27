package agir.gouv.sn.web.rest;

import agir.gouv.sn.domain.RecuperationRecyclable;
import agir.gouv.sn.repository.RecuperationRecyclableRepository;
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
 * REST controller for managing {@link agir.gouv.sn.domain.RecuperationRecyclable}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RecuperationRecyclableResource {

    private final Logger log = LoggerFactory.getLogger(RecuperationRecyclableResource.class);

    private static final String ENTITY_NAME = "recuperationRecyclable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RecuperationRecyclableRepository recuperationRecyclableRepository;

    public RecuperationRecyclableResource(RecuperationRecyclableRepository recuperationRecyclableRepository) {
        this.recuperationRecyclableRepository = recuperationRecyclableRepository;
    }

    /**
     * {@code POST  /recuperation-recyclables} : Create a new recuperationRecyclable.
     *
     * @param recuperationRecyclable the recuperationRecyclable to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recuperationRecyclable, or with status {@code 400 (Bad Request)} if the recuperationRecyclable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/recuperation-recyclables")
    public ResponseEntity<RecuperationRecyclable> createRecuperationRecyclable(@RequestBody RecuperationRecyclable recuperationRecyclable)
        throws URISyntaxException {
        log.debug("REST request to save RecuperationRecyclable : {}", recuperationRecyclable);
        if (recuperationRecyclable.getId() != null) {
            throw new BadRequestAlertException("A new recuperationRecyclable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RecuperationRecyclable result = recuperationRecyclableRepository.save(recuperationRecyclable);
        return ResponseEntity
            .created(new URI("/api/recuperation-recyclables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /recuperation-recyclables/:id} : Updates an existing recuperationRecyclable.
     *
     * @param id the id of the recuperationRecyclable to save.
     * @param recuperationRecyclable the recuperationRecyclable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recuperationRecyclable,
     * or with status {@code 400 (Bad Request)} if the recuperationRecyclable is not valid,
     * or with status {@code 500 (Internal Server Error)} if the recuperationRecyclable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/recuperation-recyclables/{id}")
    public ResponseEntity<RecuperationRecyclable> updateRecuperationRecyclable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RecuperationRecyclable recuperationRecyclable
    ) throws URISyntaxException {
        log.debug("REST request to update RecuperationRecyclable : {}, {}", id, recuperationRecyclable);
        if (recuperationRecyclable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recuperationRecyclable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recuperationRecyclableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RecuperationRecyclable result = recuperationRecyclableRepository.save(recuperationRecyclable);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recuperationRecyclable.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /recuperation-recyclables/:id} : Partial updates given fields of an existing recuperationRecyclable, field will ignore if it is null
     *
     * @param id the id of the recuperationRecyclable to save.
     * @param recuperationRecyclable the recuperationRecyclable to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recuperationRecyclable,
     * or with status {@code 400 (Bad Request)} if the recuperationRecyclable is not valid,
     * or with status {@code 404 (Not Found)} if the recuperationRecyclable is not found,
     * or with status {@code 500 (Internal Server Error)} if the recuperationRecyclable couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/recuperation-recyclables/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RecuperationRecyclable> partialUpdateRecuperationRecyclable(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RecuperationRecyclable recuperationRecyclable
    ) throws URISyntaxException {
        log.debug("REST request to partial update RecuperationRecyclable partially : {}, {}", id, recuperationRecyclable);
        if (recuperationRecyclable.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recuperationRecyclable.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recuperationRecyclableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RecuperationRecyclable> result = recuperationRecyclableRepository
            .findById(recuperationRecyclable.getId())
            .map(existingRecuperationRecyclable -> {
                if (recuperationRecyclable.getNom() != null) {
                    existingRecuperationRecyclable.setNom(recuperationRecyclable.getNom());
                }
                if (recuperationRecyclable.getDate() != null) {
                    existingRecuperationRecyclable.setDate(recuperationRecyclable.getDate());
                }
                if (recuperationRecyclable.getLieu() != null) {
                    existingRecuperationRecyclable.setLieu(recuperationRecyclable.getLieu());
                }

                return existingRecuperationRecyclable;
            })
            .map(recuperationRecyclableRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recuperationRecyclable.getId().toString())
        );
    }

    /**
     * {@code GET  /recuperation-recyclables} : get all the recuperationRecyclables.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of recuperationRecyclables in body.
     */
    @GetMapping("/recuperation-recyclables")
    public List<RecuperationRecyclable> getAllRecuperationRecyclables(
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get all RecuperationRecyclables");
        return recuperationRecyclableRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /recuperation-recyclables/:id} : get the "id" recuperationRecyclable.
     *
     * @param id the id of the recuperationRecyclable to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recuperationRecyclable, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/recuperation-recyclables/{id}")
    public ResponseEntity<RecuperationRecyclable> getRecuperationRecyclable(@PathVariable Long id) {
        log.debug("REST request to get RecuperationRecyclable : {}", id);
        Optional<RecuperationRecyclable> recuperationRecyclable = recuperationRecyclableRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(recuperationRecyclable);
    }

    /**
     * {@code DELETE  /recuperation-recyclables/:id} : delete the "id" recuperationRecyclable.
     *
     * @param id the id of the recuperationRecyclable to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/recuperation-recyclables/{id}")
    public ResponseEntity<Void> deleteRecuperationRecyclable(@PathVariable Long id) {
        log.debug("REST request to delete RecuperationRecyclable : {}", id);
        recuperationRecyclableRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
