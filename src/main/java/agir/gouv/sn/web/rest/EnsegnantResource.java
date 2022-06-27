package agir.gouv.sn.web.rest;

import agir.gouv.sn.domain.Ensegnant;
import agir.gouv.sn.repository.EnsegnantRepository;
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
 * REST controller for managing {@link agir.gouv.sn.domain.Ensegnant}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EnsegnantResource {

    private final Logger log = LoggerFactory.getLogger(EnsegnantResource.class);

    private static final String ENTITY_NAME = "ensegnant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EnsegnantRepository ensegnantRepository;

    public EnsegnantResource(EnsegnantRepository ensegnantRepository) {
        this.ensegnantRepository = ensegnantRepository;
    }

    /**
     * {@code POST  /ensegnants} : Create a new ensegnant.
     *
     * @param ensegnant the ensegnant to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ensegnant, or with status {@code 400 (Bad Request)} if the ensegnant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ensegnants")
    public ResponseEntity<Ensegnant> createEnsegnant(@Valid @RequestBody Ensegnant ensegnant) throws URISyntaxException {
        log.debug("REST request to save Ensegnant : {}", ensegnant);
        if (ensegnant.getId() != null) {
            throw new BadRequestAlertException("A new ensegnant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ensegnant result = ensegnantRepository.save(ensegnant);
        return ResponseEntity
            .created(new URI("/api/ensegnants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ensegnants/:id} : Updates an existing ensegnant.
     *
     * @param id the id of the ensegnant to save.
     * @param ensegnant the ensegnant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ensegnant,
     * or with status {@code 400 (Bad Request)} if the ensegnant is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ensegnant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ensegnants/{id}")
    public ResponseEntity<Ensegnant> updateEnsegnant(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Ensegnant ensegnant
    ) throws URISyntaxException {
        log.debug("REST request to update Ensegnant : {}, {}", id, ensegnant);
        if (ensegnant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ensegnant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ensegnantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Ensegnant result = ensegnantRepository.save(ensegnant);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ensegnant.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ensegnants/:id} : Partial updates given fields of an existing ensegnant, field will ignore if it is null
     *
     * @param id the id of the ensegnant to save.
     * @param ensegnant the ensegnant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ensegnant,
     * or with status {@code 400 (Bad Request)} if the ensegnant is not valid,
     * or with status {@code 404 (Not Found)} if the ensegnant is not found,
     * or with status {@code 500 (Internal Server Error)} if the ensegnant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ensegnants/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Ensegnant> partialUpdateEnsegnant(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Ensegnant ensegnant
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ensegnant partially : {}, {}", id, ensegnant);
        if (ensegnant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ensegnant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ensegnantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Ensegnant> result = ensegnantRepository
            .findById(ensegnant.getId())
            .map(existingEnsegnant -> {
                if (ensegnant.getNom() != null) {
                    existingEnsegnant.setNom(ensegnant.getNom());
                }
                if (ensegnant.getPrenom() != null) {
                    existingEnsegnant.setPrenom(ensegnant.getPrenom());
                }
                if (ensegnant.getEmail() != null) {
                    existingEnsegnant.setEmail(ensegnant.getEmail());
                }
                if (ensegnant.getTel() != null) {
                    existingEnsegnant.setTel(ensegnant.getTel());
                }
                if (ensegnant.getTel1() != null) {
                    existingEnsegnant.setTel1(ensegnant.getTel1());
                }

                return existingEnsegnant;
            })
            .map(ensegnantRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ensegnant.getId().toString())
        );
    }

    /**
     * {@code GET  /ensegnants} : get all the ensegnants.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ensegnants in body.
     */
    @GetMapping("/ensegnants")
    public List<Ensegnant> getAllEnsegnants(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Ensegnants");
        return ensegnantRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /ensegnants/:id} : get the "id" ensegnant.
     *
     * @param id the id of the ensegnant to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ensegnant, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ensegnants/{id}")
    public ResponseEntity<Ensegnant> getEnsegnant(@PathVariable Long id) {
        log.debug("REST request to get Ensegnant : {}", id);
        Optional<Ensegnant> ensegnant = ensegnantRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(ensegnant);
    }

    /**
     * {@code DELETE  /ensegnants/:id} : delete the "id" ensegnant.
     *
     * @param id the id of the ensegnant to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ensegnants/{id}")
    public ResponseEntity<Void> deleteEnsegnant(@PathVariable Long id) {
        log.debug("REST request to delete Ensegnant : {}", id);
        ensegnantRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
