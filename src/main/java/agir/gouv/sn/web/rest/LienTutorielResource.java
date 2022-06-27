package agir.gouv.sn.web.rest;

import agir.gouv.sn.domain.LienTutoriel;
import agir.gouv.sn.repository.LienTutorielRepository;
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
 * REST controller for managing {@link agir.gouv.sn.domain.LienTutoriel}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LienTutorielResource {

    private final Logger log = LoggerFactory.getLogger(LienTutorielResource.class);

    private static final String ENTITY_NAME = "lienTutoriel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LienTutorielRepository lienTutorielRepository;

    public LienTutorielResource(LienTutorielRepository lienTutorielRepository) {
        this.lienTutorielRepository = lienTutorielRepository;
    }

    /**
     * {@code POST  /lien-tutoriels} : Create a new lienTutoriel.
     *
     * @param lienTutoriel the lienTutoriel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lienTutoriel, or with status {@code 400 (Bad Request)} if the lienTutoriel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lien-tutoriels")
    public ResponseEntity<LienTutoriel> createLienTutoriel(@RequestBody LienTutoriel lienTutoriel) throws URISyntaxException {
        log.debug("REST request to save LienTutoriel : {}", lienTutoriel);
        if (lienTutoriel.getId() != null) {
            throw new BadRequestAlertException("A new lienTutoriel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LienTutoriel result = lienTutorielRepository.save(lienTutoriel);
        return ResponseEntity
            .created(new URI("/api/lien-tutoriels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lien-tutoriels/:id} : Updates an existing lienTutoriel.
     *
     * @param id the id of the lienTutoriel to save.
     * @param lienTutoriel the lienTutoriel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lienTutoriel,
     * or with status {@code 400 (Bad Request)} if the lienTutoriel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lienTutoriel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lien-tutoriels/{id}")
    public ResponseEntity<LienTutoriel> updateLienTutoriel(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LienTutoriel lienTutoriel
    ) throws URISyntaxException {
        log.debug("REST request to update LienTutoriel : {}, {}", id, lienTutoriel);
        if (lienTutoriel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lienTutoriel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lienTutorielRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LienTutoriel result = lienTutorielRepository.save(lienTutoriel);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lienTutoriel.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lien-tutoriels/:id} : Partial updates given fields of an existing lienTutoriel, field will ignore if it is null
     *
     * @param id the id of the lienTutoriel to save.
     * @param lienTutoriel the lienTutoriel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lienTutoriel,
     * or with status {@code 400 (Bad Request)} if the lienTutoriel is not valid,
     * or with status {@code 404 (Not Found)} if the lienTutoriel is not found,
     * or with status {@code 500 (Internal Server Error)} if the lienTutoriel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lien-tutoriels/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LienTutoriel> partialUpdateLienTutoriel(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody LienTutoriel lienTutoriel
    ) throws URISyntaxException {
        log.debug("REST request to partial update LienTutoriel partially : {}, {}", id, lienTutoriel);
        if (lienTutoriel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lienTutoriel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lienTutorielRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LienTutoriel> result = lienTutorielRepository
            .findById(lienTutoriel.getId())
            .map(existingLienTutoriel -> {
                if (lienTutoriel.getDescriptionLien() != null) {
                    existingLienTutoriel.setDescriptionLien(lienTutoriel.getDescriptionLien());
                }
                if (lienTutoriel.getLien() != null) {
                    existingLienTutoriel.setLien(lienTutoriel.getLien());
                }

                return existingLienTutoriel;
            })
            .map(lienTutorielRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lienTutoriel.getId().toString())
        );
    }

    /**
     * {@code GET  /lien-tutoriels} : get all the lienTutoriels.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lienTutoriels in body.
     */
    @GetMapping("/lien-tutoriels")
    public List<LienTutoriel> getAllLienTutoriels(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all LienTutoriels");
        return lienTutorielRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /lien-tutoriels/:id} : get the "id" lienTutoriel.
     *
     * @param id the id of the lienTutoriel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lienTutoriel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lien-tutoriels/{id}")
    public ResponseEntity<LienTutoriel> getLienTutoriel(@PathVariable Long id) {
        log.debug("REST request to get LienTutoriel : {}", id);
        Optional<LienTutoriel> lienTutoriel = lienTutorielRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(lienTutoriel);
    }

    /**
     * {@code DELETE  /lien-tutoriels/:id} : delete the "id" lienTutoriel.
     *
     * @param id the id of the lienTutoriel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lien-tutoriels/{id}")
    public ResponseEntity<Void> deleteLienTutoriel(@PathVariable Long id) {
        log.debug("REST request to delete LienTutoriel : {}", id);
        lienTutorielRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
