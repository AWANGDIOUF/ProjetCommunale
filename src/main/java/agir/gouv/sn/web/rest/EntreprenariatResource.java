package agir.gouv.sn.web.rest;

import agir.gouv.sn.domain.Entreprenariat;
import agir.gouv.sn.repository.EntreprenariatRepository;
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
 * REST controller for managing {@link agir.gouv.sn.domain.Entreprenariat}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EntreprenariatResource {

    private final Logger log = LoggerFactory.getLogger(EntreprenariatResource.class);

    private static final String ENTITY_NAME = "entreprenariat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EntreprenariatRepository entreprenariatRepository;

    public EntreprenariatResource(EntreprenariatRepository entreprenariatRepository) {
        this.entreprenariatRepository = entreprenariatRepository;
    }

    /**
     * {@code POST  /entreprenariats} : Create a new entreprenariat.
     *
     * @param entreprenariat the entreprenariat to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new entreprenariat, or with status {@code 400 (Bad Request)} if the entreprenariat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/entreprenariats")
    public ResponseEntity<Entreprenariat> createEntreprenariat(@RequestBody Entreprenariat entreprenariat) throws URISyntaxException {
        log.debug("REST request to save Entreprenariat : {}", entreprenariat);
        if (entreprenariat.getId() != null) {
            throw new BadRequestAlertException("A new entreprenariat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Entreprenariat result = entreprenariatRepository.save(entreprenariat);
        return ResponseEntity
            .created(new URI("/api/entreprenariats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /entreprenariats/:id} : Updates an existing entreprenariat.
     *
     * @param id the id of the entreprenariat to save.
     * @param entreprenariat the entreprenariat to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entreprenariat,
     * or with status {@code 400 (Bad Request)} if the entreprenariat is not valid,
     * or with status {@code 500 (Internal Server Error)} if the entreprenariat couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/entreprenariats/{id}")
    public ResponseEntity<Entreprenariat> updateEntreprenariat(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Entreprenariat entreprenariat
    ) throws URISyntaxException {
        log.debug("REST request to update Entreprenariat : {}, {}", id, entreprenariat);
        if (entreprenariat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, entreprenariat.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!entreprenariatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Entreprenariat result = entreprenariatRepository.save(entreprenariat);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, entreprenariat.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /entreprenariats/:id} : Partial updates given fields of an existing entreprenariat, field will ignore if it is null
     *
     * @param id the id of the entreprenariat to save.
     * @param entreprenariat the entreprenariat to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entreprenariat,
     * or with status {@code 400 (Bad Request)} if the entreprenariat is not valid,
     * or with status {@code 404 (Not Found)} if the entreprenariat is not found,
     * or with status {@code 500 (Internal Server Error)} if the entreprenariat couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/entreprenariats/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Entreprenariat> partialUpdateEntreprenariat(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Entreprenariat entreprenariat
    ) throws URISyntaxException {
        log.debug("REST request to partial update Entreprenariat partially : {}, {}", id, entreprenariat);
        if (entreprenariat.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, entreprenariat.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!entreprenariatRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Entreprenariat> result = entreprenariatRepository
            .findById(entreprenariat.getId())
            .map(existingEntreprenariat -> {
                if (entreprenariat.getTypeEntre() != null) {
                    existingEntreprenariat.setTypeEntre(entreprenariat.getTypeEntre());
                }
                if (entreprenariat.getAutreEntre() != null) {
                    existingEntreprenariat.setAutreEntre(entreprenariat.getAutreEntre());
                }

                return existingEntreprenariat;
            })
            .map(entreprenariatRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, entreprenariat.getId().toString())
        );
    }

    /**
     * {@code GET  /entreprenariats} : get all the entreprenariats.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of entreprenariats in body.
     */
    @GetMapping("/entreprenariats")
    public List<Entreprenariat> getAllEntreprenariats() {
        log.debug("REST request to get all Entreprenariats");
        return entreprenariatRepository.findAll();
    }

    /**
     * {@code GET  /entreprenariats/:id} : get the "id" entreprenariat.
     *
     * @param id the id of the entreprenariat to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the entreprenariat, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/entreprenariats/{id}")
    public ResponseEntity<Entreprenariat> getEntreprenariat(@PathVariable Long id) {
        log.debug("REST request to get Entreprenariat : {}", id);
        Optional<Entreprenariat> entreprenariat = entreprenariatRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(entreprenariat);
    }

    /**
     * {@code DELETE  /entreprenariats/:id} : delete the "id" entreprenariat.
     *
     * @param id the id of the entreprenariat to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/entreprenariats/{id}")
    public ResponseEntity<Void> deleteEntreprenariat(@PathVariable Long id) {
        log.debug("REST request to delete Entreprenariat : {}", id);
        entreprenariatRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
