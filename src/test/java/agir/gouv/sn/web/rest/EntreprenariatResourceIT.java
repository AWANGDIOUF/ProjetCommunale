package agir.gouv.sn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import agir.gouv.sn.IntegrationTest;
import agir.gouv.sn.domain.Entreprenariat;
import agir.gouv.sn.domain.enumeration.TypeEntreprenariat;
import agir.gouv.sn.repository.EntreprenariatRepository;
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
 * Integration tests for the {@link EntreprenariatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EntreprenariatResourceIT {

    private static final TypeEntreprenariat DEFAULT_TYPE_ENTRE = TypeEntreprenariat.Social;
    private static final TypeEntreprenariat UPDATED_TYPE_ENTRE = TypeEntreprenariat.Economique;

    private static final String DEFAULT_AUTRE_ENTRE = "AAAAAAAAAA";
    private static final String UPDATED_AUTRE_ENTRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/entreprenariats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EntreprenariatRepository entreprenariatRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEntreprenariatMockMvc;

    private Entreprenariat entreprenariat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Entreprenariat createEntity(EntityManager em) {
        Entreprenariat entreprenariat = new Entreprenariat().typeEntre(DEFAULT_TYPE_ENTRE).autreEntre(DEFAULT_AUTRE_ENTRE);
        return entreprenariat;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Entreprenariat createUpdatedEntity(EntityManager em) {
        Entreprenariat entreprenariat = new Entreprenariat().typeEntre(UPDATED_TYPE_ENTRE).autreEntre(UPDATED_AUTRE_ENTRE);
        return entreprenariat;
    }

    @BeforeEach
    public void initTest() {
        entreprenariat = createEntity(em);
    }

    @Test
    @Transactional
    void createEntreprenariat() throws Exception {
        int databaseSizeBeforeCreate = entreprenariatRepository.findAll().size();
        // Create the Entreprenariat
        restEntreprenariatMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(entreprenariat))
            )
            .andExpect(status().isCreated());

        // Validate the Entreprenariat in the database
        List<Entreprenariat> entreprenariatList = entreprenariatRepository.findAll();
        assertThat(entreprenariatList).hasSize(databaseSizeBeforeCreate + 1);
        Entreprenariat testEntreprenariat = entreprenariatList.get(entreprenariatList.size() - 1);
        assertThat(testEntreprenariat.getTypeEntre()).isEqualTo(DEFAULT_TYPE_ENTRE);
        assertThat(testEntreprenariat.getAutreEntre()).isEqualTo(DEFAULT_AUTRE_ENTRE);
    }

    @Test
    @Transactional
    void createEntreprenariatWithExistingId() throws Exception {
        // Create the Entreprenariat with an existing ID
        entreprenariat.setId(1L);

        int databaseSizeBeforeCreate = entreprenariatRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntreprenariatMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(entreprenariat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Entreprenariat in the database
        List<Entreprenariat> entreprenariatList = entreprenariatRepository.findAll();
        assertThat(entreprenariatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEntreprenariats() throws Exception {
        // Initialize the database
        entreprenariatRepository.saveAndFlush(entreprenariat);

        // Get all the entreprenariatList
        restEntreprenariatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entreprenariat.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeEntre").value(hasItem(DEFAULT_TYPE_ENTRE.toString())))
            .andExpect(jsonPath("$.[*].autreEntre").value(hasItem(DEFAULT_AUTRE_ENTRE)));
    }

    @Test
    @Transactional
    void getEntreprenariat() throws Exception {
        // Initialize the database
        entreprenariatRepository.saveAndFlush(entreprenariat);

        // Get the entreprenariat
        restEntreprenariatMockMvc
            .perform(get(ENTITY_API_URL_ID, entreprenariat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(entreprenariat.getId().intValue()))
            .andExpect(jsonPath("$.typeEntre").value(DEFAULT_TYPE_ENTRE.toString()))
            .andExpect(jsonPath("$.autreEntre").value(DEFAULT_AUTRE_ENTRE));
    }

    @Test
    @Transactional
    void getNonExistingEntreprenariat() throws Exception {
        // Get the entreprenariat
        restEntreprenariatMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEntreprenariat() throws Exception {
        // Initialize the database
        entreprenariatRepository.saveAndFlush(entreprenariat);

        int databaseSizeBeforeUpdate = entreprenariatRepository.findAll().size();

        // Update the entreprenariat
        Entreprenariat updatedEntreprenariat = entreprenariatRepository.findById(entreprenariat.getId()).get();
        // Disconnect from session so that the updates on updatedEntreprenariat are not directly saved in db
        em.detach(updatedEntreprenariat);
        updatedEntreprenariat.typeEntre(UPDATED_TYPE_ENTRE).autreEntre(UPDATED_AUTRE_ENTRE);

        restEntreprenariatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEntreprenariat.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEntreprenariat))
            )
            .andExpect(status().isOk());

        // Validate the Entreprenariat in the database
        List<Entreprenariat> entreprenariatList = entreprenariatRepository.findAll();
        assertThat(entreprenariatList).hasSize(databaseSizeBeforeUpdate);
        Entreprenariat testEntreprenariat = entreprenariatList.get(entreprenariatList.size() - 1);
        assertThat(testEntreprenariat.getTypeEntre()).isEqualTo(UPDATED_TYPE_ENTRE);
        assertThat(testEntreprenariat.getAutreEntre()).isEqualTo(UPDATED_AUTRE_ENTRE);
    }

    @Test
    @Transactional
    void putNonExistingEntreprenariat() throws Exception {
        int databaseSizeBeforeUpdate = entreprenariatRepository.findAll().size();
        entreprenariat.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEntreprenariatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, entreprenariat.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(entreprenariat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Entreprenariat in the database
        List<Entreprenariat> entreprenariatList = entreprenariatRepository.findAll();
        assertThat(entreprenariatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEntreprenariat() throws Exception {
        int databaseSizeBeforeUpdate = entreprenariatRepository.findAll().size();
        entreprenariat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntreprenariatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(entreprenariat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Entreprenariat in the database
        List<Entreprenariat> entreprenariatList = entreprenariatRepository.findAll();
        assertThat(entreprenariatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEntreprenariat() throws Exception {
        int databaseSizeBeforeUpdate = entreprenariatRepository.findAll().size();
        entreprenariat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntreprenariatMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(entreprenariat)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Entreprenariat in the database
        List<Entreprenariat> entreprenariatList = entreprenariatRepository.findAll();
        assertThat(entreprenariatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEntreprenariatWithPatch() throws Exception {
        // Initialize the database
        entreprenariatRepository.saveAndFlush(entreprenariat);

        int databaseSizeBeforeUpdate = entreprenariatRepository.findAll().size();

        // Update the entreprenariat using partial update
        Entreprenariat partialUpdatedEntreprenariat = new Entreprenariat();
        partialUpdatedEntreprenariat.setId(entreprenariat.getId());

        partialUpdatedEntreprenariat.typeEntre(UPDATED_TYPE_ENTRE).autreEntre(UPDATED_AUTRE_ENTRE);

        restEntreprenariatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEntreprenariat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEntreprenariat))
            )
            .andExpect(status().isOk());

        // Validate the Entreprenariat in the database
        List<Entreprenariat> entreprenariatList = entreprenariatRepository.findAll();
        assertThat(entreprenariatList).hasSize(databaseSizeBeforeUpdate);
        Entreprenariat testEntreprenariat = entreprenariatList.get(entreprenariatList.size() - 1);
        assertThat(testEntreprenariat.getTypeEntre()).isEqualTo(UPDATED_TYPE_ENTRE);
        assertThat(testEntreprenariat.getAutreEntre()).isEqualTo(UPDATED_AUTRE_ENTRE);
    }

    @Test
    @Transactional
    void fullUpdateEntreprenariatWithPatch() throws Exception {
        // Initialize the database
        entreprenariatRepository.saveAndFlush(entreprenariat);

        int databaseSizeBeforeUpdate = entreprenariatRepository.findAll().size();

        // Update the entreprenariat using partial update
        Entreprenariat partialUpdatedEntreprenariat = new Entreprenariat();
        partialUpdatedEntreprenariat.setId(entreprenariat.getId());

        partialUpdatedEntreprenariat.typeEntre(UPDATED_TYPE_ENTRE).autreEntre(UPDATED_AUTRE_ENTRE);

        restEntreprenariatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEntreprenariat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEntreprenariat))
            )
            .andExpect(status().isOk());

        // Validate the Entreprenariat in the database
        List<Entreprenariat> entreprenariatList = entreprenariatRepository.findAll();
        assertThat(entreprenariatList).hasSize(databaseSizeBeforeUpdate);
        Entreprenariat testEntreprenariat = entreprenariatList.get(entreprenariatList.size() - 1);
        assertThat(testEntreprenariat.getTypeEntre()).isEqualTo(UPDATED_TYPE_ENTRE);
        assertThat(testEntreprenariat.getAutreEntre()).isEqualTo(UPDATED_AUTRE_ENTRE);
    }

    @Test
    @Transactional
    void patchNonExistingEntreprenariat() throws Exception {
        int databaseSizeBeforeUpdate = entreprenariatRepository.findAll().size();
        entreprenariat.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEntreprenariatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, entreprenariat.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(entreprenariat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Entreprenariat in the database
        List<Entreprenariat> entreprenariatList = entreprenariatRepository.findAll();
        assertThat(entreprenariatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEntreprenariat() throws Exception {
        int databaseSizeBeforeUpdate = entreprenariatRepository.findAll().size();
        entreprenariat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntreprenariatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(entreprenariat))
            )
            .andExpect(status().isBadRequest());

        // Validate the Entreprenariat in the database
        List<Entreprenariat> entreprenariatList = entreprenariatRepository.findAll();
        assertThat(entreprenariatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEntreprenariat() throws Exception {
        int databaseSizeBeforeUpdate = entreprenariatRepository.findAll().size();
        entreprenariat.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntreprenariatMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(entreprenariat))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Entreprenariat in the database
        List<Entreprenariat> entreprenariatList = entreprenariatRepository.findAll();
        assertThat(entreprenariatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEntreprenariat() throws Exception {
        // Initialize the database
        entreprenariatRepository.saveAndFlush(entreprenariat);

        int databaseSizeBeforeDelete = entreprenariatRepository.findAll().size();

        // Delete the entreprenariat
        restEntreprenariatMockMvc
            .perform(delete(ENTITY_API_URL_ID, entreprenariat.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Entreprenariat> entreprenariatList = entreprenariatRepository.findAll();
        assertThat(entreprenariatList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
