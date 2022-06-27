package agir.gouv.sn.web.rest;

import agir.gouv.sn.domain.Combattant;
import agir.gouv.sn.repository.CombattantRepository;
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
 * REST controller for managing {@link agir.gouv.sn.domain.Combattant}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CombattantResource {

    private final Logger log = LoggerFactory.getLogger(CombattantResource.class);

    private static final String ENTITY_NAME = "combattant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CombattantRepository combattantRepository;

    public CombattantResource(CombattantRepository combattantRepository) {
        this.combattantRepository = combattantRepository;
    }

    /**
     * {@code POST  /combattants} : Create a new combattant.
     *
     * @param combattant the combattant to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new combattant, or with status {@code 400 (Bad Request)} if the combattant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/combattants")
    public ResponseEntity<Combattant> createCombattant(@RequestBody Combattant combattant) throws URISyntaxException {
        log.debug("REST request to save Combattant : {}", combattant);
        if (combattant.getId() != null) {
            throw new BadRequestAlertException("A new combattant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Combattant result = combattantRepository.save(combattant);
        return ResponseEntity
            .created(new URI("/api/combattants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /combattants/:id} : Updates an existing combattant.
     *
     * @param id the id of the combattant to save.
     * @param combattant the combattant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated combattant,
     * or with status {@code 400 (Bad Request)} if the combattant is not valid,
     * or with status {@code 500 (Internal Server Error)} if the combattant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/combattants/{id}")
    public ResponseEntity<Combattant> updateCombattant(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Combattant combattant
    ) throws URISyntaxException {
        log.debug("REST request to update Combattant : {}, {}", id, combattant);
        if (combattant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, combattant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!combattantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Combattant result = combattantRepository.save(combattant);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, combattant.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /combattants/:id} : Partial updates given fields of an existing combattant, field will ignore if it is null
     *
     * @param id the id of the combattant to save.
     * @param combattant the combattant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated combattant,
     * or with status {@code 400 (Bad Request)} if the combattant is not valid,
     * or with status {@code 404 (Not Found)} if the combattant is not found,
     * or with status {@code 500 (Internal Server Error)} if the combattant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/combattants/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Combattant> partialUpdateCombattant(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Combattant combattant
    ) throws URISyntaxException {
        log.debug("REST request to partial update Combattant partially : {}, {}", id, combattant);
        if (combattant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, combattant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!combattantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Combattant> result = combattantRepository
            .findById(combattant.getId())
            .map(existingCombattant -> {
                if (combattant.getNomCombattant() != null) {
                    existingCombattant.setNomCombattant(combattant.getNomCombattant());
                }
                if (combattant.getPrenomCombattant() != null) {
                    existingCombattant.setPrenomCombattant(combattant.getPrenomCombattant());
                }
                if (combattant.getDateNaisCombattant() != null) {
                    existingCombattant.setDateNaisCombattant(combattant.getDateNaisCombattant());
                }
                if (combattant.getLieuNaisCombattant() != null) {
                    existingCombattant.setLieuNaisCombattant(combattant.getLieuNaisCombattant());
                }
                if (combattant.getPhotoCombattant() != null) {
                    existingCombattant.setPhotoCombattant(combattant.getPhotoCombattant());
                }
                if (combattant.getPhotoCombattantContentType() != null) {
                    existingCombattant.setPhotoCombattantContentType(combattant.getPhotoCombattantContentType());
                }

                return existingCombattant;
            })
            .map(combattantRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, combattant.getId().toString())
        );
    }

    /**
     * {@code GET  /combattants} : get all the combattants.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of combattants in body.
     */
    @GetMapping("/combattants")
    public List<Combattant> getAllCombattants(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Combattants");
        return combattantRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /combattants/:id} : get the "id" combattant.
     *
     * @param id the id of the combattant to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the combattant, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/combattants/{id}")
    public ResponseEntity<Combattant> getCombattant(@PathVariable Long id) {
        log.debug("REST request to get Combattant : {}", id);
        Optional<Combattant> combattant = combattantRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(combattant);
    }

    /**
     * {@code DELETE  /combattants/:id} : delete the "id" combattant.
     *
     * @param id the id of the combattant to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/combattants/{id}")
    public ResponseEntity<Void> deleteCombattant(@PathVariable Long id) {
        log.debug("REST request to delete Combattant : {}", id);
        combattantRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
