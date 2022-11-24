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
import sn.projet.communal.domain.Don;
import sn.projet.communal.repository.DonRepository;
import sn.projet.communal.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.projet.communal.domain.Don}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DonResource {

    private final Logger log = LoggerFactory.getLogger(DonResource.class);

    private static final String ENTITY_NAME = "don";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DonRepository donRepository;

    public DonResource(DonRepository donRepository) {
        this.donRepository = donRepository;
    }

    /**
     * {@code POST  /dons} : Create a new don.
     *
     * @param don the don to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new don, or with status {@code 400 (Bad Request)} if the don has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dons")
    public ResponseEntity<Don> createDon(@RequestBody Don don) throws URISyntaxException {
        log.debug("REST request to save Don : {}", don);
        if (don.getId() != null) {
            throw new BadRequestAlertException("A new don cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Don result = donRepository.save(don);
        return ResponseEntity
            .created(new URI("/api/dons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dons/:id} : Updates an existing don.
     *
     * @param id the id of the don to save.
     * @param don the don to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated don,
     * or with status {@code 400 (Bad Request)} if the don is not valid,
     * or with status {@code 500 (Internal Server Error)} if the don couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dons/{id}")
    public ResponseEntity<Don> updateDon(@PathVariable(value = "id", required = false) final Long id, @RequestBody Don don)
        throws URISyntaxException {
        log.debug("REST request to update Don : {}, {}", id, don);
        if (don.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, don.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!donRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Don result = donRepository.save(don);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, don.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /dons/:id} : Partial updates given fields of an existing don, field will ignore if it is null
     *
     * @param id the id of the don to save.
     * @param don the don to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated don,
     * or with status {@code 400 (Bad Request)} if the don is not valid,
     * or with status {@code 404 (Not Found)} if the don is not found,
     * or with status {@code 500 (Internal Server Error)} if the don couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/dons/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Don> partialUpdateDon(@PathVariable(value = "id", required = false) final Long id, @RequestBody Don don)
        throws URISyntaxException {
        log.debug("REST request to partial update Don partially : {}, {}", id, don);
        if (don.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, don.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!donRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Don> result = donRepository
            .findById(don.getId())
            .map(
                existingDon -> {
                    if (don.getTypeDon() != null) {
                        existingDon.setTypeDon(don.getTypeDon());
                    }
                    if (don.getMontant() != null) {
                        existingDon.setMontant(don.getMontant());
                    }
                    if (don.getDescription() != null) {
                        existingDon.setDescription(don.getDescription());
                    }

                    return existingDon;
                }
            )
            .map(donRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, don.getId().toString())
        );
    }

    /**
     * {@code GET  /dons} : get all the dons.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dons in body.
     */
    @GetMapping("/dons")
    public ResponseEntity<List<Don>> getAllDons(Pageable pageable) {
        log.debug("REST request to get a page of Dons");
        Page<Don> page = donRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /dons/:id} : get the "id" don.
     *
     * @param id the id of the don to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the don, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dons/{id}")
    public ResponseEntity<Don> getDon(@PathVariable Long id) {
        log.debug("REST request to get Don : {}", id);
        Optional<Don> don = donRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(don);
    }

    /**
     * {@code DELETE  /dons/:id} : delete the "id" don.
     *
     * @param id the id of the don to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dons/{id}")
    public ResponseEntity<Void> deleteDon(@PathVariable Long id) {
        log.debug("REST request to delete Don : {}", id);
        donRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
