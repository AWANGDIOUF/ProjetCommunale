package agir.gouv.sn.web.rest;

import agir.gouv.sn.domain.UtilisationInternet;
import agir.gouv.sn.repository.UtilisationInternetRepository;
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
 * REST controller for managing {@link agir.gouv.sn.domain.UtilisationInternet}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class UtilisationInternetResource {

    private final Logger log = LoggerFactory.getLogger(UtilisationInternetResource.class);

    private static final String ENTITY_NAME = "utilisationInternet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UtilisationInternetRepository utilisationInternetRepository;

    public UtilisationInternetResource(UtilisationInternetRepository utilisationInternetRepository) {
        this.utilisationInternetRepository = utilisationInternetRepository;
    }

    /**
     * {@code POST  /utilisation-internets} : Create a new utilisationInternet.
     *
     * @param utilisationInternet the utilisationInternet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new utilisationInternet, or with status {@code 400 (Bad Request)} if the utilisationInternet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/utilisation-internets")
    public ResponseEntity<UtilisationInternet> createUtilisationInternet(@RequestBody UtilisationInternet utilisationInternet)
        throws URISyntaxException {
        log.debug("REST request to save UtilisationInternet : {}", utilisationInternet);
        if (utilisationInternet.getId() != null) {
            throw new BadRequestAlertException("A new utilisationInternet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UtilisationInternet result = utilisationInternetRepository.save(utilisationInternet);
        return ResponseEntity
            .created(new URI("/api/utilisation-internets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /utilisation-internets/:id} : Updates an existing utilisationInternet.
     *
     * @param id the id of the utilisationInternet to save.
     * @param utilisationInternet the utilisationInternet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated utilisationInternet,
     * or with status {@code 400 (Bad Request)} if the utilisationInternet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the utilisationInternet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/utilisation-internets/{id}")
    public ResponseEntity<UtilisationInternet> updateUtilisationInternet(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UtilisationInternet utilisationInternet
    ) throws URISyntaxException {
        log.debug("REST request to update UtilisationInternet : {}, {}", id, utilisationInternet);
        if (utilisationInternet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, utilisationInternet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!utilisationInternetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UtilisationInternet result = utilisationInternetRepository.save(utilisationInternet);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, utilisationInternet.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /utilisation-internets/:id} : Partial updates given fields of an existing utilisationInternet, field will ignore if it is null
     *
     * @param id the id of the utilisationInternet to save.
     * @param utilisationInternet the utilisationInternet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated utilisationInternet,
     * or with status {@code 400 (Bad Request)} if the utilisationInternet is not valid,
     * or with status {@code 404 (Not Found)} if the utilisationInternet is not found,
     * or with status {@code 500 (Internal Server Error)} if the utilisationInternet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/utilisation-internets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UtilisationInternet> partialUpdateUtilisationInternet(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UtilisationInternet utilisationInternet
    ) throws URISyntaxException {
        log.debug("REST request to partial update UtilisationInternet partially : {}, {}", id, utilisationInternet);
        if (utilisationInternet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, utilisationInternet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!utilisationInternetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UtilisationInternet> result = utilisationInternetRepository
            .findById(utilisationInternet.getId())
            .map(existingUtilisationInternet -> {
                if (utilisationInternet.getProfil() != null) {
                    existingUtilisationInternet.setProfil(utilisationInternet.getProfil());
                }
                if (utilisationInternet.getAutre() != null) {
                    existingUtilisationInternet.setAutre(utilisationInternet.getAutre());
                }
                if (utilisationInternet.getDomaine() != null) {
                    existingUtilisationInternet.setDomaine(utilisationInternet.getDomaine());
                }
                if (utilisationInternet.getDescription() != null) {
                    existingUtilisationInternet.setDescription(utilisationInternet.getDescription());
                }

                return existingUtilisationInternet;
            })
            .map(utilisationInternetRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, utilisationInternet.getId().toString())
        );
    }

    /**
     * {@code GET  /utilisation-internets} : get all the utilisationInternets.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of utilisationInternets in body.
     */
    @GetMapping("/utilisation-internets")
    public List<UtilisationInternet> getAllUtilisationInternets(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all UtilisationInternets");
        return utilisationInternetRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /utilisation-internets/:id} : get the "id" utilisationInternet.
     *
     * @param id the id of the utilisationInternet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the utilisationInternet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/utilisation-internets/{id}")
    public ResponseEntity<UtilisationInternet> getUtilisationInternet(@PathVariable Long id) {
        log.debug("REST request to get UtilisationInternet : {}", id);
        Optional<UtilisationInternet> utilisationInternet = utilisationInternetRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(utilisationInternet);
    }

    /**
     * {@code DELETE  /utilisation-internets/:id} : delete the "id" utilisationInternet.
     *
     * @param id the id of the utilisationInternet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/utilisation-internets/{id}")
    public ResponseEntity<Void> deleteUtilisationInternet(@PathVariable Long id) {
        log.debug("REST request to delete UtilisationInternet : {}", id);
        utilisationInternetRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
