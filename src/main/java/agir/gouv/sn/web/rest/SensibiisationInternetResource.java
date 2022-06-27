package agir.gouv.sn.web.rest;

import agir.gouv.sn.domain.SensibiisationInternet;
import agir.gouv.sn.repository.SensibiisationInternetRepository;
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
 * REST controller for managing {@link agir.gouv.sn.domain.SensibiisationInternet}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SensibiisationInternetResource {

    private final Logger log = LoggerFactory.getLogger(SensibiisationInternetResource.class);

    private static final String ENTITY_NAME = "sensibiisationInternet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SensibiisationInternetRepository sensibiisationInternetRepository;

    public SensibiisationInternetResource(SensibiisationInternetRepository sensibiisationInternetRepository) {
        this.sensibiisationInternetRepository = sensibiisationInternetRepository;
    }

    /**
     * {@code POST  /sensibiisation-internets} : Create a new sensibiisationInternet.
     *
     * @param sensibiisationInternet the sensibiisationInternet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sensibiisationInternet, or with status {@code 400 (Bad Request)} if the sensibiisationInternet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sensibiisation-internets")
    public ResponseEntity<SensibiisationInternet> createSensibiisationInternet(@RequestBody SensibiisationInternet sensibiisationInternet)
        throws URISyntaxException {
        log.debug("REST request to save SensibiisationInternet : {}", sensibiisationInternet);
        if (sensibiisationInternet.getId() != null) {
            throw new BadRequestAlertException("A new sensibiisationInternet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SensibiisationInternet result = sensibiisationInternetRepository.save(sensibiisationInternet);
        return ResponseEntity
            .created(new URI("/api/sensibiisation-internets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sensibiisation-internets/:id} : Updates an existing sensibiisationInternet.
     *
     * @param id the id of the sensibiisationInternet to save.
     * @param sensibiisationInternet the sensibiisationInternet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sensibiisationInternet,
     * or with status {@code 400 (Bad Request)} if the sensibiisationInternet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sensibiisationInternet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sensibiisation-internets/{id}")
    public ResponseEntity<SensibiisationInternet> updateSensibiisationInternet(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SensibiisationInternet sensibiisationInternet
    ) throws URISyntaxException {
        log.debug("REST request to update SensibiisationInternet : {}, {}", id, sensibiisationInternet);
        if (sensibiisationInternet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sensibiisationInternet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sensibiisationInternetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SensibiisationInternet result = sensibiisationInternetRepository.save(sensibiisationInternet);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sensibiisationInternet.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sensibiisation-internets/:id} : Partial updates given fields of an existing sensibiisationInternet, field will ignore if it is null
     *
     * @param id the id of the sensibiisationInternet to save.
     * @param sensibiisationInternet the sensibiisationInternet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sensibiisationInternet,
     * or with status {@code 400 (Bad Request)} if the sensibiisationInternet is not valid,
     * or with status {@code 404 (Not Found)} if the sensibiisationInternet is not found,
     * or with status {@code 500 (Internal Server Error)} if the sensibiisationInternet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sensibiisation-internets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SensibiisationInternet> partialUpdateSensibiisationInternet(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SensibiisationInternet sensibiisationInternet
    ) throws URISyntaxException {
        log.debug("REST request to partial update SensibiisationInternet partially : {}, {}", id, sensibiisationInternet);
        if (sensibiisationInternet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sensibiisationInternet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sensibiisationInternetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SensibiisationInternet> result = sensibiisationInternetRepository
            .findById(sensibiisationInternet.getId())
            .map(existingSensibiisationInternet -> {
                if (sensibiisationInternet.getTheme() != null) {
                    existingSensibiisationInternet.setTheme(sensibiisationInternet.getTheme());
                }
                if (sensibiisationInternet.getInterdiction() != null) {
                    existingSensibiisationInternet.setInterdiction(sensibiisationInternet.getInterdiction());
                }
                if (sensibiisationInternet.getBonnePratique() != null) {
                    existingSensibiisationInternet.setBonnePratique(sensibiisationInternet.getBonnePratique());
                }

                return existingSensibiisationInternet;
            })
            .map(sensibiisationInternetRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sensibiisationInternet.getId().toString())
        );
    }

    /**
     * {@code GET  /sensibiisation-internets} : get all the sensibiisationInternets.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sensibiisationInternets in body.
     */
    @GetMapping("/sensibiisation-internets")
    public List<SensibiisationInternet> getAllSensibiisationInternets() {
        log.debug("REST request to get all SensibiisationInternets");
        return sensibiisationInternetRepository.findAll();
    }

    /**
     * {@code GET  /sensibiisation-internets/:id} : get the "id" sensibiisationInternet.
     *
     * @param id the id of the sensibiisationInternet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sensibiisationInternet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sensibiisation-internets/{id}")
    public ResponseEntity<SensibiisationInternet> getSensibiisationInternet(@PathVariable Long id) {
        log.debug("REST request to get SensibiisationInternet : {}", id);
        Optional<SensibiisationInternet> sensibiisationInternet = sensibiisationInternetRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(sensibiisationInternet);
    }

    /**
     * {@code DELETE  /sensibiisation-internets/:id} : delete the "id" sensibiisationInternet.
     *
     * @param id the id of the sensibiisationInternet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sensibiisation-internets/{id}")
    public ResponseEntity<Void> deleteSensibiisationInternet(@PathVariable Long id) {
        log.debug("REST request to delete SensibiisationInternet : {}", id);
        sensibiisationInternetRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
