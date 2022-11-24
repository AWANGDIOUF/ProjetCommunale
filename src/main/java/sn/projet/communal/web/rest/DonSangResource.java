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
import sn.projet.communal.domain.DonSang;
import sn.projet.communal.repository.DonSangRepository;
import sn.projet.communal.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.projet.communal.domain.DonSang}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DonSangResource {

    private final Logger log = LoggerFactory.getLogger(DonSangResource.class);

    private static final String ENTITY_NAME = "donSang";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DonSangRepository donSangRepository;

    public DonSangResource(DonSangRepository donSangRepository) {
        this.donSangRepository = donSangRepository;
    }

    /**
     * {@code POST  /don-sangs} : Create a new donSang.
     *
     * @param donSang the donSang to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new donSang, or with status {@code 400 (Bad Request)} if the donSang has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/don-sangs")
    public ResponseEntity<DonSang> createDonSang(@RequestBody DonSang donSang) throws URISyntaxException {
        log.debug("REST request to save DonSang : {}", donSang);
        if (donSang.getId() != null) {
            throw new BadRequestAlertException("A new donSang cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DonSang result = donSangRepository.save(donSang);
        return ResponseEntity
            .created(new URI("/api/don-sangs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /don-sangs/:id} : Updates an existing donSang.
     *
     * @param id the id of the donSang to save.
     * @param donSang the donSang to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated donSang,
     * or with status {@code 400 (Bad Request)} if the donSang is not valid,
     * or with status {@code 500 (Internal Server Error)} if the donSang couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/don-sangs/{id}")
    public ResponseEntity<DonSang> updateDonSang(@PathVariable(value = "id", required = false) final Long id, @RequestBody DonSang donSang)
        throws URISyntaxException {
        log.debug("REST request to update DonSang : {}, {}", id, donSang);
        if (donSang.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, donSang.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!donSangRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DonSang result = donSangRepository.save(donSang);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, donSang.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /don-sangs/:id} : Partial updates given fields of an existing donSang, field will ignore if it is null
     *
     * @param id the id of the donSang to save.
     * @param donSang the donSang to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated donSang,
     * or with status {@code 400 (Bad Request)} if the donSang is not valid,
     * or with status {@code 404 (Not Found)} if the donSang is not found,
     * or with status {@code 500 (Internal Server Error)} if the donSang couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/don-sangs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DonSang> partialUpdateDonSang(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DonSang donSang
    ) throws URISyntaxException {
        log.debug("REST request to partial update DonSang partially : {}, {}", id, donSang);
        if (donSang.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, donSang.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!donSangRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DonSang> result = donSangRepository
            .findById(donSang.getId())
            .map(
                existingDonSang -> {
                    if (donSang.getOrganisateur() != null) {
                        existingDonSang.setOrganisateur(donSang.getOrganisateur());
                    }
                    if (donSang.getDescription() != null) {
                        existingDonSang.setDescription(donSang.getDescription());
                    }

                    return existingDonSang;
                }
            )
            .map(donSangRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, donSang.getId().toString())
        );
    }

    /**
     * {@code GET  /don-sangs} : get all the donSangs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of donSangs in body.
     */
    @GetMapping("/don-sangs")
    public ResponseEntity<List<DonSang>> getAllDonSangs(Pageable pageable) {
        log.debug("REST request to get a page of DonSangs");
        Page<DonSang> page = donSangRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /don-sangs/:id} : get the "id" donSang.
     *
     * @param id the id of the donSang to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the donSang, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/don-sangs/{id}")
    public ResponseEntity<DonSang> getDonSang(@PathVariable Long id) {
        log.debug("REST request to get DonSang : {}", id);
        Optional<DonSang> donSang = donSangRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(donSang);
    }

    /**
     * {@code DELETE  /don-sangs/:id} : delete the "id" donSang.
     *
     * @param id the id of the donSang to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/don-sangs/{id}")
    public ResponseEntity<Void> deleteDonSang(@PathVariable Long id) {
        log.debug("REST request to delete DonSang : {}", id);
        donSangRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
