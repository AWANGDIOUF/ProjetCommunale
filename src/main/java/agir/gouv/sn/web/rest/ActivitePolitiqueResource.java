package agir.gouv.sn.web.rest;

import agir.gouv.sn.domain.ActivitePolitique;
import agir.gouv.sn.repository.ActivitePolitiqueRepository;
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
 * REST controller for managing {@link agir.gouv.sn.domain.ActivitePolitique}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ActivitePolitiqueResource {

    private final Logger log = LoggerFactory.getLogger(ActivitePolitiqueResource.class);

    private static final String ENTITY_NAME = "activitePolitique";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ActivitePolitiqueRepository activitePolitiqueRepository;

    public ActivitePolitiqueResource(ActivitePolitiqueRepository activitePolitiqueRepository) {
        this.activitePolitiqueRepository = activitePolitiqueRepository;
    }

    /**
     * {@code POST  /activite-politiques} : Create a new activitePolitique.
     *
     * @param activitePolitique the activitePolitique to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new activitePolitique, or with status {@code 400 (Bad Request)} if the activitePolitique has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/activite-politiques")
    public ResponseEntity<ActivitePolitique> createActivitePolitique(@RequestBody ActivitePolitique activitePolitique)
        throws URISyntaxException {
        log.debug("REST request to save ActivitePolitique : {}", activitePolitique);
        if (activitePolitique.getId() != null) {
            throw new BadRequestAlertException("A new activitePolitique cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ActivitePolitique result = activitePolitiqueRepository.save(activitePolitique);
        return ResponseEntity
            .created(new URI("/api/activite-politiques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /activite-politiques/:id} : Updates an existing activitePolitique.
     *
     * @param id the id of the activitePolitique to save.
     * @param activitePolitique the activitePolitique to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated activitePolitique,
     * or with status {@code 400 (Bad Request)} if the activitePolitique is not valid,
     * or with status {@code 500 (Internal Server Error)} if the activitePolitique couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/activite-politiques/{id}")
    public ResponseEntity<ActivitePolitique> updateActivitePolitique(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ActivitePolitique activitePolitique
    ) throws URISyntaxException {
        log.debug("REST request to update ActivitePolitique : {}, {}", id, activitePolitique);
        if (activitePolitique.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, activitePolitique.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!activitePolitiqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ActivitePolitique result = activitePolitiqueRepository.save(activitePolitique);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, activitePolitique.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /activite-politiques/:id} : Partial updates given fields of an existing activitePolitique, field will ignore if it is null
     *
     * @param id the id of the activitePolitique to save.
     * @param activitePolitique the activitePolitique to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated activitePolitique,
     * or with status {@code 400 (Bad Request)} if the activitePolitique is not valid,
     * or with status {@code 404 (Not Found)} if the activitePolitique is not found,
     * or with status {@code 500 (Internal Server Error)} if the activitePolitique couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/activite-politiques/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ActivitePolitique> partialUpdateActivitePolitique(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ActivitePolitique activitePolitique
    ) throws URISyntaxException {
        log.debug("REST request to partial update ActivitePolitique partially : {}, {}", id, activitePolitique);
        if (activitePolitique.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, activitePolitique.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!activitePolitiqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ActivitePolitique> result = activitePolitiqueRepository
            .findById(activitePolitique.getId())
            .map(existingActivitePolitique -> {
                if (activitePolitique.getTitreActivite() != null) {
                    existingActivitePolitique.setTitreActivite(activitePolitique.getTitreActivite());
                }
                if (activitePolitique.getDescriptionActivite() != null) {
                    existingActivitePolitique.setDescriptionActivite(activitePolitique.getDescriptionActivite());
                }
                if (activitePolitique.getDateDebut() != null) {
                    existingActivitePolitique.setDateDebut(activitePolitique.getDateDebut());
                }
                if (activitePolitique.getDateFin() != null) {
                    existingActivitePolitique.setDateFin(activitePolitique.getDateFin());
                }

                return existingActivitePolitique;
            })
            .map(activitePolitiqueRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, activitePolitique.getId().toString())
        );
    }

    /**
     * {@code GET  /activite-politiques} : get all the activitePolitiques.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of activitePolitiques in body.
     */
    @GetMapping("/activite-politiques")
    public List<ActivitePolitique> getAllActivitePolitiques() {
        log.debug("REST request to get all ActivitePolitiques");
        return activitePolitiqueRepository.findAll();
    }

    /**
     * {@code GET  /activite-politiques/:id} : get the "id" activitePolitique.
     *
     * @param id the id of the activitePolitique to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the activitePolitique, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/activite-politiques/{id}")
    public ResponseEntity<ActivitePolitique> getActivitePolitique(@PathVariable Long id) {
        log.debug("REST request to get ActivitePolitique : {}", id);
        Optional<ActivitePolitique> activitePolitique = activitePolitiqueRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(activitePolitique);
    }

    /**
     * {@code DELETE  /activite-politiques/:id} : delete the "id" activitePolitique.
     *
     * @param id the id of the activitePolitique to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/activite-politiques/{id}")
    public ResponseEntity<Void> deleteActivitePolitique(@PathVariable Long id) {
        log.debug("REST request to delete ActivitePolitique : {}", id);
        activitePolitiqueRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
