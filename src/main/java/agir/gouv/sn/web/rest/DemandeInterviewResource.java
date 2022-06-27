package agir.gouv.sn.web.rest;

import agir.gouv.sn.domain.DemandeInterview;
import agir.gouv.sn.repository.DemandeInterviewRepository;
import agir.gouv.sn.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link agir.gouv.sn.domain.DemandeInterview}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DemandeInterviewResource {

    private final Logger log = LoggerFactory.getLogger(DemandeInterviewResource.class);

    private static final String ENTITY_NAME = "demandeInterview";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DemandeInterviewRepository demandeInterviewRepository;

    public DemandeInterviewResource(DemandeInterviewRepository demandeInterviewRepository) {
        this.demandeInterviewRepository = demandeInterviewRepository;
    }

    /**
     * {@code POST  /demande-interviews} : Create a new demandeInterview.
     *
     * @param demandeInterview the demandeInterview to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new demandeInterview, or with status {@code 400 (Bad Request)} if the demandeInterview has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/demande-interviews")
    public ResponseEntity<DemandeInterview> createDemandeInterview(@Valid @RequestBody DemandeInterview demandeInterview)
        throws URISyntaxException {
        log.debug("REST request to save DemandeInterview : {}", demandeInterview);
        if (demandeInterview.getId() != null) {
            throw new BadRequestAlertException("A new demandeInterview cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DemandeInterview result = demandeInterviewRepository.save(demandeInterview);
        return ResponseEntity
            .created(new URI("/api/demande-interviews/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /demande-interviews/:id} : Updates an existing demandeInterview.
     *
     * @param id the id of the demandeInterview to save.
     * @param demandeInterview the demandeInterview to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated demandeInterview,
     * or with status {@code 400 (Bad Request)} if the demandeInterview is not valid,
     * or with status {@code 500 (Internal Server Error)} if the demandeInterview couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/demande-interviews/{id}")
    public ResponseEntity<DemandeInterview> updateDemandeInterview(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DemandeInterview demandeInterview
    ) throws URISyntaxException {
        log.debug("REST request to update DemandeInterview : {}, {}", id, demandeInterview);
        if (demandeInterview.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, demandeInterview.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!demandeInterviewRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DemandeInterview result = demandeInterviewRepository.save(demandeInterview);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, demandeInterview.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /demande-interviews/:id} : Partial updates given fields of an existing demandeInterview, field will ignore if it is null
     *
     * @param id the id of the demandeInterview to save.
     * @param demandeInterview the demandeInterview to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated demandeInterview,
     * or with status {@code 400 (Bad Request)} if the demandeInterview is not valid,
     * or with status {@code 404 (Not Found)} if the demandeInterview is not found,
     * or with status {@code 500 (Internal Server Error)} if the demandeInterview couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/demande-interviews/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DemandeInterview> partialUpdateDemandeInterview(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DemandeInterview demandeInterview
    ) throws URISyntaxException {
        log.debug("REST request to partial update DemandeInterview partially : {}, {}", id, demandeInterview);
        if (demandeInterview.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, demandeInterview.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!demandeInterviewRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DemandeInterview> result = demandeInterviewRepository
            .findById(demandeInterview.getId())
            .map(existingDemandeInterview -> {
                if (demandeInterview.getNomJournaliste() != null) {
                    existingDemandeInterview.setNomJournaliste(demandeInterview.getNomJournaliste());
                }
                if (demandeInterview.getPrenomJournaliste() != null) {
                    existingDemandeInterview.setPrenomJournaliste(demandeInterview.getPrenomJournaliste());
                }
                if (demandeInterview.getNomSociete() != null) {
                    existingDemandeInterview.setNomSociete(demandeInterview.getNomSociete());
                }
                if (demandeInterview.getEmailJournalite() != null) {
                    existingDemandeInterview.setEmailJournalite(demandeInterview.getEmailJournalite());
                }
                if (demandeInterview.getDateInterview() != null) {
                    existingDemandeInterview.setDateInterview(demandeInterview.getDateInterview());
                }
                if (demandeInterview.getEtatDemande() != null) {
                    existingDemandeInterview.setEtatDemande(demandeInterview.getEtatDemande());
                }

                return existingDemandeInterview;
            })
            .map(demandeInterviewRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, demandeInterview.getId().toString())
        );
    }

    /**
     * {@code GET  /demande-interviews} : get all the demandeInterviews.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of demandeInterviews in body.
     */
    @GetMapping("/demande-interviews")
    public List<DemandeInterview> getAllDemandeInterviews(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all DemandeInterviews");
        return demandeInterviewRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /demande-interviews/:id} : get the "id" demandeInterview.
     *
     * @param id the id of the demandeInterview to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the demandeInterview, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/demande-interviews/{id}")
    public ResponseEntity<DemandeInterview> getDemandeInterview(@PathVariable Long id) {
        log.debug("REST request to get DemandeInterview : {}", id);
        Optional<DemandeInterview> demandeInterview = demandeInterviewRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(demandeInterview);
    }

    /**
     * {@code DELETE  /demande-interviews/:id} : delete the "id" demandeInterview.
     *
     * @param id the id of the demandeInterview to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/demande-interviews/{id}")
    public ResponseEntity<Void> deleteDemandeInterview(@PathVariable Long id) {
        log.debug("REST request to delete DemandeInterview : {}", id);
        demandeInterviewRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
