package sn.projet.communal.web.rest;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import sn.projet.communal.domain.Beneficiaire;
import sn.projet.communal.repository.BeneficiaireRepository;
import sn.projet.communal.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.projet.communal.domain.Beneficiaire}.
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
    public ResponseEntity<Beneficiaire> createBeneficiaire(@Valid @RequestBody Beneficiaire beneficiaire) throws URISyntaxException {
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
        @Valid @RequestBody Beneficiaire beneficiaire
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
    @PatchMapping(value = "/beneficiaires/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Beneficiaire> partialUpdateBeneficiaire(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Beneficiaire beneficiaire
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
            .map(
                existingBeneficiaire -> {
                    if (beneficiaire.getTypeBenefiaire() != null) {
                        existingBeneficiaire.setTypeBenefiaire(beneficiaire.getTypeBenefiaire());
                    }
                    if (beneficiaire.getTypePersoMoral() != null) {
                        existingBeneficiaire.setTypePersoMoral(beneficiaire.getTypePersoMoral());
                    }
                    if (beneficiaire.getPrenom() != null) {
                        existingBeneficiaire.setPrenom(beneficiaire.getPrenom());
                    }
                    if (beneficiaire.getNom() != null) {
                        existingBeneficiaire.setNom(beneficiaire.getNom());
                    }
                    if (beneficiaire.getCin() != null) {
                        existingBeneficiaire.setCin(beneficiaire.getCin());
                    }
                    if (beneficiaire.getAdresse() != null) {
                        existingBeneficiaire.setAdresse(beneficiaire.getAdresse());
                    }
                    if (beneficiaire.getTel1() != null) {
                        existingBeneficiaire.setTel1(beneficiaire.getTel1());
                    }
                    if (beneficiaire.getAutretel1() != null) {
                        existingBeneficiaire.setAutretel1(beneficiaire.getAutretel1());
                    }
                    if (beneficiaire.getEmailAssociation() != null) {
                        existingBeneficiaire.setEmailAssociation(beneficiaire.getEmailAssociation());
                    }
                    if (beneficiaire.getNomPresident() != null) {
                        existingBeneficiaire.setNomPresident(beneficiaire.getNomPresident());
                    }
                    if (beneficiaire.getDescription() != null) {
                        existingBeneficiaire.setDescription(beneficiaire.getDescription());
                    }

                    return existingBeneficiaire;
                }
            )
            .map(beneficiaireRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, beneficiaire.getId().toString())
        );
    }

    /**
     * {@code GET  /beneficiaires} : get all the beneficiaires.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of beneficiaires in body.
     */
    @GetMapping("/beneficiaires")
    public ResponseEntity<List<Beneficiaire>> getAllBeneficiaires(Pageable pageable) {
        log.debug("REST request to get a page of Beneficiaires");
        Page<Beneficiaire> page = beneficiaireRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
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
        Optional<Beneficiaire> beneficiaire = beneficiaireRepository.findById(id);
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
