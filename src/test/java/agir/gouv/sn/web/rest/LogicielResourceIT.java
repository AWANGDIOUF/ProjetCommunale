package agir.gouv.sn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import agir.gouv.sn.IntegrationTest;
import agir.gouv.sn.domain.Logiciel;
import agir.gouv.sn.repository.LogicielRepository;
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
 * Integration tests for the {@link LogicielResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LogicielResourceIT {

    private static final String DEFAULT_NOM_LOGICIEL = "AAAAAAAAAA";
    private static final String UPDATED_NOM_LOGICIEL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/logiciels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LogicielRepository logicielRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLogicielMockMvc;

    private Logiciel logiciel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Logiciel createEntity(EntityManager em) {
        Logiciel logiciel = new Logiciel().nomLogiciel(DEFAULT_NOM_LOGICIEL).description(DEFAULT_DESCRIPTION);
        return logiciel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Logiciel createUpdatedEntity(EntityManager em) {
        Logiciel logiciel = new Logiciel().nomLogiciel(UPDATED_NOM_LOGICIEL).description(UPDATED_DESCRIPTION);
        return logiciel;
    }

    @BeforeEach
    public void initTest() {
        logiciel = createEntity(em);
    }

    @Test
    @Transactional
    void createLogiciel() throws Exception {
        int databaseSizeBeforeCreate = logicielRepository.findAll().size();
        // Create the Logiciel
        restLogicielMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(logiciel)))
            .andExpect(status().isCreated());

        // Validate the Logiciel in the database
        List<Logiciel> logicielList = logicielRepository.findAll();
        assertThat(logicielList).hasSize(databaseSizeBeforeCreate + 1);
        Logiciel testLogiciel = logicielList.get(logicielList.size() - 1);
        assertThat(testLogiciel.getNomLogiciel()).isEqualTo(DEFAULT_NOM_LOGICIEL);
        assertThat(testLogiciel.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createLogicielWithExistingId() throws Exception {
        // Create the Logiciel with an existing ID
        logiciel.setId(1L);

        int databaseSizeBeforeCreate = logicielRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLogicielMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(logiciel)))
            .andExpect(status().isBadRequest());

        // Validate the Logiciel in the database
        List<Logiciel> logicielList = logicielRepository.findAll();
        assertThat(logicielList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLogiciels() throws Exception {
        // Initialize the database
        logicielRepository.saveAndFlush(logiciel);

        // Get all the logicielList
        restLogicielMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(logiciel.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomLogiciel").value(hasItem(DEFAULT_NOM_LOGICIEL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getLogiciel() throws Exception {
        // Initialize the database
        logicielRepository.saveAndFlush(logiciel);

        // Get the logiciel
        restLogicielMockMvc
            .perform(get(ENTITY_API_URL_ID, logiciel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(logiciel.getId().intValue()))
            .andExpect(jsonPath("$.nomLogiciel").value(DEFAULT_NOM_LOGICIEL))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingLogiciel() throws Exception {
        // Get the logiciel
        restLogicielMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLogiciel() throws Exception {
        // Initialize the database
        logicielRepository.saveAndFlush(logiciel);

        int databaseSizeBeforeUpdate = logicielRepository.findAll().size();

        // Update the logiciel
        Logiciel updatedLogiciel = logicielRepository.findById(logiciel.getId()).get();
        // Disconnect from session so that the updates on updatedLogiciel are not directly saved in db
        em.detach(updatedLogiciel);
        updatedLogiciel.nomLogiciel(UPDATED_NOM_LOGICIEL).description(UPDATED_DESCRIPTION);

        restLogicielMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLogiciel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLogiciel))
            )
            .andExpect(status().isOk());

        // Validate the Logiciel in the database
        List<Logiciel> logicielList = logicielRepository.findAll();
        assertThat(logicielList).hasSize(databaseSizeBeforeUpdate);
        Logiciel testLogiciel = logicielList.get(logicielList.size() - 1);
        assertThat(testLogiciel.getNomLogiciel()).isEqualTo(UPDATED_NOM_LOGICIEL);
        assertThat(testLogiciel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingLogiciel() throws Exception {
        int databaseSizeBeforeUpdate = logicielRepository.findAll().size();
        logiciel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLogicielMockMvc
            .perform(
                put(ENTITY_API_URL_ID, logiciel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(logiciel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Logiciel in the database
        List<Logiciel> logicielList = logicielRepository.findAll();
        assertThat(logicielList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLogiciel() throws Exception {
        int databaseSizeBeforeUpdate = logicielRepository.findAll().size();
        logiciel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLogicielMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(logiciel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Logiciel in the database
        List<Logiciel> logicielList = logicielRepository.findAll();
        assertThat(logicielList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLogiciel() throws Exception {
        int databaseSizeBeforeUpdate = logicielRepository.findAll().size();
        logiciel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLogicielMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(logiciel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Logiciel in the database
        List<Logiciel> logicielList = logicielRepository.findAll();
        assertThat(logicielList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLogicielWithPatch() throws Exception {
        // Initialize the database
        logicielRepository.saveAndFlush(logiciel);

        int databaseSizeBeforeUpdate = logicielRepository.findAll().size();

        // Update the logiciel using partial update
        Logiciel partialUpdatedLogiciel = new Logiciel();
        partialUpdatedLogiciel.setId(logiciel.getId());

        restLogicielMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLogiciel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLogiciel))
            )
            .andExpect(status().isOk());

        // Validate the Logiciel in the database
        List<Logiciel> logicielList = logicielRepository.findAll();
        assertThat(logicielList).hasSize(databaseSizeBeforeUpdate);
        Logiciel testLogiciel = logicielList.get(logicielList.size() - 1);
        assertThat(testLogiciel.getNomLogiciel()).isEqualTo(DEFAULT_NOM_LOGICIEL);
        assertThat(testLogiciel.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateLogicielWithPatch() throws Exception {
        // Initialize the database
        logicielRepository.saveAndFlush(logiciel);

        int databaseSizeBeforeUpdate = logicielRepository.findAll().size();

        // Update the logiciel using partial update
        Logiciel partialUpdatedLogiciel = new Logiciel();
        partialUpdatedLogiciel.setId(logiciel.getId());

        partialUpdatedLogiciel.nomLogiciel(UPDATED_NOM_LOGICIEL).description(UPDATED_DESCRIPTION);

        restLogicielMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLogiciel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLogiciel))
            )
            .andExpect(status().isOk());

        // Validate the Logiciel in the database
        List<Logiciel> logicielList = logicielRepository.findAll();
        assertThat(logicielList).hasSize(databaseSizeBeforeUpdate);
        Logiciel testLogiciel = logicielList.get(logicielList.size() - 1);
        assertThat(testLogiciel.getNomLogiciel()).isEqualTo(UPDATED_NOM_LOGICIEL);
        assertThat(testLogiciel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingLogiciel() throws Exception {
        int databaseSizeBeforeUpdate = logicielRepository.findAll().size();
        logiciel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLogicielMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, logiciel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(logiciel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Logiciel in the database
        List<Logiciel> logicielList = logicielRepository.findAll();
        assertThat(logicielList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLogiciel() throws Exception {
        int databaseSizeBeforeUpdate = logicielRepository.findAll().size();
        logiciel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLogicielMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(logiciel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Logiciel in the database
        List<Logiciel> logicielList = logicielRepository.findAll();
        assertThat(logicielList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLogiciel() throws Exception {
        int databaseSizeBeforeUpdate = logicielRepository.findAll().size();
        logiciel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLogicielMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(logiciel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Logiciel in the database
        List<Logiciel> logicielList = logicielRepository.findAll();
        assertThat(logicielList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLogiciel() throws Exception {
        // Initialize the database
        logicielRepository.saveAndFlush(logiciel);

        int databaseSizeBeforeDelete = logicielRepository.findAll().size();

        // Delete the logiciel
        restLogicielMockMvc
            .perform(delete(ENTITY_API_URL_ID, logiciel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Logiciel> logicielList = logicielRepository.findAll();
        assertThat(logicielList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
