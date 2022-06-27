package agir.gouv.sn.web.rest;

import agir.gouv.sn.domain.Artiste;
import agir.gouv.sn.repository.ArtisteRepository;
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
 * REST controller for managing {@link agir.gouv.sn.domain.Artiste}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ArtisteResource {

    private final Logger log = LoggerFactory.getLogger(ArtisteResource.class);

    private static final String ENTITY_NAME = "artiste";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ArtisteRepository artisteRepository;

    public ArtisteResource(ArtisteRepository artisteRepository) {
        this.artisteRepository = artisteRepository;
    }

    /**
     * {@code POST  /artistes} : Create a new artiste.
     *
     * @param artiste the artiste to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new artiste, or with status {@code 400 (Bad Request)} if the artiste has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/artistes")
    public ResponseEntity<Artiste> createArtiste(@RequestBody Artiste artiste) throws URISyntaxException {
        log.debug("REST request to save Artiste : {}", artiste);
        if (artiste.getId() != null) {
            throw new BadRequestAlertException("A new artiste cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Artiste result = artisteRepository.save(artiste);
        return ResponseEntity
            .created(new URI("/api/artistes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /artistes/:id} : Updates an existing artiste.
     *
     * @param id the id of the artiste to save.
     * @param artiste the artiste to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated artiste,
     * or with status {@code 400 (Bad Request)} if the artiste is not valid,
     * or with status {@code 500 (Internal Server Error)} if the artiste couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/artistes/{id}")
    public ResponseEntity<Artiste> updateArtiste(@PathVariable(value = "id", required = false) final Long id, @RequestBody Artiste artiste)
        throws URISyntaxException {
        log.debug("REST request to update Artiste : {}, {}", id, artiste);
        if (artiste.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, artiste.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!artisteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Artiste result = artisteRepository.save(artiste);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, artiste.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /artistes/:id} : Partial updates given fields of an existing artiste, field will ignore if it is null
     *
     * @param id the id of the artiste to save.
     * @param artiste the artiste to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated artiste,
     * or with status {@code 400 (Bad Request)} if the artiste is not valid,
     * or with status {@code 404 (Not Found)} if the artiste is not found,
     * or with status {@code 500 (Internal Server Error)} if the artiste couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/artistes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Artiste> partialUpdateArtiste(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Artiste artiste
    ) throws URISyntaxException {
        log.debug("REST request to partial update Artiste partially : {}, {}", id, artiste);
        if (artiste.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, artiste.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!artisteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Artiste> result = artisteRepository
            .findById(artiste.getId())
            .map(existingArtiste -> {
                if (artiste.getNomArtiste() != null) {
                    existingArtiste.setNomArtiste(artiste.getNomArtiste());
                }
                if (artiste.getPrenomArtiste() != null) {
                    existingArtiste.setPrenomArtiste(artiste.getPrenomArtiste());
                }
                if (artiste.getDomaine() != null) {
                    existingArtiste.setDomaine(artiste.getDomaine());
                }
                if (artiste.getAutreDomaine() != null) {
                    existingArtiste.setAutreDomaine(artiste.getAutreDomaine());
                }

                return existingArtiste;
            })
            .map(artisteRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, artiste.getId().toString())
        );
    }

    /**
     * {@code GET  /artistes} : get all the artistes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of artistes in body.
     */
    @GetMapping("/artistes")
    public List<Artiste> getAllArtistes() {
        log.debug("REST request to get all Artistes");
        return artisteRepository.findAll();
    }

    /**
     * {@code GET  /artistes/:id} : get the "id" artiste.
     *
     * @param id the id of the artiste to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the artiste, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/artistes/{id}")
    public ResponseEntity<Artiste> getArtiste(@PathVariable Long id) {
        log.debug("REST request to get Artiste : {}", id);
        Optional<Artiste> artiste = artisteRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(artiste);
    }

    /**
     * {@code DELETE  /artistes/:id} : delete the "id" artiste.
     *
     * @param id the id of the artiste to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/artistes/{id}")
    public ResponseEntity<Void> deleteArtiste(@PathVariable Long id) {
        log.debug("REST request to delete Artiste : {}", id);
        artisteRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
