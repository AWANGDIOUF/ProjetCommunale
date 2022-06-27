package agir.gouv.sn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import agir.gouv.sn.IntegrationTest;
import agir.gouv.sn.domain.SensibiisationInternet;
import agir.gouv.sn.repository.SensibiisationInternetRepository;
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
 * Integration tests for the {@link SensibiisationInternetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SensibiisationInternetResourceIT {

    private static final LocalDate DEFAULT_THEME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_THEME = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_INTERDICTION = "AAAAAAAAAA";
    private static final String UPDATED_INTERDICTION = "BBBBBBBBBB";

    private static final String DEFAULT_BONNE_PRATIQUE = "AAAAAAAAAA";
    private static final String UPDATED_BONNE_PRATIQUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sensibiisation-internets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SensibiisationInternetRepository sensibiisationInternetRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSensibiisationInternetMockMvc;

    private SensibiisationInternet sensibiisationInternet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SensibiisationInternet createEntity(EntityManager em) {
        SensibiisationInternet sensibiisationInternet = new SensibiisationInternet()
            .theme(DEFAULT_THEME)
            .interdiction(DEFAULT_INTERDICTION)
            .bonnePratique(DEFAULT_BONNE_PRATIQUE);
        return sensibiisationInternet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SensibiisationInternet createUpdatedEntity(EntityManager em) {
        SensibiisationInternet sensibiisationInternet = new SensibiisationInternet()
            .theme(UPDATED_THEME)
            .interdiction(UPDATED_INTERDICTION)
            .bonnePratique(UPDATED_BONNE_PRATIQUE);
        return sensibiisationInternet;
    }

    @BeforeEach
    public void initTest() {
        sensibiisationInternet = createEntity(em);
    }

    @Test
    @Transactional
    void createSensibiisationInternet() throws Exception {
        int databaseSizeBeforeCreate = sensibiisationInternetRepository.findAll().size();
        // Create the SensibiisationInternet
        restSensibiisationInternetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sensibiisationInternet))
            )
            .andExpect(status().isCreated());

        // Validate the SensibiisationInternet in the database
        List<SensibiisationInternet> sensibiisationInternetList = sensibiisationInternetRepository.findAll();
        assertThat(sensibiisationInternetList).hasSize(databaseSizeBeforeCreate + 1);
        SensibiisationInternet testSensibiisationInternet = sensibiisationInternetList.get(sensibiisationInternetList.size() - 1);
        assertThat(testSensibiisationInternet.getTheme()).isEqualTo(DEFAULT_THEME);
        assertThat(testSensibiisationInternet.getInterdiction()).isEqualTo(DEFAULT_INTERDICTION);
        assertThat(testSensibiisationInternet.getBonnePratique()).isEqualTo(DEFAULT_BONNE_PRATIQUE);
    }

    @Test
    @Transactional
    void createSensibiisationInternetWithExistingId() throws Exception {
        // Create the SensibiisationInternet with an existing ID
        sensibiisationInternet.setId(1L);

        int databaseSizeBeforeCreate = sensibiisationInternetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSensibiisationInternetMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sensibiisationInternet))
            )
            .andExpect(status().isBadRequest());

        // Validate the SensibiisationInternet in the database
        List<SensibiisationInternet> sensibiisationInternetList = sensibiisationInternetRepository.findAll();
        assertThat(sensibiisationInternetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSensibiisationInternets() throws Exception {
        // Initialize the database
        sensibiisationInternetRepository.saveAndFlush(sensibiisationInternet);

        // Get all the sensibiisationInternetList
        restSensibiisationInternetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sensibiisationInternet.getId().intValue())))
            .andExpect(jsonPath("$.[*].theme").value(hasItem(DEFAULT_THEME.toString())))
            .andExpect(jsonPath("$.[*].interdiction").value(hasItem(DEFAULT_INTERDICTION.toString())))
            .andExpect(jsonPath("$.[*].bonnePratique").value(hasItem(DEFAULT_BONNE_PRATIQUE.toString())));
    }

    @Test
    @Transactional
    void getSensibiisationInternet() throws Exception {
        // Initialize the database
        sensibiisationInternetRepository.saveAndFlush(sensibiisationInternet);

        // Get the sensibiisationInternet
        restSensibiisationInternetMockMvc
            .perform(get(ENTITY_API_URL_ID, sensibiisationInternet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sensibiisationInternet.getId().intValue()))
            .andExpect(jsonPath("$.theme").value(DEFAULT_THEME.toString()))
            .andExpect(jsonPath("$.interdiction").value(DEFAULT_INTERDICTION.toString()))
            .andExpect(jsonPath("$.bonnePratique").value(DEFAULT_BONNE_PRATIQUE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSensibiisationInternet() throws Exception {
        // Get the sensibiisationInternet
        restSensibiisationInternetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSensibiisationInternet() throws Exception {
        // Initialize the database
        sensibiisationInternetRepository.saveAndFlush(sensibiisationInternet);

        int databaseSizeBeforeUpdate = sensibiisationInternetRepository.findAll().size();

        // Update the sensibiisationInternet
        SensibiisationInternet updatedSensibiisationInternet = sensibiisationInternetRepository
            .findById(sensibiisationInternet.getId())
            .get();
        // Disconnect from session so that the updates on updatedSensibiisationInternet are not directly saved in db
        em.detach(updatedSensibiisationInternet);
        updatedSensibiisationInternet.theme(UPDATED_THEME).interdiction(UPDATED_INTERDICTION).bonnePratique(UPDATED_BONNE_PRATIQUE);

        restSensibiisationInternetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSensibiisationInternet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSensibiisationInternet))
            )
            .andExpect(status().isOk());

        // Validate the SensibiisationInternet in the database
        List<SensibiisationInternet> sensibiisationInternetList = sensibiisationInternetRepository.findAll();
        assertThat(sensibiisationInternetList).hasSize(databaseSizeBeforeUpdate);
        SensibiisationInternet testSensibiisationInternet = sensibiisationInternetList.get(sensibiisationInternetList.size() - 1);
        assertThat(testSensibiisationInternet.getTheme()).isEqualTo(UPDATED_THEME);
        assertThat(testSensibiisationInternet.getInterdiction()).isEqualTo(UPDATED_INTERDICTION);
        assertThat(testSensibiisationInternet.getBonnePratique()).isEqualTo(UPDATED_BONNE_PRATIQUE);
    }

    @Test
    @Transactional
    void putNonExistingSensibiisationInternet() throws Exception {
        int databaseSizeBeforeUpdate = sensibiisationInternetRepository.findAll().size();
        sensibiisationInternet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSensibiisationInternetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, sensibiisationInternet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sensibiisationInternet))
            )
            .andExpect(status().isBadRequest());

        // Validate the SensibiisationInternet in the database
        List<SensibiisationInternet> sensibiisationInternetList = sensibiisationInternetRepository.findAll();
        assertThat(sensibiisationInternetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSensibiisationInternet() throws Exception {
        int databaseSizeBeforeUpdate = sensibiisationInternetRepository.findAll().size();
        sensibiisationInternet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSensibiisationInternetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sensibiisationInternet))
            )
            .andExpect(status().isBadRequest());

        // Validate the SensibiisationInternet in the database
        List<SensibiisationInternet> sensibiisationInternetList = sensibiisationInternetRepository.findAll();
        assertThat(sensibiisationInternetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSensibiisationInternet() throws Exception {
        int databaseSizeBeforeUpdate = sensibiisationInternetRepository.findAll().size();
        sensibiisationInternet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSensibiisationInternetMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(sensibiisationInternet))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SensibiisationInternet in the database
        List<SensibiisationInternet> sensibiisationInternetList = sensibiisationInternetRepository.findAll();
        assertThat(sensibiisationInternetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSensibiisationInternetWithPatch() throws Exception {
        // Initialize the database
        sensibiisationInternetRepository.saveAndFlush(sensibiisationInternet);

        int databaseSizeBeforeUpdate = sensibiisationInternetRepository.findAll().size();

        // Update the sensibiisationInternet using partial update
        SensibiisationInternet partialUpdatedSensibiisationInternet = new SensibiisationInternet();
        partialUpdatedSensibiisationInternet.setId(sensibiisationInternet.getId());

        partialUpdatedSensibiisationInternet.theme(UPDATED_THEME).interdiction(UPDATED_INTERDICTION).bonnePratique(UPDATED_BONNE_PRATIQUE);

        restSensibiisationInternetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSensibiisationInternet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSensibiisationInternet))
            )
            .andExpect(status().isOk());

        // Validate the SensibiisationInternet in the database
        List<SensibiisationInternet> sensibiisationInternetList = sensibiisationInternetRepository.findAll();
        assertThat(sensibiisationInternetList).hasSize(databaseSizeBeforeUpdate);
        SensibiisationInternet testSensibiisationInternet = sensibiisationInternetList.get(sensibiisationInternetList.size() - 1);
        assertThat(testSensibiisationInternet.getTheme()).isEqualTo(UPDATED_THEME);
        assertThat(testSensibiisationInternet.getInterdiction()).isEqualTo(UPDATED_INTERDICTION);
        assertThat(testSensibiisationInternet.getBonnePratique()).isEqualTo(UPDATED_BONNE_PRATIQUE);
    }

    @Test
    @Transactional
    void fullUpdateSensibiisationInternetWithPatch() throws Exception {
        // Initialize the database
        sensibiisationInternetRepository.saveAndFlush(sensibiisationInternet);

        int databaseSizeBeforeUpdate = sensibiisationInternetRepository.findAll().size();

        // Update the sensibiisationInternet using partial update
        SensibiisationInternet partialUpdatedSensibiisationInternet = new SensibiisationInternet();
        partialUpdatedSensibiisationInternet.setId(sensibiisationInternet.getId());

        partialUpdatedSensibiisationInternet.theme(UPDATED_THEME).interdiction(UPDATED_INTERDICTION).bonnePratique(UPDATED_BONNE_PRATIQUE);

        restSensibiisationInternetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSensibiisationInternet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSensibiisationInternet))
            )
            .andExpect(status().isOk());

        // Validate the SensibiisationInternet in the database
        List<SensibiisationInternet> sensibiisationInternetList = sensibiisationInternetRepository.findAll();
        assertThat(sensibiisationInternetList).hasSize(databaseSizeBeforeUpdate);
        SensibiisationInternet testSensibiisationInternet = sensibiisationInternetList.get(sensibiisationInternetList.size() - 1);
        assertThat(testSensibiisationInternet.getTheme()).isEqualTo(UPDATED_THEME);
        assertThat(testSensibiisationInternet.getInterdiction()).isEqualTo(UPDATED_INTERDICTION);
        assertThat(testSensibiisationInternet.getBonnePratique()).isEqualTo(UPDATED_BONNE_PRATIQUE);
    }

    @Test
    @Transactional
    void patchNonExistingSensibiisationInternet() throws Exception {
        int databaseSizeBeforeUpdate = sensibiisationInternetRepository.findAll().size();
        sensibiisationInternet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSensibiisationInternetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, sensibiisationInternet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sensibiisationInternet))
            )
            .andExpect(status().isBadRequest());

        // Validate the SensibiisationInternet in the database
        List<SensibiisationInternet> sensibiisationInternetList = sensibiisationInternetRepository.findAll();
        assertThat(sensibiisationInternetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSensibiisationInternet() throws Exception {
        int databaseSizeBeforeUpdate = sensibiisationInternetRepository.findAll().size();
        sensibiisationInternet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSensibiisationInternetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sensibiisationInternet))
            )
            .andExpect(status().isBadRequest());

        // Validate the SensibiisationInternet in the database
        List<SensibiisationInternet> sensibiisationInternetList = sensibiisationInternetRepository.findAll();
        assertThat(sensibiisationInternetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSensibiisationInternet() throws Exception {
        int databaseSizeBeforeUpdate = sensibiisationInternetRepository.findAll().size();
        sensibiisationInternet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSensibiisationInternetMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(sensibiisationInternet))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SensibiisationInternet in the database
        List<SensibiisationInternet> sensibiisationInternetList = sensibiisationInternetRepository.findAll();
        assertThat(sensibiisationInternetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSensibiisationInternet() throws Exception {
        // Initialize the database
        sensibiisationInternetRepository.saveAndFlush(sensibiisationInternet);

        int databaseSizeBeforeDelete = sensibiisationInternetRepository.findAll().size();

        // Delete the sensibiisationInternet
        restSensibiisationInternetMockMvc
            .perform(delete(ENTITY_API_URL_ID, sensibiisationInternet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SensibiisationInternet> sensibiisationInternetList = sensibiisationInternetRepository.findAll();
        assertThat(sensibiisationInternetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
