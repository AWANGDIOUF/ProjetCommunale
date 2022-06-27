package agir.gouv.sn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import agir.gouv.sn.IntegrationTest;
import agir.gouv.sn.domain.CalendrierEvenement;
import agir.gouv.sn.repository.CalendrierEvenementRepository;
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

/**
 * Integration tests for the {@link CalendrierEvenementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CalendrierEvenementResourceIT {

    private static final String DEFAULT_NOM_EVE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_EVE = "BBBBBBBBBB";

    private static final String DEFAULT_BUT = "AAAAAAAAAA";
    private static final String UPDATED_BUT = "BBBBBBBBBB";

    private static final String DEFAULT_OBJECTIF = "AAAAAAAAAA";
    private static final String UPDATED_OBJECTIF = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_LIEU = "AAAAAAAAAA";
    private static final String UPDATED_LIEU = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/calendrier-evenements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CalendrierEvenementRepository calendrierEvenementRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCalendrierEvenementMockMvc;

    private CalendrierEvenement calendrierEvenement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CalendrierEvenement createEntity(EntityManager em) {
        CalendrierEvenement calendrierEvenement = new CalendrierEvenement()
            .nomEve(DEFAULT_NOM_EVE)
            .but(DEFAULT_BUT)
            .objectif(DEFAULT_OBJECTIF)
            .date(DEFAULT_DATE)
            .lieu(DEFAULT_LIEU);
        return calendrierEvenement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CalendrierEvenement createUpdatedEntity(EntityManager em) {
        CalendrierEvenement calendrierEvenement = new CalendrierEvenement()
            .nomEve(UPDATED_NOM_EVE)
            .but(UPDATED_BUT)
            .objectif(UPDATED_OBJECTIF)
            .date(UPDATED_DATE)
            .lieu(UPDATED_LIEU);
        return calendrierEvenement;
    }

    @BeforeEach
    public void initTest() {
        calendrierEvenement = createEntity(em);
    }

    @Test
    @Transactional
    void createCalendrierEvenement() throws Exception {
        int databaseSizeBeforeCreate = calendrierEvenementRepository.findAll().size();
        // Create the CalendrierEvenement
        restCalendrierEvenementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calendrierEvenement))
            )
            .andExpect(status().isCreated());

        // Validate the CalendrierEvenement in the database
        List<CalendrierEvenement> calendrierEvenementList = calendrierEvenementRepository.findAll();
        assertThat(calendrierEvenementList).hasSize(databaseSizeBeforeCreate + 1);
        CalendrierEvenement testCalendrierEvenement = calendrierEvenementList.get(calendrierEvenementList.size() - 1);
        assertThat(testCalendrierEvenement.getNomEve()).isEqualTo(DEFAULT_NOM_EVE);
        assertThat(testCalendrierEvenement.getBut()).isEqualTo(DEFAULT_BUT);
        assertThat(testCalendrierEvenement.getObjectif()).isEqualTo(DEFAULT_OBJECTIF);
        assertThat(testCalendrierEvenement.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testCalendrierEvenement.getLieu()).isEqualTo(DEFAULT_LIEU);
    }

    @Test
    @Transactional
    void createCalendrierEvenementWithExistingId() throws Exception {
        // Create the CalendrierEvenement with an existing ID
        calendrierEvenement.setId(1L);

        int databaseSizeBeforeCreate = calendrierEvenementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCalendrierEvenementMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calendrierEvenement))
            )
            .andExpect(status().isBadRequest());

        // Validate the CalendrierEvenement in the database
        List<CalendrierEvenement> calendrierEvenementList = calendrierEvenementRepository.findAll();
        assertThat(calendrierEvenementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCalendrierEvenements() throws Exception {
        // Initialize the database
        calendrierEvenementRepository.saveAndFlush(calendrierEvenement);

        // Get all the calendrierEvenementList
        restCalendrierEvenementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(calendrierEvenement.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomEve").value(hasItem(DEFAULT_NOM_EVE)))
            .andExpect(jsonPath("$.[*].but").value(hasItem(DEFAULT_BUT.toString())))
            .andExpect(jsonPath("$.[*].objectif").value(hasItem(DEFAULT_OBJECTIF.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].lieu").value(hasItem(DEFAULT_LIEU)));
    }

    @Test
    @Transactional
    void getCalendrierEvenement() throws Exception {
        // Initialize the database
        calendrierEvenementRepository.saveAndFlush(calendrierEvenement);

        // Get the calendrierEvenement
        restCalendrierEvenementMockMvc
            .perform(get(ENTITY_API_URL_ID, calendrierEvenement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(calendrierEvenement.getId().intValue()))
            .andExpect(jsonPath("$.nomEve").value(DEFAULT_NOM_EVE))
            .andExpect(jsonPath("$.but").value(DEFAULT_BUT.toString()))
            .andExpect(jsonPath("$.objectif").value(DEFAULT_OBJECTIF.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.lieu").value(DEFAULT_LIEU));
    }

    @Test
    @Transactional
    void getNonExistingCalendrierEvenement() throws Exception {
        // Get the calendrierEvenement
        restCalendrierEvenementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCalendrierEvenement() throws Exception {
        // Initialize the database
        calendrierEvenementRepository.saveAndFlush(calendrierEvenement);

        int databaseSizeBeforeUpdate = calendrierEvenementRepository.findAll().size();

        // Update the calendrierEvenement
        CalendrierEvenement updatedCalendrierEvenement = calendrierEvenementRepository.findById(calendrierEvenement.getId()).get();
        // Disconnect from session so that the updates on updatedCalendrierEvenement are not directly saved in db
        em.detach(updatedCalendrierEvenement);
        updatedCalendrierEvenement
            .nomEve(UPDATED_NOM_EVE)
            .but(UPDATED_BUT)
            .objectif(UPDATED_OBJECTIF)
            .date(UPDATED_DATE)
            .lieu(UPDATED_LIEU);

        restCalendrierEvenementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCalendrierEvenement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCalendrierEvenement))
            )
            .andExpect(status().isOk());

        // Validate the CalendrierEvenement in the database
        List<CalendrierEvenement> calendrierEvenementList = calendrierEvenementRepository.findAll();
        assertThat(calendrierEvenementList).hasSize(databaseSizeBeforeUpdate);
        CalendrierEvenement testCalendrierEvenement = calendrierEvenementList.get(calendrierEvenementList.size() - 1);
        assertThat(testCalendrierEvenement.getNomEve()).isEqualTo(UPDATED_NOM_EVE);
        assertThat(testCalendrierEvenement.getBut()).isEqualTo(UPDATED_BUT);
        assertThat(testCalendrierEvenement.getObjectif()).isEqualTo(UPDATED_OBJECTIF);
        assertThat(testCalendrierEvenement.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testCalendrierEvenement.getLieu()).isEqualTo(UPDATED_LIEU);
    }

    @Test
    @Transactional
    void putNonExistingCalendrierEvenement() throws Exception {
        int databaseSizeBeforeUpdate = calendrierEvenementRepository.findAll().size();
        calendrierEvenement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCalendrierEvenementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, calendrierEvenement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(calendrierEvenement))
            )
            .andExpect(status().isBadRequest());

        // Validate the CalendrierEvenement in the database
        List<CalendrierEvenement> calendrierEvenementList = calendrierEvenementRepository.findAll();
        assertThat(calendrierEvenementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCalendrierEvenement() throws Exception {
        int databaseSizeBeforeUpdate = calendrierEvenementRepository.findAll().size();
        calendrierEvenement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalendrierEvenementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(calendrierEvenement))
            )
            .andExpect(status().isBadRequest());

        // Validate the CalendrierEvenement in the database
        List<CalendrierEvenement> calendrierEvenementList = calendrierEvenementRepository.findAll();
        assertThat(calendrierEvenementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCalendrierEvenement() throws Exception {
        int databaseSizeBeforeUpdate = calendrierEvenementRepository.findAll().size();
        calendrierEvenement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalendrierEvenementMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(calendrierEvenement))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CalendrierEvenement in the database
        List<CalendrierEvenement> calendrierEvenementList = calendrierEvenementRepository.findAll();
        assertThat(calendrierEvenementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCalendrierEvenementWithPatch() throws Exception {
        // Initialize the database
        calendrierEvenementRepository.saveAndFlush(calendrierEvenement);

        int databaseSizeBeforeUpdate = calendrierEvenementRepository.findAll().size();

        // Update the calendrierEvenement using partial update
        CalendrierEvenement partialUpdatedCalendrierEvenement = new CalendrierEvenement();
        partialUpdatedCalendrierEvenement.setId(calendrierEvenement.getId());

        partialUpdatedCalendrierEvenement.nomEve(UPDATED_NOM_EVE).but(UPDATED_BUT).date(UPDATED_DATE).lieu(UPDATED_LIEU);

        restCalendrierEvenementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCalendrierEvenement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCalendrierEvenement))
            )
            .andExpect(status().isOk());

        // Validate the CalendrierEvenement in the database
        List<CalendrierEvenement> calendrierEvenementList = calendrierEvenementRepository.findAll();
        assertThat(calendrierEvenementList).hasSize(databaseSizeBeforeUpdate);
        CalendrierEvenement testCalendrierEvenement = calendrierEvenementList.get(calendrierEvenementList.size() - 1);
        assertThat(testCalendrierEvenement.getNomEve()).isEqualTo(UPDATED_NOM_EVE);
        assertThat(testCalendrierEvenement.getBut()).isEqualTo(UPDATED_BUT);
        assertThat(testCalendrierEvenement.getObjectif()).isEqualTo(DEFAULT_OBJECTIF);
        assertThat(testCalendrierEvenement.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testCalendrierEvenement.getLieu()).isEqualTo(UPDATED_LIEU);
    }

    @Test
    @Transactional
    void fullUpdateCalendrierEvenementWithPatch() throws Exception {
        // Initialize the database
        calendrierEvenementRepository.saveAndFlush(calendrierEvenement);

        int databaseSizeBeforeUpdate = calendrierEvenementRepository.findAll().size();

        // Update the calendrierEvenement using partial update
        CalendrierEvenement partialUpdatedCalendrierEvenement = new CalendrierEvenement();
        partialUpdatedCalendrierEvenement.setId(calendrierEvenement.getId());

        partialUpdatedCalendrierEvenement
            .nomEve(UPDATED_NOM_EVE)
            .but(UPDATED_BUT)
            .objectif(UPDATED_OBJECTIF)
            .date(UPDATED_DATE)
            .lieu(UPDATED_LIEU);

        restCalendrierEvenementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCalendrierEvenement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCalendrierEvenement))
            )
            .andExpect(status().isOk());

        // Validate the CalendrierEvenement in the database
        List<CalendrierEvenement> calendrierEvenementList = calendrierEvenementRepository.findAll();
        assertThat(calendrierEvenementList).hasSize(databaseSizeBeforeUpdate);
        CalendrierEvenement testCalendrierEvenement = calendrierEvenementList.get(calendrierEvenementList.size() - 1);
        assertThat(testCalendrierEvenement.getNomEve()).isEqualTo(UPDATED_NOM_EVE);
        assertThat(testCalendrierEvenement.getBut()).isEqualTo(UPDATED_BUT);
        assertThat(testCalendrierEvenement.getObjectif()).isEqualTo(UPDATED_OBJECTIF);
        assertThat(testCalendrierEvenement.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testCalendrierEvenement.getLieu()).isEqualTo(UPDATED_LIEU);
    }

    @Test
    @Transactional
    void patchNonExistingCalendrierEvenement() throws Exception {
        int databaseSizeBeforeUpdate = calendrierEvenementRepository.findAll().size();
        calendrierEvenement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCalendrierEvenementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, calendrierEvenement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(calendrierEvenement))
            )
            .andExpect(status().isBadRequest());

        // Validate the CalendrierEvenement in the database
        List<CalendrierEvenement> calendrierEvenementList = calendrierEvenementRepository.findAll();
        assertThat(calendrierEvenementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCalendrierEvenement() throws Exception {
        int databaseSizeBeforeUpdate = calendrierEvenementRepository.findAll().size();
        calendrierEvenement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalendrierEvenementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(calendrierEvenement))
            )
            .andExpect(status().isBadRequest());

        // Validate the CalendrierEvenement in the database
        List<CalendrierEvenement> calendrierEvenementList = calendrierEvenementRepository.findAll();
        assertThat(calendrierEvenementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCalendrierEvenement() throws Exception {
        int databaseSizeBeforeUpdate = calendrierEvenementRepository.findAll().size();
        calendrierEvenement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCalendrierEvenementMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(calendrierEvenement))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CalendrierEvenement in the database
        List<CalendrierEvenement> calendrierEvenementList = calendrierEvenementRepository.findAll();
        assertThat(calendrierEvenementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCalendrierEvenement() throws Exception {
        // Initialize the database
        calendrierEvenementRepository.saveAndFlush(calendrierEvenement);

        int databaseSizeBeforeDelete = calendrierEvenementRepository.findAll().size();

        // Delete the calendrierEvenement
        restCalendrierEvenementMockMvc
            .perform(delete(ENTITY_API_URL_ID, calendrierEvenement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CalendrierEvenement> calendrierEvenementList = calendrierEvenementRepository.findAll();
        assertThat(calendrierEvenementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
