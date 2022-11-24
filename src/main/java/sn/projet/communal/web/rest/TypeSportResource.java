package sn.projet.communal.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
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
import sn.projet.communal.domain.TypeSport;
import sn.projet.communal.repository.TypeSportRepository;
import sn.projet.communal.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link sn.projet.communal.domain.TypeSport}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TypeSportResource {

    private final Logger log = LoggerFactory.getLogger(TypeSportResource.class);

    private static final String ENTITY_NAME = "typeSport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeSportRepository typeSportRepository;

    public TypeSportResource(TypeSportRepository typeSportRepository) {
        this.typeSportRepository = typeSportRepository;
    }

    /**
     * {@code POST  /type-sports} : Create a new typeSport.
     *
     * @param typeSport the typeSport to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeSport, or with status {@code 400 (Bad Request)} if the typeSport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/type-sports")
    public ResponseEntity<TypeSport> createTypeSport(@RequestBody TypeSport typeSport) throws URISyntaxException {
        log.debug("REST request to save TypeSport : {}", typeSport);
        if (typeSport.getId() != null) {
            throw new BadRequestAlertException("A new typeSport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TypeSport result = typeSportRepository.save(typeSport);
        return ResponseEntity
            .created(new URI("/api/type-sports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-sports/:id} : Updates an existing typeSport.
     *
     * @param id the id of the typeSport to save.
     * @param typeSport the typeSport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeSport,
     * or with status {@code 400 (Bad Request)} if the typeSport is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeSport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-sports/{id}")
    public ResponseEntity<TypeSport> updateTypeSport(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TypeSport typeSport
    ) throws URISyntaxException {
        log.debug("REST request to update TypeSport : {}, {}", id, typeSport);
        if (typeSport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeSport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeSportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TypeSport result = typeSportRepository.save(typeSport);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeSport.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /type-sports/:id} : Partial updates given fields of an existing typeSport, field will ignore if it is null
     *
     * @param id the id of the typeSport to save.
     * @param typeSport the typeSport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeSport,
     * or with status {@code 400 (Bad Request)} if the typeSport is not valid,
     * or with status {@code 404 (Not Found)} if the typeSport is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeSport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/type-sports/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TypeSport> partialUpdateTypeSport(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TypeSport typeSport
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypeSport partially : {}, {}", id, typeSport);
        if (typeSport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeSport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeSportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypeSport> result = typeSportRepository
            .findById(typeSport.getId())
            .map(
                existingTypeSport -> {
                    if (typeSport.getSport() != null) {
                        existingTypeSport.setSport(typeSport.getSport());
                    }

                    return existingTypeSport;
                }
            )
            .map(typeSportRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, typeSport.getId().toString())
        );
    }

    /**
     * {@code GET  /type-sports} : get all the typeSports.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeSports in body.
     */
    @GetMapping("/type-sports")
    public ResponseEntity<List<TypeSport>> getAllTypeSports(Pageable pageable, @RequestParam(required = false) String filter) {
        if ("equipe-is-null".equals(filter)) {
            log.debug("REST request to get all TypeSports where equipe is null");
            return new ResponseEntity<>(
                StreamSupport
                    .stream(typeSportRepository.findAll().spliterator(), false)
                    .filter(typeSport -> typeSport.getEquipe() == null)
                    .collect(Collectors.toList()),
                HttpStatus.OK
            );
        }

        if ("club-is-null".equals(filter)) {
            log.debug("REST request to get all TypeSports where club is null");
            return new ResponseEntity<>(
                StreamSupport
                    .stream(typeSportRepository.findAll().spliterator(), false)
                    .filter(typeSport -> typeSport.getClub() == null)
                    .collect(Collectors.toList()),
                HttpStatus.OK
            );
        }
        log.debug("REST request to get a page of TypeSports");
        Page<TypeSport> page = typeSportRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /type-sports/:id} : get the "id" typeSport.
     *
     * @param id the id of the typeSport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeSport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/type-sports/{id}")
    public ResponseEntity<TypeSport> getTypeSport(@PathVariable Long id) {
        log.debug("REST request to get TypeSport : {}", id);
        Optional<TypeSport> typeSport = typeSportRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(typeSport);
    }

    /**
     * {@code DELETE  /type-sports/:id} : delete the "id" typeSport.
     *
     * @param id the id of the typeSport to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/type-sports/{id}")
    public ResponseEntity<Void> deleteTypeSport(@PathVariable Long id) {
        log.debug("REST request to delete TypeSport : {}", id);
        typeSportRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
