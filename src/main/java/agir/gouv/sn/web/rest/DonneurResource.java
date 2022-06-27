package agir.gouv.sn.web.rest;

import agir.gouv.sn.domain.Donneur;
import agir.gouv.sn.repository.DonneurRepository;
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
 * REST controller for managing {@link agir.gouv.sn.domain.Donneur}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DonneurResource {

    private final Logger log = LoggerFactory.getLogger(DonneurResource.class);

    private static final String ENTITY_NAME = "donneur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DonneurRepository donneurRepository;

    public DonneurResource(DonneurRepository donneurRepository) {
        this.donneurRepository = donneurRepository;
    }

    /**
     * {@code POST  /donneurs} : Create a new donneur.
     *
     * @param donneur the donneur to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new donneur, or with status {@code 400 (Bad Request)} if the donneur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/donneurs")
    public ResponseEntity<Donneur> createDonneur(@Valid @RequestBody Donneur donneur) throws URISyntaxException {
        log.debug("REST request to save Donneur : {}", donneur);
        if (donneur.getId() != null) {
            throw new BadRequestAlertException("A new donneur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Donneur result = donneurRepository.save(donneur);
        return ResponseEntity
            .created(new URI("/api/donneurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /donneurs/:id} : Updates an existing donneur.
     *
     * @param id the id of the donneur to save.
     * @param donneur the donneur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated donneur,
     * or with status {@code 400 (Bad Request)} if the donneur is not valid,
     * or with status {@code 500 (Internal Server Error)} if the donneur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/donneurs/{id}")
    public ResponseEntity<Donneur> updateDonneur(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Donneur donneur
    ) throws URISyntaxException {
        log.debug("REST request to update Donneur : {}, {}", id, donneur);
        if (donneur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, donneur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!donneurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Donneur result = donneurRepository.save(donneur);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, donneur.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /donneurs/:id} : Partial updates given fields of an existing donneur, field will ignore if it is null
     *
     * @param id the id of the donneur to save.
     * @param donneur the donneur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated donneur,
     * or with status {@code 400 (Bad Request)} if the donneur is not valid,
     * or with status {@code 404 (Not Found)} if the donneur is not found,
     * or with status {@code 500 (Internal Server Error)} if the donneur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/donneurs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Donneur> partialUpdateDonneur(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Donneur donneur
    ) throws URISyntaxException {
        log.debug("REST request to partial update Donneur partially : {}, {}", id, donneur);
        if (donneur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, donneur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!donneurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Donneur> result = donneurRepository
            .findById(donneur.getId())
            .map(existingDonneur -> {
                if (donneur.getTypeDonneur() != null) {
                    existingDonneur.setTypeDonneur(donneur.getTypeDonneur());
                }
                if (donneur.getPrenom() != null) {
                    existingDonneur.setPrenom(donneur.getPrenom());
                }
                if (donneur.getNom() != null) {
                    existingDonneur.setNom(donneur.getNom());
                }
                if (donneur.getEmail() != null) {
                    existingDonneur.setEmail(donneur.getEmail());
                }
                if (donneur.getAdresse() != null) {
                    existingDonneur.setAdresse(donneur.getAdresse());
                }
                if (donneur.getTel1() != null) {
                    existingDonneur.setTel1(donneur.getTel1());
                }
                if (donneur.getVille() != null) {
                    existingDonneur.setVille(donneur.getVille());
                }
                if (donneur.getDescription() != null) {
                    existingDonneur.setDescription(donneur.getDescription());
                }

                return existingDonneur;
            })
            .map(donneurRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, donneur.getId().toString())
        );
    }

    /**
     * {@code GET  /donneurs} : get all the donneurs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of donneurs in body.
     */
    @GetMapping("/donneurs")
    public List<Donneur> getAllDonneurs() {
        log.debug("REST request to get all Donneurs");
        return donneurRepository.findAll();
    }

    /**
     * {@code GET  /donneurs/:id} : get the "id" donneur.
     *
     * @param id the id of the donneur to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the donneur, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/donneurs/{id}")
    public ResponseEntity<Donneur> getDonneur(@PathVariable Long id) {
        log.debug("REST request to get Donneur : {}", id);
        Optional<Donneur> donneur = donneurRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(donneur);
    }

    /**
     * {@code DELETE  /donneurs/:id} : delete the "id" donneur.
     *
     * @param id the id of the donneur to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/donneurs/{id}")
    public ResponseEntity<Void> deleteDonneur(@PathVariable Long id) {
        log.debug("REST request to delete Donneur : {}", id);
        donneurRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
