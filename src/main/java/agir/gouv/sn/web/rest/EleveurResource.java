package agir.gouv.sn.web.rest;

import agir.gouv.sn.domain.Eleveur;
import agir.gouv.sn.repository.EleveurRepository;
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
 * REST controller for managing {@link agir.gouv.sn.domain.Eleveur}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EleveurResource {

    private final Logger log = LoggerFactory.getLogger(EleveurResource.class);

    private static final String ENTITY_NAME = "eleveur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EleveurRepository eleveurRepository;

    public EleveurResource(EleveurRepository eleveurRepository) {
        this.eleveurRepository = eleveurRepository;
    }

    /**
     * {@code POST  /eleveurs} : Create a new eleveur.
     *
     * @param eleveur the eleveur to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eleveur, or with status {@code 400 (Bad Request)} if the eleveur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/eleveurs")
    public ResponseEntity<Eleveur> createEleveur(@RequestBody Eleveur eleveur) throws URISyntaxException {
        log.debug("REST request to save Eleveur : {}", eleveur);
        if (eleveur.getId() != null) {
            throw new BadRequestAlertException("A new eleveur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Eleveur result = eleveurRepository.save(eleveur);
        return ResponseEntity
            .created(new URI("/api/eleveurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /eleveurs/:id} : Updates an existing eleveur.
     *
     * @param id the id of the eleveur to save.
     * @param eleveur the eleveur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eleveur,
     * or with status {@code 400 (Bad Request)} if the eleveur is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eleveur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/eleveurs/{id}")
    public ResponseEntity<Eleveur> updateEleveur(@PathVariable(value = "id", required = false) final Long id, @RequestBody Eleveur eleveur)
        throws URISyntaxException {
        log.debug("REST request to update Eleveur : {}, {}", id, eleveur);
        if (eleveur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eleveur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eleveurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Eleveur result = eleveurRepository.save(eleveur);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eleveur.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /eleveurs/:id} : Partial updates given fields of an existing eleveur, field will ignore if it is null
     *
     * @param id the id of the eleveur to save.
     * @param eleveur the eleveur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eleveur,
     * or with status {@code 400 (Bad Request)} if the eleveur is not valid,
     * or with status {@code 404 (Not Found)} if the eleveur is not found,
     * or with status {@code 500 (Internal Server Error)} if the eleveur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/eleveurs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Eleveur> partialUpdateEleveur(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Eleveur eleveur
    ) throws URISyntaxException {
        log.debug("REST request to partial update Eleveur partially : {}, {}", id, eleveur);
        if (eleveur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eleveur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eleveurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Eleveur> result = eleveurRepository
            .findById(eleveur.getId())
            .map(existingEleveur -> {
                if (eleveur.getNomEleveur() != null) {
                    existingEleveur.setNomEleveur(eleveur.getNomEleveur());
                }
                if (eleveur.getPrenomEleveur() != null) {
                    existingEleveur.setPrenomEleveur(eleveur.getPrenomEleveur());
                }
                if (eleveur.getTelEleveur() != null) {
                    existingEleveur.setTelEleveur(eleveur.getTelEleveur());
                }
                if (eleveur.getTel1Eleveur() != null) {
                    existingEleveur.setTel1Eleveur(eleveur.getTel1Eleveur());
                }
                if (eleveur.getAdresse() != null) {
                    existingEleveur.setAdresse(eleveur.getAdresse());
                }
                if (eleveur.getNomElevage() != null) {
                    existingEleveur.setNomElevage(eleveur.getNomElevage());
                }
                if (eleveur.getDescriptionActivite() != null) {
                    existingEleveur.setDescriptionActivite(eleveur.getDescriptionActivite());
                }

                return existingEleveur;
            })
            .map(eleveurRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eleveur.getId().toString())
        );
    }

    /**
     * {@code GET  /eleveurs} : get all the eleveurs.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eleveurs in body.
     */
    @GetMapping("/eleveurs")
    public List<Eleveur> getAllEleveurs(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Eleveurs");
        return eleveurRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /eleveurs/:id} : get the "id" eleveur.
     *
     * @param id the id of the eleveur to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eleveur, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/eleveurs/{id}")
    public ResponseEntity<Eleveur> getEleveur(@PathVariable Long id) {
        log.debug("REST request to get Eleveur : {}", id);
        Optional<Eleveur> eleveur = eleveurRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(eleveur);
    }

    /**
     * {@code DELETE  /eleveurs/:id} : delete the "id" eleveur.
     *
     * @param id the id of the eleveur to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/eleveurs/{id}")
    public ResponseEntity<Void> deleteEleveur(@PathVariable Long id) {
        log.debug("REST request to delete Eleveur : {}", id);
        eleveurRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
