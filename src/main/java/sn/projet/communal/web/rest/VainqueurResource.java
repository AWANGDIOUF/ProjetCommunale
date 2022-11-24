package sn.projet.communal.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
import sn.projet.communal.domain.Vainqueur;
import sn.projet.communal.repository.VainqueurRepository;
import sn.projet.communal.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.projet.communal.domain.Vainqueur}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VainqueurResource {

    private final Logger log = LoggerFactory.getLogger(VainqueurResource.class);

    private static final String ENTITY_NAME = "vainqueur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VainqueurRepository vainqueurRepository;

    public VainqueurResource(VainqueurRepository vainqueurRepository) {
        this.vainqueurRepository = vainqueurRepository;
    }

    /**
     * {@code POST  /vainqueurs} : Create a new vainqueur.
     *
     * @param vainqueur the vainqueur to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vainqueur, or with status {@code 400 (Bad Request)} if the vainqueur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vainqueurs")
    public ResponseEntity<Vainqueur> createVainqueur(@RequestBody Vainqueur vainqueur) throws URISyntaxException {
        log.debug("REST request to save Vainqueur : {}", vainqueur);
        if (vainqueur.getId() != null) {
            throw new BadRequestAlertException("A new vainqueur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Vainqueur result = vainqueurRepository.save(vainqueur);
        return ResponseEntity
            .created(new URI("/api/vainqueurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vainqueurs/:id} : Updates an existing vainqueur.
     *
     * @param id the id of the vainqueur to save.
     * @param vainqueur the vainqueur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vainqueur,
     * or with status {@code 400 (Bad Request)} if the vainqueur is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vainqueur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vainqueurs/{id}")
    public ResponseEntity<Vainqueur> updateVainqueur(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Vainqueur vainqueur
    ) throws URISyntaxException {
        log.debug("REST request to update Vainqueur : {}, {}", id, vainqueur);
        if (vainqueur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vainqueur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vainqueurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Vainqueur result = vainqueurRepository.save(vainqueur);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vainqueur.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vainqueurs/:id} : Partial updates given fields of an existing vainqueur, field will ignore if it is null
     *
     * @param id the id of the vainqueur to save.
     * @param vainqueur the vainqueur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vainqueur,
     * or with status {@code 400 (Bad Request)} if the vainqueur is not valid,
     * or with status {@code 404 (Not Found)} if the vainqueur is not found,
     * or with status {@code 500 (Internal Server Error)} if the vainqueur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vainqueurs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Vainqueur> partialUpdateVainqueur(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Vainqueur vainqueur
    ) throws URISyntaxException {
        log.debug("REST request to partial update Vainqueur partially : {}, {}", id, vainqueur);
        if (vainqueur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vainqueur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vainqueurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Vainqueur> result = vainqueurRepository
            .findById(vainqueur.getId())
            .map(
                existingVainqueur -> {
                    if (vainqueur.getPrix() != null) {
                        existingVainqueur.setPrix(vainqueur.getPrix());
                    }

                    return existingVainqueur;
                }
            )
            .map(vainqueurRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vainqueur.getId().toString())
        );
    }

    /**
     * {@code GET  /vainqueurs} : get all the vainqueurs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vainqueurs in body.
     */
    @GetMapping("/vainqueurs")
    public ResponseEntity<List<Vainqueur>> getAllVainqueurs(Pageable pageable) {
        log.debug("REST request to get a page of Vainqueurs");
        Page<Vainqueur> page = vainqueurRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /vainqueurs/:id} : get the "id" vainqueur.
     *
     * @param id the id of the vainqueur to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vainqueur, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vainqueurs/{id}")
    public ResponseEntity<Vainqueur> getVainqueur(@PathVariable Long id) {
        log.debug("REST request to get Vainqueur : {}", id);
        Optional<Vainqueur> vainqueur = vainqueurRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(vainqueur);
    }

    /**
     * {@code DELETE  /vainqueurs/:id} : delete the "id" vainqueur.
     *
     * @param id the id of the vainqueur to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vainqueurs/{id}")
    public ResponseEntity<Void> deleteVainqueur(@PathVariable Long id) {
        log.debug("REST request to delete Vainqueur : {}", id);
        vainqueurRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
