package agir.gouv.sn.web.rest;

import agir.gouv.sn.domain.ResultatExamen;
import agir.gouv.sn.repository.ResultatExamenRepository;
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
 * REST controller for managing {@link agir.gouv.sn.domain.ResultatExamen}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ResultatExamenResource {

    private final Logger log = LoggerFactory.getLogger(ResultatExamenResource.class);

    private static final String ENTITY_NAME = "resultatExamen";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResultatExamenRepository resultatExamenRepository;

    public ResultatExamenResource(ResultatExamenRepository resultatExamenRepository) {
        this.resultatExamenRepository = resultatExamenRepository;
    }

    /**
     * {@code POST  /resultat-examen} : Create a new resultatExamen.
     *
     * @param resultatExamen the resultatExamen to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resultatExamen, or with status {@code 400 (Bad Request)} if the resultatExamen has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/resultat-examen")
    public ResponseEntity<ResultatExamen> createResultatExamen(@RequestBody ResultatExamen resultatExamen) throws URISyntaxException {
        log.debug("REST request to save ResultatExamen : {}", resultatExamen);
        if (resultatExamen.getId() != null) {
            throw new BadRequestAlertException("A new resultatExamen cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ResultatExamen result = resultatExamenRepository.save(resultatExamen);
        return ResponseEntity
            .created(new URI("/api/resultat-examen/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /resultat-examen/:id} : Updates an existing resultatExamen.
     *
     * @param id the id of the resultatExamen to save.
     * @param resultatExamen the resultatExamen to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resultatExamen,
     * or with status {@code 400 (Bad Request)} if the resultatExamen is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resultatExamen couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/resultat-examen/{id}")
    public ResponseEntity<ResultatExamen> updateResultatExamen(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ResultatExamen resultatExamen
    ) throws URISyntaxException {
        log.debug("REST request to update ResultatExamen : {}, {}", id, resultatExamen);
        if (resultatExamen.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resultatExamen.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!resultatExamenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ResultatExamen result = resultatExamenRepository.save(resultatExamen);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resultatExamen.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /resultat-examen/:id} : Partial updates given fields of an existing resultatExamen, field will ignore if it is null
     *
     * @param id the id of the resultatExamen to save.
     * @param resultatExamen the resultatExamen to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resultatExamen,
     * or with status {@code 400 (Bad Request)} if the resultatExamen is not valid,
     * or with status {@code 404 (Not Found)} if the resultatExamen is not found,
     * or with status {@code 500 (Internal Server Error)} if the resultatExamen couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/resultat-examen/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ResultatExamen> partialUpdateResultatExamen(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ResultatExamen resultatExamen
    ) throws URISyntaxException {
        log.debug("REST request to partial update ResultatExamen partially : {}, {}", id, resultatExamen);
        if (resultatExamen.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resultatExamen.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!resultatExamenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ResultatExamen> result = resultatExamenRepository
            .findById(resultatExamen.getId())
            .map(existingResultatExamen -> {
                if (resultatExamen.getTypeExament() != null) {
                    existingResultatExamen.setTypeExament(resultatExamen.getTypeExament());
                }
                if (resultatExamen.getAutreExamen() != null) {
                    existingResultatExamen.setAutreExamen(resultatExamen.getAutreExamen());
                }
                if (resultatExamen.getTauxReuissite() != null) {
                    existingResultatExamen.setTauxReuissite(resultatExamen.getTauxReuissite());
                }
                if (resultatExamen.getAnnee() != null) {
                    existingResultatExamen.setAnnee(resultatExamen.getAnnee());
                }

                return existingResultatExamen;
            })
            .map(resultatExamenRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resultatExamen.getId().toString())
        );
    }

    /**
     * {@code GET  /resultat-examen} : get all the resultatExamen.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resultatExamen in body.
     */
    @GetMapping("/resultat-examen")
    public List<ResultatExamen> getAllResultatExamen(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all ResultatExamen");
        return resultatExamenRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /resultat-examen/:id} : get the "id" resultatExamen.
     *
     * @param id the id of the resultatExamen to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resultatExamen, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/resultat-examen/{id}")
    public ResponseEntity<ResultatExamen> getResultatExamen(@PathVariable Long id) {
        log.debug("REST request to get ResultatExamen : {}", id);
        Optional<ResultatExamen> resultatExamen = resultatExamenRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(resultatExamen);
    }

    /**
     * {@code DELETE  /resultat-examen/:id} : delete the "id" resultatExamen.
     *
     * @param id the id of the resultatExamen to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/resultat-examen/{id}")
    public ResponseEntity<Void> deleteResultatExamen(@PathVariable Long id) {
        log.debug("REST request to delete ResultatExamen : {}", id);
        resultatExamenRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
