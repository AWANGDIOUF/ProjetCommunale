package agir.gouv.sn.web.rest;

import agir.gouv.sn.domain.CollecteurOdeur;
import agir.gouv.sn.repository.CollecteurOdeurRepository;
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
 * REST controller for managing {@link agir.gouv.sn.domain.CollecteurOdeur}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CollecteurOdeurResource {

    private final Logger log = LoggerFactory.getLogger(CollecteurOdeurResource.class);

    private static final String ENTITY_NAME = "collecteurOdeur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CollecteurOdeurRepository collecteurOdeurRepository;

    public CollecteurOdeurResource(CollecteurOdeurRepository collecteurOdeurRepository) {
        this.collecteurOdeurRepository = collecteurOdeurRepository;
    }

    /**
     * {@code POST  /collecteur-odeurs} : Create a new collecteurOdeur.
     *
     * @param collecteurOdeur the collecteurOdeur to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new collecteurOdeur, or with status {@code 400 (Bad Request)} if the collecteurOdeur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/collecteur-odeurs")
    public ResponseEntity<CollecteurOdeur> createCollecteurOdeur(@RequestBody CollecteurOdeur collecteurOdeur) throws URISyntaxException {
        log.debug("REST request to save CollecteurOdeur : {}", collecteurOdeur);
        if (collecteurOdeur.getId() != null) {
            throw new BadRequestAlertException("A new collecteurOdeur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CollecteurOdeur result = collecteurOdeurRepository.save(collecteurOdeur);
        return ResponseEntity
            .created(new URI("/api/collecteur-odeurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /collecteur-odeurs/:id} : Updates an existing collecteurOdeur.
     *
     * @param id the id of the collecteurOdeur to save.
     * @param collecteurOdeur the collecteurOdeur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated collecteurOdeur,
     * or with status {@code 400 (Bad Request)} if the collecteurOdeur is not valid,
     * or with status {@code 500 (Internal Server Error)} if the collecteurOdeur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/collecteur-odeurs/{id}")
    public ResponseEntity<CollecteurOdeur> updateCollecteurOdeur(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CollecteurOdeur collecteurOdeur
    ) throws URISyntaxException {
        log.debug("REST request to update CollecteurOdeur : {}, {}", id, collecteurOdeur);
        if (collecteurOdeur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, collecteurOdeur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!collecteurOdeurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CollecteurOdeur result = collecteurOdeurRepository.save(collecteurOdeur);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, collecteurOdeur.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /collecteur-odeurs/:id} : Partial updates given fields of an existing collecteurOdeur, field will ignore if it is null
     *
     * @param id the id of the collecteurOdeur to save.
     * @param collecteurOdeur the collecteurOdeur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated collecteurOdeur,
     * or with status {@code 400 (Bad Request)} if the collecteurOdeur is not valid,
     * or with status {@code 404 (Not Found)} if the collecteurOdeur is not found,
     * or with status {@code 500 (Internal Server Error)} if the collecteurOdeur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/collecteur-odeurs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CollecteurOdeur> partialUpdateCollecteurOdeur(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CollecteurOdeur collecteurOdeur
    ) throws URISyntaxException {
        log.debug("REST request to partial update CollecteurOdeur partially : {}, {}", id, collecteurOdeur);
        if (collecteurOdeur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, collecteurOdeur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!collecteurOdeurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CollecteurOdeur> result = collecteurOdeurRepository
            .findById(collecteurOdeur.getId())
            .map(existingCollecteurOdeur -> {
                if (collecteurOdeur.getNomCollecteur() != null) {
                    existingCollecteurOdeur.setNomCollecteur(collecteurOdeur.getNomCollecteur());
                }
                if (collecteurOdeur.getPrenomCollecteur() != null) {
                    existingCollecteurOdeur.setPrenomCollecteur(collecteurOdeur.getPrenomCollecteur());
                }
                if (collecteurOdeur.getDate() != null) {
                    existingCollecteurOdeur.setDate(collecteurOdeur.getDate());
                }
                if (collecteurOdeur.getTel1() != null) {
                    existingCollecteurOdeur.setTel1(collecteurOdeur.getTel1());
                }

                return existingCollecteurOdeur;
            })
            .map(collecteurOdeurRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, collecteurOdeur.getId().toString())
        );
    }

    /**
     * {@code GET  /collecteur-odeurs} : get all the collecteurOdeurs.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of collecteurOdeurs in body.
     */
    @GetMapping("/collecteur-odeurs")
    public List<CollecteurOdeur> getAllCollecteurOdeurs(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all CollecteurOdeurs");
        return collecteurOdeurRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /collecteur-odeurs/:id} : get the "id" collecteurOdeur.
     *
     * @param id the id of the collecteurOdeur to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the collecteurOdeur, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/collecteur-odeurs/{id}")
    public ResponseEntity<CollecteurOdeur> getCollecteurOdeur(@PathVariable Long id) {
        log.debug("REST request to get CollecteurOdeur : {}", id);
        Optional<CollecteurOdeur> collecteurOdeur = collecteurOdeurRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(collecteurOdeur);
    }

    /**
     * {@code DELETE  /collecteur-odeurs/:id} : delete the "id" collecteurOdeur.
     *
     * @param id the id of the collecteurOdeur to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/collecteur-odeurs/{id}")
    public ResponseEntity<Void> deleteCollecteurOdeur(@PathVariable Long id) {
        log.debug("REST request to delete CollecteurOdeur : {}", id);
        collecteurOdeurRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
