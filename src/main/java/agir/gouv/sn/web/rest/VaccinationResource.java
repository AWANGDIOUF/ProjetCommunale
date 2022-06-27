package agir.gouv.sn.web.rest;

import agir.gouv.sn.domain.Vaccination;
import agir.gouv.sn.repository.VaccinationRepository;
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
 * REST controller for managing {@link agir.gouv.sn.domain.Vaccination}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VaccinationResource {

    private final Logger log = LoggerFactory.getLogger(VaccinationResource.class);

    private static final String ENTITY_NAME = "vaccination";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VaccinationRepository vaccinationRepository;

    public VaccinationResource(VaccinationRepository vaccinationRepository) {
        this.vaccinationRepository = vaccinationRepository;
    }

    /**
     * {@code POST  /vaccinations} : Create a new vaccination.
     *
     * @param vaccination the vaccination to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vaccination, or with status {@code 400 (Bad Request)} if the vaccination has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vaccinations")
    public ResponseEntity<Vaccination> createVaccination(@RequestBody Vaccination vaccination) throws URISyntaxException {
        log.debug("REST request to save Vaccination : {}", vaccination);
        if (vaccination.getId() != null) {
            throw new BadRequestAlertException("A new vaccination cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Vaccination result = vaccinationRepository.save(vaccination);
        return ResponseEntity
            .created(new URI("/api/vaccinations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vaccinations/:id} : Updates an existing vaccination.
     *
     * @param id the id of the vaccination to save.
     * @param vaccination the vaccination to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vaccination,
     * or with status {@code 400 (Bad Request)} if the vaccination is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vaccination couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vaccinations/{id}")
    public ResponseEntity<Vaccination> updateVaccination(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Vaccination vaccination
    ) throws URISyntaxException {
        log.debug("REST request to update Vaccination : {}, {}", id, vaccination);
        if (vaccination.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vaccination.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vaccinationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Vaccination result = vaccinationRepository.save(vaccination);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vaccination.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vaccinations/:id} : Partial updates given fields of an existing vaccination, field will ignore if it is null
     *
     * @param id the id of the vaccination to save.
     * @param vaccination the vaccination to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vaccination,
     * or with status {@code 400 (Bad Request)} if the vaccination is not valid,
     * or with status {@code 404 (Not Found)} if the vaccination is not found,
     * or with status {@code 500 (Internal Server Error)} if the vaccination couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vaccinations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Vaccination> partialUpdateVaccination(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Vaccination vaccination
    ) throws URISyntaxException {
        log.debug("REST request to partial update Vaccination partially : {}, {}", id, vaccination);
        if (vaccination.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vaccination.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vaccinationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Vaccination> result = vaccinationRepository
            .findById(vaccination.getId())
            .map(existingVaccination -> {
                if (vaccination.getDate() != null) {
                    existingVaccination.setDate(vaccination.getDate());
                }
                if (vaccination.getDescription() != null) {
                    existingVaccination.setDescription(vaccination.getDescription());
                }
                if (vaccination.getDuree() != null) {
                    existingVaccination.setDuree(vaccination.getDuree());
                }
                if (vaccination.getDateDebut() != null) {
                    existingVaccination.setDateDebut(vaccination.getDateDebut());
                }
                if (vaccination.getDateFin() != null) {
                    existingVaccination.setDateFin(vaccination.getDateFin());
                }

                return existingVaccination;
            })
            .map(vaccinationRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vaccination.getId().toString())
        );
    }

    /**
     * {@code GET  /vaccinations} : get all the vaccinations.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vaccinations in body.
     */
    @GetMapping("/vaccinations")
    public List<Vaccination> getAllVaccinations(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Vaccinations");
        return vaccinationRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /vaccinations/:id} : get the "id" vaccination.
     *
     * @param id the id of the vaccination to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vaccination, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vaccinations/{id}")
    public ResponseEntity<Vaccination> getVaccination(@PathVariable Long id) {
        log.debug("REST request to get Vaccination : {}", id);
        Optional<Vaccination> vaccination = vaccinationRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(vaccination);
    }

    /**
     * {@code DELETE  /vaccinations/:id} : delete the "id" vaccination.
     *
     * @param id the id of the vaccination to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vaccinations/{id}")
    public ResponseEntity<Void> deleteVaccination(@PathVariable Long id) {
        log.debug("REST request to delete Vaccination : {}", id);
        vaccinationRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
