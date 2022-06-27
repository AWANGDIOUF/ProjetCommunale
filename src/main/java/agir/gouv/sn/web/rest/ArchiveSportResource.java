package agir.gouv.sn.web.rest;

import agir.gouv.sn.domain.ArchiveSport;
import agir.gouv.sn.repository.ArchiveSportRepository;
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
 * REST controller for managing {@link agir.gouv.sn.domain.ArchiveSport}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ArchiveSportResource {

    private final Logger log = LoggerFactory.getLogger(ArchiveSportResource.class);

    private static final String ENTITY_NAME = "archiveSport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArchiveSportRepository archiveSportRepository;

    public ArchiveSportResource(ArchiveSportRepository archiveSportRepository) {
        this.archiveSportRepository = archiveSportRepository;
    }

    /**
     * {@code POST  /archive-sports} : Create a new archiveSport.
     *
     * @param archiveSport the archiveSport to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new archiveSport, or with status {@code 400 (Bad Request)} if the archiveSport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/archive-sports")
    public ResponseEntity<ArchiveSport> createArchiveSport(@RequestBody ArchiveSport archiveSport) throws URISyntaxException {
        log.debug("REST request to save ArchiveSport : {}", archiveSport);
        if (archiveSport.getId() != null) {
            throw new BadRequestAlertException("A new archiveSport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ArchiveSport result = archiveSportRepository.save(archiveSport);
        return ResponseEntity
            .created(new URI("/api/archive-sports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /archive-sports/:id} : Updates an existing archiveSport.
     *
     * @param id the id of the archiveSport to save.
     * @param archiveSport the archiveSport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated archiveSport,
     * or with status {@code 400 (Bad Request)} if the archiveSport is not valid,
     * or with status {@code 500 (Internal Server Error)} if the archiveSport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/archive-sports/{id}")
    public ResponseEntity<ArchiveSport> updateArchiveSport(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ArchiveSport archiveSport
    ) throws URISyntaxException {
        log.debug("REST request to update ArchiveSport : {}, {}", id, archiveSport);
        if (archiveSport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, archiveSport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!archiveSportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ArchiveSport result = archiveSportRepository.save(archiveSport);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, archiveSport.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /archive-sports/:id} : Partial updates given fields of an existing archiveSport, field will ignore if it is null
     *
     * @param id the id of the archiveSport to save.
     * @param archiveSport the archiveSport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated archiveSport,
     * or with status {@code 400 (Bad Request)} if the archiveSport is not valid,
     * or with status {@code 404 (Not Found)} if the archiveSport is not found,
     * or with status {@code 500 (Internal Server Error)} if the archiveSport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/archive-sports/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ArchiveSport> partialUpdateArchiveSport(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ArchiveSport archiveSport
    ) throws URISyntaxException {
        log.debug("REST request to partial update ArchiveSport partially : {}, {}", id, archiveSport);
        if (archiveSport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, archiveSport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!archiveSportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ArchiveSport> result = archiveSportRepository
            .findById(archiveSport.getId())
            .map(existingArchiveSport -> {
                if (archiveSport.getAnnee() != null) {
                    existingArchiveSport.setAnnee(archiveSport.getAnnee());
                }

                return existingArchiveSport;
            })
            .map(archiveSportRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, archiveSport.getId().toString())
        );
    }

    /**
     * {@code GET  /archive-sports} : get all the archiveSports.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of archiveSports in body.
     */
    @GetMapping("/archive-sports")
    public List<ArchiveSport> getAllArchiveSports(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all ArchiveSports");
        return archiveSportRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /archive-sports/:id} : get the "id" archiveSport.
     *
     * @param id the id of the archiveSport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the archiveSport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/archive-sports/{id}")
    public ResponseEntity<ArchiveSport> getArchiveSport(@PathVariable Long id) {
        log.debug("REST request to get ArchiveSport : {}", id);
        Optional<ArchiveSport> archiveSport = archiveSportRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(archiveSport);
    }

    /**
     * {@code DELETE  /archive-sports/:id} : delete the "id" archiveSport.
     *
     * @param id the id of the archiveSport to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/archive-sports/{id}")
    public ResponseEntity<Void> deleteArchiveSport(@PathVariable Long id) {
        log.debug("REST request to delete ArchiveSport : {}", id);
        archiveSportRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
