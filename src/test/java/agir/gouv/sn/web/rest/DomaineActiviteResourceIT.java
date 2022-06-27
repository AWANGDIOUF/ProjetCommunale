package agir.gouv.sn.web.rest;

import static agir.gouv.sn.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import agir.gouv.sn.IntegrationTest;
import agir.gouv.sn.domain.DomaineActivite;
import agir.gouv.sn.repository.DomaineActiviteRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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

/**
 * Integration tests for the {@link DomaineActiviteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DomaineActiviteResourceIT {

    private static final String DEFAULT_TYPE_ACTIVITE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_ACTIVITE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_ACTIVITE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE_ACTIVITE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/domaine-activites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DomaineActiviteRepository domaineActiviteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDomaineActiviteMockMvc;

    private DomaineActivite domaineActivite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DomaineActivite createEntity(EntityManager em) {
        DomaineActivite domaineActivite = new DomaineActivite()
            .typeActivite(DEFAULT_TYPE_ACTIVITE)
            .description(DEFAULT_DESCRIPTION)
            .dateActivite(DEFAULT_DATE_ACTIVITE);
        return domaineActivite;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DomaineActivite createUpdatedEntity(EntityManager em) {
        DomaineActivite domaineActivite = new DomaineActivite()
            .typeActivite(UPDATED_TYPE_ACTIVITE)
            .description(UPDATED_DESCRIPTION)
            .dateActivite(UPDATED_DATE_ACTIVITE);
        return domaineActivite;
    }

    @BeforeEach
    public void initTest() {
        domaineActivite = createEntity(em);
    }

    @Test
    @Transactional
    void createDomaineActivite() throws Exception {
        int databaseSizeBeforeCreate = domaineActiviteRepository.findAll().size();
        // Create the DomaineActivite
        restDomaineActiviteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(domaineActivite))
            )
            .andExpect(status().isCreated());

        // Validate the DomaineActivite in the database
        List<DomaineActivite> domaineActiviteList = domaineActiviteRepository.findAll();
        assertThat(domaineActiviteList).hasSize(databaseSizeBeforeCreate + 1);
        DomaineActivite testDomaineActivite = domaineActiviteList.get(domaineActiviteList.size() - 1);
        assertThat(testDomaineActivite.getTypeActivite()).isEqualTo(DEFAULT_TYPE_ACTIVITE);
        assertThat(testDomaineActivite.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDomaineActivite.getDateActivite()).isEqualTo(DEFAULT_DATE_ACTIVITE);
    }

    @Test
    @Transactional
    void createDomaineActiviteWithExistingId() throws Exception {
        // Create the DomaineActivite with an existing ID
        domaineActivite.setId(1L);

        int databaseSizeBeforeCreate = domaineActiviteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDomaineActiviteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(domaineActivite))
            )
            .andExpect(status().isBadRequest());

        // Validate the DomaineActivite in the database
        List<DomaineActivite> domaineActiviteList = domaineActiviteRepository.findAll();
        assertThat(domaineActiviteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDomaineActivites() throws Exception {
        // Initialize the database
        domaineActiviteRepository.saveAndFlush(domaineActivite);

        // Get all the domaineActiviteList
        restDomaineActiviteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(domaineActivite.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeActivite").value(hasItem(DEFAULT_TYPE_ACTIVITE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dateActivite").value(hasItem(sameInstant(DEFAULT_DATE_ACTIVITE))));
    }

    @Test
    @Transactional
    void getDomaineActivite() throws Exception {
        // Initialize the database
        domaineActiviteRepository.saveAndFlush(domaineActivite);

        // Get the domaineActivite
        restDomaineActiviteMockMvc
            .perform(get(ENTITY_API_URL_ID, domaineActivite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(domaineActivite.getId().intValue()))
            .andExpect(jsonPath("$.typeActivite").value(DEFAULT_TYPE_ACTIVITE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.dateActivite").value(sameInstant(DEFAULT_DATE_ACTIVITE)));
    }

    @Test
    @Transactional
    void getNonExistingDomaineActivite() throws Exception {
        // Get the domaineActivite
        restDomaineActiviteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDomaineActivite() throws Exception {
        // Initialize the database
        domaineActiviteRepository.saveAndFlush(domaineActivite);

        int databaseSizeBeforeUpdate = domaineActiviteRepository.findAll().size();

        // Update the domaineActivite
        DomaineActivite updatedDomaineActivite = domaineActiviteRepository.findById(domaineActivite.getId()).get();
        // Disconnect from session so that the updates on updatedDomaineActivite are not directly saved in db
        em.detach(updatedDomaineActivite);
        updatedDomaineActivite.typeActivite(UPDATED_TYPE_ACTIVITE).description(UPDATED_DESCRIPTION).dateActivite(UPDATED_DATE_ACTIVITE);

        restDomaineActiviteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDomaineActivite.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDomaineActivite))
            )
            .andExpect(status().isOk());

        // Validate the DomaineActivite in the database
        List<DomaineActivite> domaineActiviteList = domaineActiviteRepository.findAll();
        assertThat(domaineActiviteList).hasSize(databaseSizeBeforeUpdate);
        DomaineActivite testDomaineActivite = domaineActiviteList.get(domaineActiviteList.size() - 1);
        assertThat(testDomaineActivite.getTypeActivite()).isEqualTo(UPDATED_TYPE_ACTIVITE);
        assertThat(testDomaineActivite.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDomaineActivite.getDateActivite()).isEqualTo(UPDATED_DATE_ACTIVITE);
    }

    @Test
    @Transactional
    void putNonExistingDomaineActivite() throws Exception {
        int databaseSizeBeforeUpdate = domaineActiviteRepository.findAll().size();
        domaineActivite.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDomaineActiviteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, domaineActivite.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(domaineActivite))
            )
            .andExpect(status().isBadRequest());

        // Validate the DomaineActivite in the database
        List<DomaineActivite> domaineActiviteList = domaineActiviteRepository.findAll();
        assertThat(domaineActiviteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDomaineActivite() throws Exception {
        int databaseSizeBeforeUpdate = domaineActiviteRepository.findAll().size();
        domaineActivite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDomaineActiviteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(domaineActivite))
            )
            .andExpect(status().isBadRequest());

        // Validate the DomaineActivite in the database
        List<DomaineActivite> domaineActiviteList = domaineActiviteRepository.findAll();
        assertThat(domaineActiviteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDomaineActivite() throws Exception {
        int databaseSizeBeforeUpdate = domaineActiviteRepository.findAll().size();
        domaineActivite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDomaineActiviteMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(domaineActivite))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DomaineActivite in the database
        List<DomaineActivite> domaineActiviteList = domaineActiviteRepository.findAll();
        assertThat(domaineActiviteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDomaineActiviteWithPatch() throws Exception {
        // Initialize the database
        domaineActiviteRepository.saveAndFlush(domaineActivite);

        int databaseSizeBeforeUpdate = domaineActiviteRepository.findAll().size();

        // Update the domaineActivite using partial update
        DomaineActivite partialUpdatedDomaineActivite = new DomaineActivite();
        partialUpdatedDomaineActivite.setId(domaineActivite.getId());

        partialUpdatedDomaineActivite
            .typeActivite(UPDATED_TYPE_ACTIVITE)
            .description(UPDATED_DESCRIPTION)
            .dateActivite(UPDATED_DATE_ACTIVITE);

        restDomaineActiviteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDomaineActivite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDomaineActivite))
            )
            .andExpect(status().isOk());

        // Validate the DomaineActivite in the database
        List<DomaineActivite> domaineActiviteList = domaineActiviteRepository.findAll();
        assertThat(domaineActiviteList).hasSize(databaseSizeBeforeUpdate);
        DomaineActivite testDomaineActivite = domaineActiviteList.get(domaineActiviteList.size() - 1);
        assertThat(testDomaineActivite.getTypeActivite()).isEqualTo(UPDATED_TYPE_ACTIVITE);
        assertThat(testDomaineActivite.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDomaineActivite.getDateActivite()).isEqualTo(UPDATED_DATE_ACTIVITE);
    }

    @Test
    @Transactional
    void fullUpdateDomaineActiviteWithPatch() throws Exception {
        // Initialize the database
        domaineActiviteRepository.saveAndFlush(domaineActivite);

        int databaseSizeBeforeUpdate = domaineActiviteRepository.findAll().size();

        // Update the domaineActivite using partial update
        DomaineActivite partialUpdatedDomaineActivite = new DomaineActivite();
        partialUpdatedDomaineActivite.setId(domaineActivite.getId());

        partialUpdatedDomaineActivite
            .typeActivite(UPDATED_TYPE_ACTIVITE)
            .description(UPDATED_DESCRIPTION)
            .dateActivite(UPDATED_DATE_ACTIVITE);

        restDomaineActiviteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDomaineActivite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDomaineActivite))
            )
            .andExpect(status().isOk());

        // Validate the DomaineActivite in the database
        List<DomaineActivite> domaineActiviteList = domaineActiviteRepository.findAll();
        assertThat(domaineActiviteList).hasSize(databaseSizeBeforeUpdate);
        DomaineActivite testDomaineActivite = domaineActiviteList.get(domaineActiviteList.size() - 1);
        assertThat(testDomaineActivite.getTypeActivite()).isEqualTo(UPDATED_TYPE_ACTIVITE);
        assertThat(testDomaineActivite.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDomaineActivite.getDateActivite()).isEqualTo(UPDATED_DATE_ACTIVITE);
    }

    @Test
    @Transactional
    void patchNonExistingDomaineActivite() throws Exception {
        int databaseSizeBeforeUpdate = domaineActiviteRepository.findAll().size();
        domaineActivite.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDomaineActiviteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, domaineActivite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(domaineActivite))
            )
            .andExpect(status().isBadRequest());

        // Validate the DomaineActivite in the database
        List<DomaineActivite> domaineActiviteList = domaineActiviteRepository.findAll();
        assertThat(domaineActiviteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDomaineActivite() throws Exception {
        int databaseSizeBeforeUpdate = domaineActiviteRepository.findAll().size();
        domaineActivite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDomaineActiviteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(domaineActivite))
            )
            .andExpect(status().isBadRequest());

        // Validate the DomaineActivite in the database
        List<DomaineActivite> domaineActiviteList = domaineActiviteRepository.findAll();
        assertThat(domaineActiviteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDomaineActivite() throws Exception {
        int databaseSizeBeforeUpdate = domaineActiviteRepository.findAll().size();
        domaineActivite.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDomaineActiviteMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(domaineActivite))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DomaineActivite in the database
        List<DomaineActivite> domaineActiviteList = domaineActiviteRepository.findAll();
        assertThat(domaineActiviteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDomaineActivite() throws Exception {
        // Initialize the database
        domaineActiviteRepository.saveAndFlush(domaineActivite);

        int databaseSizeBeforeDelete = domaineActiviteRepository.findAll().size();

        // Delete the domaineActivite
        restDomaineActiviteMockMvc
            .perform(delete(ENTITY_API_URL_ID, domaineActivite.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DomaineActivite> domaineActiviteList = domaineActiviteRepository.findAll();
        assertThat(domaineActiviteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
