package agir.gouv.sn.web.rest;

import agir.gouv.sn.domain.Cible;
import agir.gouv.sn.repository.CibleRepository;
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
 * REST controller for managing {@link agir.gouv.sn.domain.Cible}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CibleResource {

    private final Logger log = LoggerFactory.getLogger(CibleResource.class);

    private static final String ENTITY_NAME = "cible";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CibleRepository cibleRepository;

    public CibleResource(CibleRepository cibleRepository) {
        this.cibleRepository = cibleRepository;
    }

    /**
     * {@code POST  /cibles} : Create a new cible.
     *
     * @param cible the cible to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cible, or with status {@code 400 (Bad Request)} if the cible has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cibles")
    public ResponseEntity<Cible> createCible(@RequestBody Cible cible) throws URISyntaxException {
        log.debug("REST request to save Cible : {}", cible);
        if (cible.getId() != null) {
            throw new BadRequestAlertException("A new cible cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cible result = cibleRepository.save(cible);
        return ResponseEntity
            .created(new URI("/api/cibles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cibles/:id} : Updates an existing cible.
     *
     * @param id the id of the cible to save.
     * @param cible the cible to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cible,
     * or with status {@code 400 (Bad Request)} if the cible is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cible couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cibles/{id}")
    public ResponseEntity<Cible> updateCible(@PathVariable(value = "id", required = false) final Long id, @RequestBody Cible cible)
        throws URISyntaxException {
        log.debug("REST request to update Cible : {}, {}", id, cible);
        if (cible.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cible.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cibleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Cible result = cibleRepository.save(cible);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cible.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cibles/:id} : Partial updates given fields of an existing cible, field will ignore if it is null
     *
     * @param id the id of the cible to save.
     * @param cible the cible to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cible,
     * or with status {@code 400 (Bad Request)} if the cible is not valid,
     * or with status {@code 404 (Not Found)} if the cible is not found,
     * or with status {@code 500 (Internal Server Error)} if the cible couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cibles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Cible> partialUpdateCible(@PathVariable(value = "id", required = false) final Long id, @RequestBody Cible cible)
        throws URISyntaxException {
        log.debug("REST request to partial update Cible partially : {}, {}", id, cible);
        if (cible.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cible.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cibleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Cible> result = cibleRepository
            .findById(cible.getId())
            .map(existingCible -> {
                if (cible.getCible() != null) {
                    existingCible.setCible(cible.getCible());
                }
                if (cible.getAge() != null) {
                    existingCible.setAge(cible.getAge());
                }

                return existingCible;
            })
            .map(cibleRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cible.getId().toString())
        );
    }

    /**
     * {@code GET  /cibles} : get all the cibles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cibles in body.
     */
    @GetMapping("/cibles")
    public List<Cible> getAllCibles() {
        log.debug("REST request to get all Cibles");
        return cibleRepository.findAll();
    }

    /**
     * {@code GET  /cibles/:id} : get the "id" cible.
     *
     * @param id the id of the cible to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cible, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cibles/{id}")
    public ResponseEntity<Cible> getCible(@PathVariable Long id) {
        log.debug("REST request to get Cible : {}", id);
        Optional<Cible> cible = cibleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(cible);
    }

    /**
     * {@code DELETE  /cibles/:id} : delete the "id" cible.
     *
     * @param id the id of the cible to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cibles/{id}")
    public ResponseEntity<Void> deleteCible(@PathVariable Long id) {
        log.debug("REST request to delete Cible : {}", id);
        cibleRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
