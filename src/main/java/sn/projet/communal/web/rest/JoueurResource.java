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
import sn.projet.communal.domain.Joueur;
import sn.projet.communal.repository.JoueurRepository;
import sn.projet.communal.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.projet.communal.domain.Joueur}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class JoueurResource {

    private final Logger log = LoggerFactory.getLogger(JoueurResource.class);

    private static final String ENTITY_NAME = "joueur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JoueurRepository joueurRepository;

    public JoueurResource(JoueurRepository joueurRepository) {
        this.joueurRepository = joueurRepository;
    }

    /**
     * {@code POST  /joueurs} : Create a new joueur.
     *
     * @param joueur the joueur to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new joueur, or with status {@code 400 (Bad Request)} if the joueur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/joueurs")
    public ResponseEntity<Joueur> createJoueur(@RequestBody Joueur joueur) throws URISyntaxException {
        log.debug("REST request to save Joueur : {}", joueur);
        if (joueur.getId() != null) {
            throw new BadRequestAlertException("A new joueur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Joueur result = joueurRepository.save(joueur);
        return ResponseEntity
            .created(new URI("/api/joueurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /joueurs/:id} : Updates an existing joueur.
     *
     * @param id the id of the joueur to save.
     * @param joueur the joueur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated joueur,
     * or with status {@code 400 (Bad Request)} if the joueur is not valid,
     * or with status {@code 500 (Internal Server Error)} if the joueur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/joueurs/{id}")
    public ResponseEntity<Joueur> updateJoueur(@PathVariable(value = "id", required = false) final Long id, @RequestBody Joueur joueur)
        throws URISyntaxException {
        log.debug("REST request to update Joueur : {}, {}", id, joueur);
        if (joueur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, joueur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!joueurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Joueur result = joueurRepository.save(joueur);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, joueur.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /joueurs/:id} : Partial updates given fields of an existing joueur, field will ignore if it is null
     *
     * @param id the id of the joueur to save.
     * @param joueur the joueur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated joueur,
     * or with status {@code 400 (Bad Request)} if the joueur is not valid,
     * or with status {@code 404 (Not Found)} if the joueur is not found,
     * or with status {@code 500 (Internal Server Error)} if the joueur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/joueurs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Joueur> partialUpdateJoueur(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Joueur joueur
    ) throws URISyntaxException {
        log.debug("REST request to partial update Joueur partially : {}, {}", id, joueur);
        if (joueur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, joueur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!joueurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Joueur> result = joueurRepository
            .findById(joueur.getId())
            .map(
                existingJoueur -> {
                    if (joueur.getNom() != null) {
                        existingJoueur.setNom(joueur.getNom());
                    }
                    if (joueur.getPrenom() != null) {
                        existingJoueur.setPrenom(joueur.getPrenom());
                    }
                    if (joueur.getDateNais() != null) {
                        existingJoueur.setDateNais(joueur.getDateNais());
                    }
                    if (joueur.getLieuNais() != null) {
                        existingJoueur.setLieuNais(joueur.getLieuNais());
                    }
                    if (joueur.getPoste() != null) {
                        existingJoueur.setPoste(joueur.getPoste());
                    }
                    if (joueur.getPhoto() != null) {
                        existingJoueur.setPhoto(joueur.getPhoto());
                    }
                    if (joueur.getPhotoContentType() != null) {
                        existingJoueur.setPhotoContentType(joueur.getPhotoContentType());
                    }

                    return existingJoueur;
                }
            )
            .map(joueurRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, joueur.getId().toString())
        );
    }

    /**
     * {@code GET  /joueurs} : get all the joueurs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of joueurs in body.
     */
    @GetMapping("/joueurs")
    public ResponseEntity<List<Joueur>> getAllJoueurs(Pageable pageable) {
        log.debug("REST request to get a page of Joueurs");
        Page<Joueur> page = joueurRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /joueurs/:id} : get the "id" joueur.
     *
     * @param id the id of the joueur to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the joueur, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/joueurs/{id}")
    public ResponseEntity<Joueur> getJoueur(@PathVariable Long id) {
        log.debug("REST request to get Joueur : {}", id);
        Optional<Joueur> joueur = joueurRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(joueur);
    }

    /**
     * {@code DELETE  /joueurs/:id} : delete the "id" joueur.
     *
     * @param id the id of the joueur to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/joueurs/{id}")
    public ResponseEntity<Void> deleteJoueur(@PathVariable Long id) {
        log.debug("REST request to delete Joueur : {}", id);
        joueurRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
