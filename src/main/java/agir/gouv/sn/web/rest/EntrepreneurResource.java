package agir.gouv.sn.web.rest;

import agir.gouv.sn.domain.Entrepreneur;
import agir.gouv.sn.repository.EntrepreneurRepository;
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
 * REST controller for managing {@link agir.gouv.sn.domain.Entrepreneur}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EntrepreneurResource {

    private final Logger log = LoggerFactory.getLogger(EntrepreneurResource.class);

    private static final String ENTITY_NAME = "entrepreneur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EntrepreneurRepository entrepreneurRepository;

    public EntrepreneurResource(EntrepreneurRepository entrepreneurRepository) {
        this.entrepreneurRepository = entrepreneurRepository;
    }

    /**
     * {@code POST  /entrepreneurs} : Create a new entrepreneur.
     *
     * @param entrepreneur the entrepreneur to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new entrepreneur, or with status {@code 400 (Bad Request)} if the entrepreneur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/entrepreneurs")
    public ResponseEntity<Entrepreneur> createEntrepreneur(@Valid @RequestBody Entrepreneur entrepreneur) throws URISyntaxException {
        log.debug("REST request to save Entrepreneur : {}", entrepreneur);
        if (entrepreneur.getId() != null) {
            throw new BadRequestAlertException("A new entrepreneur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Entrepreneur result = entrepreneurRepository.save(entrepreneur);
        return ResponseEntity
            .created(new URI("/api/entrepreneurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /entrepreneurs/:id} : Updates an existing entrepreneur.
     *
     * @param id the id of the entrepreneur to save.
     * @param entrepreneur the entrepreneur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entrepreneur,
     * or with status {@code 400 (Bad Request)} if the entrepreneur is not valid,
     * or with status {@code 500 (Internal Server Error)} if the entrepreneur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/entrepreneurs/{id}")
    public ResponseEntity<Entrepreneur> updateEntrepreneur(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Entrepreneur entrepreneur
    ) throws URISyntaxException {
        log.debug("REST request to update Entrepreneur : {}, {}", id, entrepreneur);
        if (entrepreneur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, entrepreneur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!entrepreneurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Entrepreneur result = entrepreneurRepository.save(entrepreneur);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, entrepreneur.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /entrepreneurs/:id} : Partial updates given fields of an existing entrepreneur, field will ignore if it is null
     *
     * @param id the id of the entrepreneur to save.
     * @param entrepreneur the entrepreneur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated entrepreneur,
     * or with status {@code 400 (Bad Request)} if the entrepreneur is not valid,
     * or with status {@code 404 (Not Found)} if the entrepreneur is not found,
     * or with status {@code 500 (Internal Server Error)} if the entrepreneur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/entrepreneurs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Entrepreneur> partialUpdateEntrepreneur(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Entrepreneur entrepreneur
    ) throws URISyntaxException {
        log.debug("REST request to partial update Entrepreneur partially : {}, {}", id, entrepreneur);
        if (entrepreneur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, entrepreneur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!entrepreneurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Entrepreneur> result = entrepreneurRepository
            .findById(entrepreneur.getId())
            .map(existingEntrepreneur -> {
                if (entrepreneur.getNomEntrepreneur() != null) {
                    existingEntrepreneur.setNomEntrepreneur(entrepreneur.getNomEntrepreneur());
                }
                if (entrepreneur.getPrenomEntrepreneur() != null) {
                    existingEntrepreneur.setPrenomEntrepreneur(entrepreneur.getPrenomEntrepreneur());
                }
                if (entrepreneur.getEmailEntrepreneur() != null) {
                    existingEntrepreneur.setEmailEntrepreneur(entrepreneur.getEmailEntrepreneur());
                }
                if (entrepreneur.getTelEntrepreneur() != null) {
                    existingEntrepreneur.setTelEntrepreneur(entrepreneur.getTelEntrepreneur());
                }
                if (entrepreneur.getTel1Entrepreneur() != null) {
                    existingEntrepreneur.setTel1Entrepreneur(entrepreneur.getTel1Entrepreneur());
                }

                return existingEntrepreneur;
            })
            .map(entrepreneurRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, entrepreneur.getId().toString())
        );
    }

    /**
     * {@code GET  /entrepreneurs} : get all the entrepreneurs.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of entrepreneurs in body.
     */
    @GetMapping("/entrepreneurs")
    public List<Entrepreneur> getAllEntrepreneurs(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Entrepreneurs");
        return entrepreneurRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /entrepreneurs/:id} : get the "id" entrepreneur.
     *
     * @param id the id of the entrepreneur to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the entrepreneur, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/entrepreneurs/{id}")
    public ResponseEntity<Entrepreneur> getEntrepreneur(@PathVariable Long id) {
        log.debug("REST request to get Entrepreneur : {}", id);
        Optional<Entrepreneur> entrepreneur = entrepreneurRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(entrepreneur);
    }

    /**
     * {@code DELETE  /entrepreneurs/:id} : delete the "id" entrepreneur.
     *
     * @param id the id of the entrepreneur to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/entrepreneurs/{id}")
    public ResponseEntity<Void> deleteEntrepreneur(@PathVariable Long id) {
        log.debug("REST request to delete Entrepreneur : {}", id);
        entrepreneurRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
