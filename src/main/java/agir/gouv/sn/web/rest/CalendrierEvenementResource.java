package agir.gouv.sn.web.rest;

import agir.gouv.sn.domain.CalendrierEvenement;
import agir.gouv.sn.repository.CalendrierEvenementRepository;
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
 * REST controller for managing {@link agir.gouv.sn.domain.CalendrierEvenement}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CalendrierEvenementResource {

    private final Logger log = LoggerFactory.getLogger(CalendrierEvenementResource.class);

    private static final String ENTITY_NAME = "calendrierEvenement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CalendrierEvenementRepository calendrierEvenementRepository;

    public CalendrierEvenementResource(CalendrierEvenementRepository calendrierEvenementRepository) {
        this.calendrierEvenementRepository = calendrierEvenementRepository;
    }

    /**
     * {@code POST  /calendrier-evenements} : Create a new calendrierEvenement.
     *
     * @param calendrierEvenement the calendrierEvenement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new calendrierEvenement, or with status {@code 400 (Bad Request)} if the calendrierEvenement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/calendrier-evenements")
    public ResponseEntity<CalendrierEvenement> createCalendrierEvenement(@RequestBody CalendrierEvenement calendrierEvenement)
        throws URISyntaxException {
        log.debug("REST request to save CalendrierEvenement : {}", calendrierEvenement);
        if (calendrierEvenement.getId() != null) {
            throw new BadRequestAlertException("A new calendrierEvenement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CalendrierEvenement result = calendrierEvenementRepository.save(calendrierEvenement);
        return ResponseEntity
            .created(new URI("/api/calendrier-evenements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /calendrier-evenements/:id} : Updates an existing calendrierEvenement.
     *
     * @param id the id of the calendrierEvenement to save.
     * @param calendrierEvenement the calendrierEvenement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated calendrierEvenement,
     * or with status {@code 400 (Bad Request)} if the calendrierEvenement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the calendrierEvenement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/calendrier-evenements/{id}")
    public ResponseEntity<CalendrierEvenement> updateCalendrierEvenement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CalendrierEvenement calendrierEvenement
    ) throws URISyntaxException {
        log.debug("REST request to update CalendrierEvenement : {}, {}", id, calendrierEvenement);
        if (calendrierEvenement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, calendrierEvenement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!calendrierEvenementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CalendrierEvenement result = calendrierEvenementRepository.save(calendrierEvenement);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, calendrierEvenement.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /calendrier-evenements/:id} : Partial updates given fields of an existing calendrierEvenement, field will ignore if it is null
     *
     * @param id the id of the calendrierEvenement to save.
     * @param calendrierEvenement the calendrierEvenement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated calendrierEvenement,
     * or with status {@code 400 (Bad Request)} if the calendrierEvenement is not valid,
     * or with status {@code 404 (Not Found)} if the calendrierEvenement is not found,
     * or with status {@code 500 (Internal Server Error)} if the calendrierEvenement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/calendrier-evenements/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CalendrierEvenement> partialUpdateCalendrierEvenement(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CalendrierEvenement calendrierEvenement
    ) throws URISyntaxException {
        log.debug("REST request to partial update CalendrierEvenement partially : {}, {}", id, calendrierEvenement);
        if (calendrierEvenement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, calendrierEvenement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!calendrierEvenementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CalendrierEvenement> result = calendrierEvenementRepository
            .findById(calendrierEvenement.getId())
            .map(existingCalendrierEvenement -> {
                if (calendrierEvenement.getNomEve() != null) {
                    existingCalendrierEvenement.setNomEve(calendrierEvenement.getNomEve());
                }
                if (calendrierEvenement.getBut() != null) {
                    existingCalendrierEvenement.setBut(calendrierEvenement.getBut());
                }
                if (calendrierEvenement.getObjectif() != null) {
                    existingCalendrierEvenement.setObjectif(calendrierEvenement.getObjectif());
                }
                if (calendrierEvenement.getDate() != null) {
                    existingCalendrierEvenement.setDate(calendrierEvenement.getDate());
                }
                if (calendrierEvenement.getLieu() != null) {
                    existingCalendrierEvenement.setLieu(calendrierEvenement.getLieu());
                }

                return existingCalendrierEvenement;
            })
            .map(calendrierEvenementRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, calendrierEvenement.getId().toString())
        );
    }

    /**
     * {@code GET  /calendrier-evenements} : get all the calendrierEvenements.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of calendrierEvenements in body.
     */
    @GetMapping("/calendrier-evenements")
    public List<CalendrierEvenement> getAllCalendrierEvenements() {
        log.debug("REST request to get all CalendrierEvenements");
        return calendrierEvenementRepository.findAll();
    }

    /**
     * {@code GET  /calendrier-evenements/:id} : get the "id" calendrierEvenement.
     *
     * @param id the id of the calendrierEvenement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the calendrierEvenement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/calendrier-evenements/{id}")
    public ResponseEntity<CalendrierEvenement> getCalendrierEvenement(@PathVariable Long id) {
        log.debug("REST request to get CalendrierEvenement : {}", id);
        Optional<CalendrierEvenement> calendrierEvenement = calendrierEvenementRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(calendrierEvenement);
    }

    /**
     * {@code DELETE  /calendrier-evenements/:id} : delete the "id" calendrierEvenement.
     *
     * @param id the id of the calendrierEvenement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/calendrier-evenements/{id}")
    public ResponseEntity<Void> deleteCalendrierEvenement(@PathVariable Long id) {
        log.debug("REST request to delete CalendrierEvenement : {}", id);
        calendrierEvenementRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
