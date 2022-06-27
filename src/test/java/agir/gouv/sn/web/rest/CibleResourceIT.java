package agir.gouv.sn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import agir.gouv.sn.IntegrationTest;
import agir.gouv.sn.domain.Cible;
import agir.gouv.sn.domain.enumeration.CibleVacc;
import agir.gouv.sn.repository.CibleRepository;
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
 * Integration tests for the {@link CibleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CibleResourceIT {

    private static final CibleVacc DEFAULT_CIBLE = CibleVacc.Enfant;
    private static final CibleVacc UPDATED_CIBLE = CibleVacc.Adulte;

    private static final Long DEFAULT_AGE = 1L;
    private static final Long UPDATED_AGE = 2L;

    private static final String ENTITY_API_URL = "/api/cibles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CibleRepository cibleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCibleMockMvc;

    private Cible cible;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cible createEntity(EntityManager em) {
        Cible cible = new Cible().cible(DEFAULT_CIBLE).age(DEFAULT_AGE);
        return cible;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cible createUpdatedEntity(EntityManager em) {
        Cible cible = new Cible().cible(UPDATED_CIBLE).age(UPDATED_AGE);
        return cible;
    }

    @BeforeEach
    public void initTest() {
        cible = createEntity(em);
    }

    @Test
    @Transactional
    void createCible() throws Exception {
        int databaseSizeBeforeCreate = cibleRepository.findAll().size();
        // Create the Cible
        restCibleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cible)))
            .andExpect(status().isCreated());

        // Validate the Cible in the database
        List<Cible> cibleList = cibleRepository.findAll();
        assertThat(cibleList).hasSize(databaseSizeBeforeCreate + 1);
        Cible testCible = cibleList.get(cibleList.size() - 1);
        assertThat(testCible.getCible()).isEqualTo(DEFAULT_CIBLE);
        assertThat(testCible.getAge()).isEqualTo(DEFAULT_AGE);
    }

    @Test
    @Transactional
    void createCibleWithExistingId() throws Exception {
        // Create the Cible with an existing ID
        cible.setId(1L);

        int databaseSizeBeforeCreate = cibleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCibleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cible)))
            .andExpect(status().isBadRequest());

        // Validate the Cible in the database
        List<Cible> cibleList = cibleRepository.findAll();
        assertThat(cibleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCibles() throws Exception {
        // Initialize the database
        cibleRepository.saveAndFlush(cible);

        // Get all the cibleList
        restCibleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cible.getId().intValue())))
            .andExpect(jsonPath("$.[*].cible").value(hasItem(DEFAULT_CIBLE.toString())))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE.intValue())));
    }

    @Test
    @Transactional
    void getCible() throws Exception {
        // Initialize the database
        cibleRepository.saveAndFlush(cible);

        // Get the cible
        restCibleMockMvc
            .perform(get(ENTITY_API_URL_ID, cible.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cible.getId().intValue()))
            .andExpect(jsonPath("$.cible").value(DEFAULT_CIBLE.toString()))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingCible() throws Exception {
        // Get the cible
        restCibleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCible() throws Exception {
        // Initialize the database
        cibleRepository.saveAndFlush(cible);

        int databaseSizeBeforeUpdate = cibleRepository.findAll().size();

        // Update the cible
        Cible updatedCible = cibleRepository.findById(cible.getId()).get();
        // Disconnect from session so that the updates on updatedCible are not directly saved in db
        em.detach(updatedCible);
        updatedCible.cible(UPDATED_CIBLE).age(UPDATED_AGE);

        restCibleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCible.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCible))
            )
            .andExpect(status().isOk());

        // Validate the Cible in the database
        List<Cible> cibleList = cibleRepository.findAll();
        assertThat(cibleList).hasSize(databaseSizeBeforeUpdate);
        Cible testCible = cibleList.get(cibleList.size() - 1);
        assertThat(testCible.getCible()).isEqualTo(UPDATED_CIBLE);
        assertThat(testCible.getAge()).isEqualTo(UPDATED_AGE);
    }

    @Test
    @Transactional
    void putNonExistingCible() throws Exception {
        int databaseSizeBeforeUpdate = cibleRepository.findAll().size();
        cible.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCibleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cible.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cible))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cible in the database
        List<Cible> cibleList = cibleRepository.findAll();
        assertThat(cibleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCible() throws Exception {
        int databaseSizeBeforeUpdate = cibleRepository.findAll().size();
        cible.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCibleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cible))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cible in the database
        List<Cible> cibleList = cibleRepository.findAll();
        assertThat(cibleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCible() throws Exception {
        int databaseSizeBeforeUpdate = cibleRepository.findAll().size();
        cible.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCibleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cible)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cible in the database
        List<Cible> cibleList = cibleRepository.findAll();
        assertThat(cibleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCibleWithPatch() throws Exception {
        // Initialize the database
        cibleRepository.saveAndFlush(cible);

        int databaseSizeBeforeUpdate = cibleRepository.findAll().size();

        // Update the cible using partial update
        Cible partialUpdatedCible = new Cible();
        partialUpdatedCible.setId(cible.getId());

        partialUpdatedCible.cible(UPDATED_CIBLE);

        restCibleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCible.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCible))
            )
            .andExpect(status().isOk());

        // Validate the Cible in the database
        List<Cible> cibleList = cibleRepository.findAll();
        assertThat(cibleList).hasSize(databaseSizeBeforeUpdate);
        Cible testCible = cibleList.get(cibleList.size() - 1);
        assertThat(testCible.getCible()).isEqualTo(UPDATED_CIBLE);
        assertThat(testCible.getAge()).isEqualTo(DEFAULT_AGE);
    }

    @Test
    @Transactional
    void fullUpdateCibleWithPatch() throws Exception {
        // Initialize the database
        cibleRepository.saveAndFlush(cible);

        int databaseSizeBeforeUpdate = cibleRepository.findAll().size();

        // Update the cible using partial update
        Cible partialUpdatedCible = new Cible();
        partialUpdatedCible.setId(cible.getId());

        partialUpdatedCible.cible(UPDATED_CIBLE).age(UPDATED_AGE);

        restCibleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCible.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCible))
            )
            .andExpect(status().isOk());

        // Validate the Cible in the database
        List<Cible> cibleList = cibleRepository.findAll();
        assertThat(cibleList).hasSize(databaseSizeBeforeUpdate);
        Cible testCible = cibleList.get(cibleList.size() - 1);
        assertThat(testCible.getCible()).isEqualTo(UPDATED_CIBLE);
        assertThat(testCible.getAge()).isEqualTo(UPDATED_AGE);
    }

    @Test
    @Transactional
    void patchNonExistingCible() throws Exception {
        int databaseSizeBeforeUpdate = cibleRepository.findAll().size();
        cible.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCibleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cible.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cible))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cible in the database
        List<Cible> cibleList = cibleRepository.findAll();
        assertThat(cibleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCible() throws Exception {
        int databaseSizeBeforeUpdate = cibleRepository.findAll().size();
        cible.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCibleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cible))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cible in the database
        List<Cible> cibleList = cibleRepository.findAll();
        assertThat(cibleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCible() throws Exception {
        int databaseSizeBeforeUpdate = cibleRepository.findAll().size();
        cible.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCibleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cible)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cible in the database
        List<Cible> cibleList = cibleRepository.findAll();
        assertThat(cibleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCible() throws Exception {
        // Initialize the database
        cibleRepository.saveAndFlush(cible);

        int databaseSizeBeforeDelete = cibleRepository.findAll().size();

        // Delete the cible
        restCibleMockMvc
            .perform(delete(ENTITY_API_URL_ID, cible.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cible> cibleList = cibleRepository.findAll();
        assertThat(cibleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
