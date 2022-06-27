package agir.gouv.sn.web.rest;

import agir.gouv.sn.domain.TypeVaccin;
import agir.gouv.sn.repository.TypeVaccinRepository;
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
 * REST controller for managing {@link agir.gouv.sn.domain.TypeVaccin}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TypeVaccinResource {

    private final Logger log = LoggerFactory.getLogger(TypeVaccinResource.class);

    private static final String ENTITY_NAME = "typeVaccin";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeVaccinRepository typeVaccinRepository;

    public TypeVaccinResource(TypeVaccinRepository typeVaccinRepository) {
        this.typeVaccinRepository = typeVaccinRepository;
    }

    /**
     * {@code POST  /type-vaccins} : Create a new typeVaccin.
     *
     * @param typeVaccin the typeVaccin to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeVaccin, or with status {@code 400 (Bad Request)} if the typeVaccin has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-vaccins")
    public ResponseEntity<TypeVaccin> createTypeVaccin(@RequestBody TypeVaccin typeVaccin) throws URISyntaxException {
        log.debug("REST request to save TypeVaccin : {}", typeVaccin);
        if (typeVaccin.getId() != null) {
            throw new BadRequestAlertException("A new typeVaccin cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeVaccin result = typeVaccinRepository.save(typeVaccin);
        return ResponseEntity
            .created(new URI("/api/type-vaccins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-vaccins/:id} : Updates an existing typeVaccin.
     *
     * @param id the id of the typeVaccin to save.
     * @param typeVaccin the typeVaccin to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeVaccin,
     * or with status {@code 400 (Bad Request)} if the typeVaccin is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeVaccin couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-vaccins/{id}")
    public ResponseEntity<TypeVaccin> updateTypeVaccin(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TypeVaccin typeVaccin
    ) throws URISyntaxException {
        log.debug("REST request to update TypeVaccin : {}, {}", id, typeVaccin);
        if (typeVaccin.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeVaccin.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeVaccinRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TypeVaccin result = typeVaccinRepository.save(typeVaccin);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeVaccin.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /type-vaccins/:id} : Partial updates given fields of an existing typeVaccin, field will ignore if it is null
     *
     * @param id the id of the typeVaccin to save.
     * @param typeVaccin the typeVaccin to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeVaccin,
     * or with status {@code 400 (Bad Request)} if the typeVaccin is not valid,
     * or with status {@code 404 (Not Found)} if the typeVaccin is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeVaccin couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/type-vaccins/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TypeVaccin> partialUpdateTypeVaccin(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TypeVaccin typeVaccin
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypeVaccin partially : {}, {}", id, typeVaccin);
        if (typeVaccin.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeVaccin.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeVaccinRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypeVaccin> result = typeVaccinRepository
            .findById(typeVaccin.getId())
            .map(existingTypeVaccin -> {
                if (typeVaccin.getLibelle() != null) {
                    existingTypeVaccin.setLibelle(typeVaccin.getLibelle());
                }
                if (typeVaccin.getObjectif() != null) {
                    existingTypeVaccin.setObjectif(typeVaccin.getObjectif());
                }

                return existingTypeVaccin;
            })
            .map(typeVaccinRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeVaccin.getId().toString())
        );
    }

    /**
     * {@code GET  /type-vaccins} : get all the typeVaccins.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeVaccins in body.
     */
    @GetMapping("/type-vaccins")
    public List<TypeVaccin> getAllTypeVaccins() {
        log.debug("REST request to get all TypeVaccins");
        return typeVaccinRepository.findAll();
    }

    /**
     * {@code GET  /type-vaccins/:id} : get the "id" typeVaccin.
     *
     * @param id the id of the typeVaccin to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeVaccin, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-vaccins/{id}")
    public ResponseEntity<TypeVaccin> getTypeVaccin(@PathVariable Long id) {
        log.debug("REST request to get TypeVaccin : {}", id);
        Optional<TypeVaccin> typeVaccin = typeVaccinRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(typeVaccin);
    }

    /**
     * {@code DELETE  /type-vaccins/:id} : delete the "id" typeVaccin.
     *
     * @param id the id of the typeVaccin to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-vaccins/{id}")
    public ResponseEntity<Void> deleteTypeVaccin(@PathVariable Long id) {
        log.debug("REST request to delete TypeVaccin : {}", id);
        typeVaccinRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
