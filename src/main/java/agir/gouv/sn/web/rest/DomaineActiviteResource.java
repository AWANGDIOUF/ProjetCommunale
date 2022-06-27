package agir.gouv.sn.web.rest;

import agir.gouv.sn.domain.DomaineActivite;
import agir.gouv.sn.repository.DomaineActiviteRepository;
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
 * REST controller for managing {@link agir.gouv.sn.domain.DomaineActivite}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DomaineActiviteResource {

    private final Logger log = LoggerFactory.getLogger(DomaineActiviteResource.class);

    private static final String ENTITY_NAME = "domaineActivite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DomaineActiviteRepository domaineActiviteRepository;

    public DomaineActiviteResource(DomaineActiviteRepository domaineActiviteRepository) {
        this.domaineActiviteRepository = domaineActiviteRepository;
    }

    /**
     * {@code POST  /domaine-activites} : Create a new domaineActivite.
     *
     * @param domaineActivite the domaineActivite to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new domaineActivite, or with status {@code 400 (Bad Request)} if the domaineActivite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/domaine-activites")
    public ResponseEntity<DomaineActivite> createDomaineActivite(@RequestBody DomaineActivite domaineActivite) throws URISyntaxException {
        log.debug("REST request to save DomaineActivite : {}", domaineActivite);
        if (domaineActivite.getId() != null) {
            throw new BadRequestAlertException("A new domaineActivite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DomaineActivite result = domaineActiviteRepository.save(domaineActivite);
        return ResponseEntity
            .created(new URI("/api/domaine-activites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /domaine-activites/:id} : Updates an existing domaineActivite.
     *
     * @param id the id of the domaineActivite to save.
     * @param domaineActivite the domaineActivite to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated domaineActivite,
     * or with status {@code 400 (Bad Request)} if the domaineActivite is not valid,
     * or with status {@code 500 (Internal Server Error)} if the domaineActivite couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/domaine-activites/{id}")
    public ResponseEntity<DomaineActivite> updateDomaineActivite(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DomaineActivite domaineActivite
    ) throws URISyntaxException {
        log.debug("REST request to update DomaineActivite : {}, {}", id, domaineActivite);
        if (domaineActivite.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, domaineActivite.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!domaineActiviteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DomaineActivite result = domaineActiviteRepository.save(domaineActivite);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, domaineActivite.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /domaine-activites/:id} : Partial updates given fields of an existing domaineActivite, field will ignore if it is null
     *
     * @param id the id of the domaineActivite to save.
     * @param domaineActivite the domaineActivite to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated domaineActivite,
     * or with status {@code 400 (Bad Request)} if the domaineActivite is not valid,
     * or with status {@code 404 (Not Found)} if the domaineActivite is not found,
     * or with status {@code 500 (Internal Server Error)} if the domaineActivite couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/domaine-activites/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DomaineActivite> partialUpdateDomaineActivite(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DomaineActivite domaineActivite
    ) throws URISyntaxException {
        log.debug("REST request to partial update DomaineActivite partially : {}, {}", id, domaineActivite);
        if (domaineActivite.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, domaineActivite.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!domaineActiviteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DomaineActivite> result = domaineActiviteRepository
            .findById(domaineActivite.getId())
            .map(existingDomaineActivite -> {
                if (domaineActivite.getTypeActivite() != null) {
                    existingDomaineActivite.setTypeActivite(domaineActivite.getTypeActivite());
                }
                if (domaineActivite.getDescription() != null) {
                    existingDomaineActivite.setDescription(domaineActivite.getDescription());
                }
                if (domaineActivite.getDateActivite() != null) {
                    existingDomaineActivite.setDateActivite(domaineActivite.getDateActivite());
                }

                return existingDomaineActivite;
            })
            .map(domaineActiviteRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, domaineActivite.getId().toString())
        );
    }

    /**
     * {@code GET  /domaine-activites} : get all the domaineActivites.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of domaineActivites in body.
     */
    @GetMapping("/domaine-activites")
    public List<DomaineActivite> getAllDomaineActivites() {
        log.debug("REST request to get all DomaineActivites");
        return domaineActiviteRepository.findAll();
    }

    /**
     * {@code GET  /domaine-activites/:id} : get the "id" domaineActivite.
     *
     * @param id the id of the domaineActivite to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the domaineActivite, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/domaine-activites/{id}")
    public ResponseEntity<DomaineActivite> getDomaineActivite(@PathVariable Long id) {
        log.debug("REST request to get DomaineActivite : {}", id);
        Optional<DomaineActivite> domaineActivite = domaineActiviteRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(domaineActivite);
    }

    /**
     * {@code DELETE  /domaine-activites/:id} : delete the "id" domaineActivite.
     *
     * @param id the id of the domaineActivite to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/domaine-activites/{id}")
    public ResponseEntity<Void> deleteDomaineActivite(@PathVariable Long id) {
        log.debug("REST request to delete DomaineActivite : {}", id);
        domaineActiviteRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
