package agir.gouv.sn.web.rest;

import agir.gouv.sn.domain.Beneficiaire;
import agir.gouv.sn.repository.BeneficiaireRepository;
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
 * REST controller for managing {@link agir.gouv.sn.domain.Beneficiaire}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class BeneficiaireResource {

    private final Logger log = LoggerFactory.getLogger(BeneficiaireResource.class);

    private static final String ENTITY_NAME = "beneficiaire";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BeneficiaireRepository beneficiaireRepository;

    public BeneficiaireResource(BeneficiaireRepository beneficiaireRepository) {
        this.beneficiaireRepository = beneficiaireRepository;
    }

    /**
     * {@code POST  /beneficiaires} : Create a new beneficiaire.
     *
     * @param beneficiaire the beneficiaire to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new beneficiaire, or with status {@code 400 (Bad Request)} if the beneficiaire has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/beneficiaires")
    public ResponseEntity<Beneficiaire> createBeneficiaire(@RequestBody Beneficiaire beneficiaire) throws URISyntaxException {
        log.debug("REST request to save Beneficiaire : {}", beneficiaire);
        if (beneficiaire.getId() != null) {
            throw new BadRequestAlertException("A new beneficiaire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Beneficiaire result = beneficiaireRepository.save(beneficiaire);
        return ResponseEntity
            .created(new URI("/api/beneficiaires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /beneficiaires/:id} : Updates an existing beneficiaire.
     *
     * @param id the id of the beneficiaire to save.
     * @param beneficiaire the beneficiaire to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated beneficiaire,
     * or with status {@code 400 (Bad Request)} if the beneficiaire is not valid,
     * or with status {@code 500 (Internal Server Error)} if the beneficiaire couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/beneficiaires/{id}")
    public ResponseEntity<Beneficiaire> updateBeneficiaire(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Beneficiaire beneficiaire
    ) throws URISyntaxException {
        log.debug("REST request to update Beneficiaire : {}, {}", id, beneficiaire);
        if (beneficiaire.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, beneficiaire.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!beneficiaireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Beneficiaire result = beneficiaireRepository.save(beneficiaire);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, beneficiaire.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /beneficiaires/:id} : Partial updates given fields of an existing beneficiaire, field will ignore if it is null
     *
     * @param id the id of the beneficiaire to save.
     * @param beneficiaire the beneficiaire to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated beneficiaire,
     * or with status {@code 400 (Bad Request)} if the beneficiaire is not valid,
     * or with status {@code 404 (Not Found)} if the beneficiaire is not found,
     * or with status {@code 500 (Internal Server Error)} if the beneficiaire couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/beneficiaires/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Beneficiaire> partialUpdateBeneficiaire(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Beneficiaire beneficiaire
    ) throws URISyntaxException {
        log.debug("REST request to partial update Beneficiaire partially : {}, {}", id, beneficiaire);
        if (beneficiaire.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, beneficiaire.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!beneficiaireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Beneficiaire> result = beneficiaireRepository
            .findById(beneficiaire.getId())
            .map(existingBeneficiaire -> {
                if (beneficiaire.getTypeBeneficiaire() != null) {
                    existingBeneficiaire.setTypeBeneficiaire(beneficiaire.getTypeBeneficiaire());
                }
                if (beneficiaire.getAutreBeneficiaire() != null) {
                    existingBeneficiaire.setAutreBeneficiaire(beneficiaire.getAutreBeneficiaire());
                }
                if (beneficiaire.getDescription() != null) {
                    existingBeneficiaire.setDescription(beneficiaire.getDescription());
                }
                if (beneficiaire.getJustification() != null) {
                    existingBeneficiaire.setJustification(beneficiaire.getJustification());
                }

                return existingBeneficiaire;
            })
            .map(beneficiaireRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, beneficiaire.getId().toString())
        );
    }

    /**
     * {@code GET  /beneficiaires} : get all the beneficiaires.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of beneficiaires in body.
     */
    @GetMapping("/beneficiaires")
    public List<Beneficiaire> getAllBeneficiaires(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Beneficiaires");
        return beneficiaireRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /beneficiaires/:id} : get the "id" beneficiaire.
     *
     * @param id the id of the beneficiaire to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the beneficiaire, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/beneficiaires/{id}")
    public ResponseEntity<Beneficiaire> getBeneficiaire(@PathVariable Long id) {
        log.debug("REST request to get Beneficiaire : {}", id);
        Optional<Beneficiaire> beneficiaire = beneficiaireRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(beneficiaire);
    }

    /**
     * {@code DELETE  /beneficiaires/:id} : delete the "id" beneficiaire.
     *
     * @param id the id of the beneficiaire to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/beneficiaires/{id}")
    public ResponseEntity<Void> deleteBeneficiaire(@PathVariable Long id) {
        log.debug("REST request to delete Beneficiaire : {}", id);
        beneficiaireRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
