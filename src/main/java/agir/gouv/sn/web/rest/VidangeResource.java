package agir.gouv.sn.web.rest;

import agir.gouv.sn.domain.Vidange;
import agir.gouv.sn.repository.VidangeRepository;
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
 * REST controller for managing {@link agir.gouv.sn.domain.Vidange}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VidangeResource {

    private final Logger log = LoggerFactory.getLogger(VidangeResource.class);

    private static final String ENTITY_NAME = "vidange";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VidangeRepository vidangeRepository;

    public VidangeResource(VidangeRepository vidangeRepository) {
        this.vidangeRepository = vidangeRepository;
    }

    /**
     * {@code POST  /vidanges} : Create a new vidange.
     *
     * @param vidange the vidange to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vidange, or with status {@code 400 (Bad Request)} if the vidange has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vidanges")
    public ResponseEntity<Vidange> createVidange(@RequestBody Vidange vidange) throws URISyntaxException {
        log.debug("REST request to save Vidange : {}", vidange);
        if (vidange.getId() != null) {
            throw new BadRequestAlertException("A new vidange cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Vidange result = vidangeRepository.save(vidange);
        return ResponseEntity
            .created(new URI("/api/vidanges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vidanges/:id} : Updates an existing vidange.
     *
     * @param id the id of the vidange to save.
     * @param vidange the vidange to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vidange,
     * or with status {@code 400 (Bad Request)} if the vidange is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vidange couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vidanges/{id}")
    public ResponseEntity<Vidange> updateVidange(@PathVariable(value = "id", required = false) final Long id, @RequestBody Vidange vidange)
        throws URISyntaxException {
        log.debug("REST request to update Vidange : {}, {}", id, vidange);
        if (vidange.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vidange.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vidangeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Vidange result = vidangeRepository.save(vidange);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vidange.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vidanges/:id} : Partial updates given fields of an existing vidange, field will ignore if it is null
     *
     * @param id the id of the vidange to save.
     * @param vidange the vidange to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vidange,
     * or with status {@code 400 (Bad Request)} if the vidange is not valid,
     * or with status {@code 404 (Not Found)} if the vidange is not found,
     * or with status {@code 500 (Internal Server Error)} if the vidange couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vidanges/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Vidange> partialUpdateVidange(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Vidange vidange
    ) throws URISyntaxException {
        log.debug("REST request to partial update Vidange partially : {}, {}", id, vidange);
        if (vidange.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vidange.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vidangeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Vidange> result = vidangeRepository
            .findById(vidange.getId())
            .map(existingVidange -> {
                if (vidange.getNomVideur() != null) {
                    existingVidange.setNomVideur(vidange.getNomVideur());
                }
                if (vidange.getPrenomVideur() != null) {
                    existingVidange.setPrenomVideur(vidange.getPrenomVideur());
                }
                if (vidange.getTel1() != null) {
                    existingVidange.setTel1(vidange.getTel1());
                }
                if (vidange.getTel2() != null) {
                    existingVidange.setTel2(vidange.getTel2());
                }

                return existingVidange;
            })
            .map(vidangeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vidange.getId().toString())
        );
    }

    /**
     * {@code GET  /vidanges} : get all the vidanges.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vidanges in body.
     */
    @GetMapping("/vidanges")
    public List<Vidange> getAllVidanges(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Vidanges");
        return vidangeRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /vidanges/:id} : get the "id" vidange.
     *
     * @param id the id of the vidange to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vidange, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vidanges/{id}")
    public ResponseEntity<Vidange> getVidange(@PathVariable Long id) {
        log.debug("REST request to get Vidange : {}", id);
        Optional<Vidange> vidange = vidangeRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(vidange);
    }

    /**
     * {@code DELETE  /vidanges/:id} : delete the "id" vidange.
     *
     * @param id the id of the vidange to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vidanges/{id}")
    public ResponseEntity<Void> deleteVidange(@PathVariable Long id) {
        log.debug("REST request to delete Vidange : {}", id);
        vidangeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
