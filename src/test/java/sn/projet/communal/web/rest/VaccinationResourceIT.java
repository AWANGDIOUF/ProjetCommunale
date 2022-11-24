package sn.projet.communal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import sn.projet.communal.IntegrationTest;
import sn.projet.communal.domain.Vaccination;
import sn.projet.communal.repository.VaccinationRepository;

/**
 * Integration tests for the {@link VaccinationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VaccinationResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_DUREE = false;
    private static final Boolean UPDATED_DUREE = true;

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/vaccinations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VaccinationRepository vaccinationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVaccinationMockMvc;

    private Vaccination vaccination;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vaccination createEntity(EntityManager em) {
        Vaccination vaccination = new Vaccination()
            .date(DEFAULT_DATE)
            .description(DEFAULT_DESCRIPTION)
            .duree(DEFAULT_DUREE)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN);
        return vaccination;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vaccination createUpdatedEntity(EntityManager em) {
        Vaccination vaccination = new Vaccination()
            .date(UPDATED_DATE)
            .description(UPDATED_DESCRIPTION)
            .duree(UPDATED_DUREE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN);
        return vaccination;
    }

    @BeforeEach
    public void initTest() {
        vaccination = createEntity(em);
    }

    @Test
    @Transactional
    void createVaccination() throws Exception {
        int databaseSizeBeforeCreate = vaccinationRepository.findAll().size();
        // Create the Vaccination
        restVaccinationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vaccination)))
            .andExpect(status().isCreated());

        // Validate the Vaccination in the database
        List<Vaccination> vaccinationList = vaccinationRepository.findAll();
        assertThat(vaccinationList).hasSize(databaseSizeBeforeCreate + 1);
        Vaccination testVaccination = vaccinationList.get(vaccinationList.size() - 1);
        assertThat(testVaccination.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testVaccination.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testVaccination.getDuree()).isEqualTo(DEFAULT_DUREE);
        assertThat(testVaccination.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testVaccination.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
    }

    @Test
    @Transactional
    void createVaccinationWithExistingId() throws Exception {
        // Create the Vaccination with an existing ID
        vaccination.setId(1L);

        int databaseSizeBeforeCreate = vaccinationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVaccinationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vaccination)))
            .andExpect(status().isBadRequest());

        // Validate the Vaccination in the database
        List<Vaccination> vaccinationList = vaccinationRepository.findAll();
        assertThat(vaccinationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVaccinations() throws Exception {
        // Initialize the database
        vaccinationRepository.saveAndFlush(vaccination);

        // Get all the vaccinationList
        restVaccinationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vaccination.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].duree").value(hasItem(DEFAULT_DUREE.booleanValue())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())));
    }

    @Test
    @Transactional
    void getVaccination() throws Exception {
        // Initialize the database
        vaccinationRepository.saveAndFlush(vaccination);

        // Get the vaccination
        restVaccinationMockMvc
            .perform(get(ENTITY_API_URL_ID, vaccination.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vaccination.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.duree").value(DEFAULT_DUREE.booleanValue()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()));
    }

    @Test
    @Transactional
    void getNonExistingVaccination() throws Exception {
        // Get the vaccination
        restVaccinationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVaccination() throws Exception {
        // Initialize the database
        vaccinationRepository.saveAndFlush(vaccination);

        int databaseSizeBeforeUpdate = vaccinationRepository.findAll().size();

        // Update the vaccination
        Vaccination updatedVaccination = vaccinationRepository.findById(vaccination.getId()).get();
        // Disconnect from session so that the updates on updatedVaccination are not directly saved in db
        em.detach(updatedVaccination);
        updatedVaccination
            .date(UPDATED_DATE)
            .description(UPDATED_DESCRIPTION)
            .duree(UPDATED_DUREE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN);

        restVaccinationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVaccination.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVaccination))
            )
            .andExpect(status().isOk());

        // Validate the Vaccination in the database
        List<Vaccination> vaccinationList = vaccinationRepository.findAll();
        assertThat(vaccinationList).hasSize(databaseSizeBeforeUpdate);
        Vaccination testVaccination = vaccinationList.get(vaccinationList.size() - 1);
        assertThat(testVaccination.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testVaccination.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVaccination.getDuree()).isEqualTo(UPDATED_DUREE);
        assertThat(testVaccination.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testVaccination.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void putNonExistingVaccination() throws Exception {
        int databaseSizeBeforeUpdate = vaccinationRepository.findAll().size();
        vaccination.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVaccinationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vaccination.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vaccination))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vaccination in the database
        List<Vaccination> vaccinationList = vaccinationRepository.findAll();
        assertThat(vaccinationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVaccination() throws Exception {
        int databaseSizeBeforeUpdate = vaccinationRepository.findAll().size();
        vaccination.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccinationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vaccination))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vaccination in the database
        List<Vaccination> vaccinationList = vaccinationRepository.findAll();
        assertThat(vaccinationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVaccination() throws Exception {
        int databaseSizeBeforeUpdate = vaccinationRepository.findAll().size();
        vaccination.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccinationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vaccination)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vaccination in the database
        List<Vaccination> vaccinationList = vaccinationRepository.findAll();
        assertThat(vaccinationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVaccinationWithPatch() throws Exception {
        // Initialize the database
        vaccinationRepository.saveAndFlush(vaccination);

        int databaseSizeBeforeUpdate = vaccinationRepository.findAll().size();

        // Update the vaccination using partial update
        Vaccination partialUpdatedVaccination = new Vaccination();
        partialUpdatedVaccination.setId(vaccination.getId());

        partialUpdatedVaccination
            .date(UPDATED_DATE)
            .description(UPDATED_DESCRIPTION)
            .duree(UPDATED_DUREE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN);

        restVaccinationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVaccination.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVaccination))
            )
            .andExpect(status().isOk());

        // Validate the Vaccination in the database
        List<Vaccination> vaccinationList = vaccinationRepository.findAll();
        assertThat(vaccinationList).hasSize(databaseSizeBeforeUpdate);
        Vaccination testVaccination = vaccinationList.get(vaccinationList.size() - 1);
        assertThat(testVaccination.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testVaccination.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVaccination.getDuree()).isEqualTo(UPDATED_DUREE);
        assertThat(testVaccination.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testVaccination.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void fullUpdateVaccinationWithPatch() throws Exception {
        // Initialize the database
        vaccinationRepository.saveAndFlush(vaccination);

        int databaseSizeBeforeUpdate = vaccinationRepository.findAll().size();

        // Update the vaccination using partial update
        Vaccination partialUpdatedVaccination = new Vaccination();
        partialUpdatedVaccination.setId(vaccination.getId());

        partialUpdatedVaccination
            .date(UPDATED_DATE)
            .description(UPDATED_DESCRIPTION)
            .duree(UPDATED_DUREE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN);

        restVaccinationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVaccination.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVaccination))
            )
            .andExpect(status().isOk());

        // Validate the Vaccination in the database
        List<Vaccination> vaccinationList = vaccinationRepository.findAll();
        assertThat(vaccinationList).hasSize(databaseSizeBeforeUpdate);
        Vaccination testVaccination = vaccinationList.get(vaccinationList.size() - 1);
        assertThat(testVaccination.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testVaccination.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testVaccination.getDuree()).isEqualTo(UPDATED_DUREE);
        assertThat(testVaccination.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testVaccination.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void patchNonExistingVaccination() throws Exception {
        int databaseSizeBeforeUpdate = vaccinationRepository.findAll().size();
        vaccination.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVaccinationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vaccination.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vaccination))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vaccination in the database
        List<Vaccination> vaccinationList = vaccinationRepository.findAll();
        assertThat(vaccinationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVaccination() throws Exception {
        int databaseSizeBeforeUpdate = vaccinationRepository.findAll().size();
        vaccination.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccinationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vaccination))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vaccination in the database
        List<Vaccination> vaccinationList = vaccinationRepository.findAll();
        assertThat(vaccinationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVaccination() throws Exception {
        int databaseSizeBeforeUpdate = vaccinationRepository.findAll().size();
        vaccination.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVaccinationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vaccination))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vaccination in the database
        List<Vaccination> vaccinationList = vaccinationRepository.findAll();
        assertThat(vaccinationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVaccination() throws Exception {
        // Initialize the database
        vaccinationRepository.saveAndFlush(vaccination);

        int databaseSizeBeforeDelete = vaccinationRepository.findAll().size();

        // Delete the vaccination
        restVaccinationMockMvc
            .perform(delete(ENTITY_API_URL_ID, vaccination.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vaccination> vaccinationList = vaccinationRepository.findAll();
        assertThat(vaccinationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
