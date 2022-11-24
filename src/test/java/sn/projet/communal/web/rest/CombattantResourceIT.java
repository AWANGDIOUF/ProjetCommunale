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
import sn.projet.communal.domain.Combattant;
import sn.projet.communal.repository.CombattantRepository;

/**
 * Integration tests for the {@link CombattantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CombattantResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_NAIS = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_NAIS = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_LIEU_NAIS = "AAAAAAAAAA";
    private static final String UPDATED_LIEU_NAIS = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/combattants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CombattantRepository combattantRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCombattantMockMvc;

    private Combattant combattant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Combattant createEntity(EntityManager em) {
        Combattant combattant = new Combattant()
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .dateNais(DEFAULT_DATE_NAIS)
            .lieuNais(DEFAULT_LIEU_NAIS)
            .photo(DEFAULT_PHOTO)
            .photoContentType(DEFAULT_PHOTO_CONTENT_TYPE);
        return combattant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Combattant createUpdatedEntity(EntityManager em) {
        Combattant combattant = new Combattant()
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .dateNais(UPDATED_DATE_NAIS)
            .lieuNais(UPDATED_LIEU_NAIS)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE);
        return combattant;
    }

    @BeforeEach
    public void initTest() {
        combattant = createEntity(em);
    }

    @Test
    @Transactional
    void createCombattant() throws Exception {
        int databaseSizeBeforeCreate = combattantRepository.findAll().size();
        // Create the Combattant
        restCombattantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(combattant)))
            .andExpect(status().isCreated());

        // Validate the Combattant in the database
        List<Combattant> combattantList = combattantRepository.findAll();
        assertThat(combattantList).hasSize(databaseSizeBeforeCreate + 1);
        Combattant testCombattant = combattantList.get(combattantList.size() - 1);
        assertThat(testCombattant.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testCombattant.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testCombattant.getDateNais()).isEqualTo(DEFAULT_DATE_NAIS);
        assertThat(testCombattant.getLieuNais()).isEqualTo(DEFAULT_LIEU_NAIS);
        assertThat(testCombattant.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testCombattant.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createCombattantWithExistingId() throws Exception {
        // Create the Combattant with an existing ID
        combattant.setId(1L);

        int databaseSizeBeforeCreate = combattantRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCombattantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(combattant)))
            .andExpect(status().isBadRequest());

        // Validate the Combattant in the database
        List<Combattant> combattantList = combattantRepository.findAll();
        assertThat(combattantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCombattants() throws Exception {
        // Initialize the database
        combattantRepository.saveAndFlush(combattant);

        // Get all the combattantList
        restCombattantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(combattant.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].dateNais").value(hasItem(DEFAULT_DATE_NAIS.toString())))
            .andExpect(jsonPath("$.[*].lieuNais").value(hasItem(DEFAULT_LIEU_NAIS)))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO))));
    }

    @Test
    @Transactional
    void getCombattant() throws Exception {
        // Initialize the database
        combattantRepository.saveAndFlush(combattant);

        // Get the combattant
        restCombattantMockMvc
            .perform(get(ENTITY_API_URL_ID, combattant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(combattant.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.dateNais").value(DEFAULT_DATE_NAIS.toString()))
            .andExpect(jsonPath("$.lieuNais").value(DEFAULT_LIEU_NAIS))
            .andExpect(jsonPath("$.photoContentType").value(DEFAULT_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.photo").value(Base64Utils.encodeToString(DEFAULT_PHOTO)));
    }

    @Test
    @Transactional
    void getNonExistingCombattant() throws Exception {
        // Get the combattant
        restCombattantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCombattant() throws Exception {
        // Initialize the database
        combattantRepository.saveAndFlush(combattant);

        int databaseSizeBeforeUpdate = combattantRepository.findAll().size();

        // Update the combattant
        Combattant updatedCombattant = combattantRepository.findById(combattant.getId()).get();
        // Disconnect from session so that the updates on updatedCombattant are not directly saved in db
        em.detach(updatedCombattant);
        updatedCombattant
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .dateNais(UPDATED_DATE_NAIS)
            .lieuNais(UPDATED_LIEU_NAIS)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE);

        restCombattantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCombattant.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCombattant))
            )
            .andExpect(status().isOk());

        // Validate the Combattant in the database
        List<Combattant> combattantList = combattantRepository.findAll();
        assertThat(combattantList).hasSize(databaseSizeBeforeUpdate);
        Combattant testCombattant = combattantList.get(combattantList.size() - 1);
        assertThat(testCombattant.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testCombattant.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testCombattant.getDateNais()).isEqualTo(UPDATED_DATE_NAIS);
        assertThat(testCombattant.getLieuNais()).isEqualTo(UPDATED_LIEU_NAIS);
        assertThat(testCombattant.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testCombattant.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingCombattant() throws Exception {
        int databaseSizeBeforeUpdate = combattantRepository.findAll().size();
        combattant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCombattantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, combattant.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(combattant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Combattant in the database
        List<Combattant> combattantList = combattantRepository.findAll();
        assertThat(combattantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCombattant() throws Exception {
        int databaseSizeBeforeUpdate = combattantRepository.findAll().size();
        combattant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCombattantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(combattant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Combattant in the database
        List<Combattant> combattantList = combattantRepository.findAll();
        assertThat(combattantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCombattant() throws Exception {
        int databaseSizeBeforeUpdate = combattantRepository.findAll().size();
        combattant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCombattantMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(combattant)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Combattant in the database
        List<Combattant> combattantList = combattantRepository.findAll();
        assertThat(combattantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCombattantWithPatch() throws Exception {
        // Initialize the database
        combattantRepository.saveAndFlush(combattant);

        int databaseSizeBeforeUpdate = combattantRepository.findAll().size();

        // Update the combattant using partial update
        Combattant partialUpdatedCombattant = new Combattant();
        partialUpdatedCombattant.setId(combattant.getId());

        partialUpdatedCombattant
            .prenom(UPDATED_PRENOM)
            .lieuNais(UPDATED_LIEU_NAIS)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE);

        restCombattantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCombattant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCombattant))
            )
            .andExpect(status().isOk());

        // Validate the Combattant in the database
        List<Combattant> combattantList = combattantRepository.findAll();
        assertThat(combattantList).hasSize(databaseSizeBeforeUpdate);
        Combattant testCombattant = combattantList.get(combattantList.size() - 1);
        assertThat(testCombattant.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testCombattant.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testCombattant.getDateNais()).isEqualTo(DEFAULT_DATE_NAIS);
        assertThat(testCombattant.getLieuNais()).isEqualTo(UPDATED_LIEU_NAIS);
        assertThat(testCombattant.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testCombattant.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateCombattantWithPatch() throws Exception {
        // Initialize the database
        combattantRepository.saveAndFlush(combattant);

        int databaseSizeBeforeUpdate = combattantRepository.findAll().size();

        // Update the combattant using partial update
        Combattant partialUpdatedCombattant = new Combattant();
        partialUpdatedCombattant.setId(combattant.getId());

        partialUpdatedCombattant
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .dateNais(UPDATED_DATE_NAIS)
            .lieuNais(UPDATED_LIEU_NAIS)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE);

        restCombattantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCombattant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCombattant))
            )
            .andExpect(status().isOk());

        // Validate the Combattant in the database
        List<Combattant> combattantList = combattantRepository.findAll();
        assertThat(combattantList).hasSize(databaseSizeBeforeUpdate);
        Combattant testCombattant = combattantList.get(combattantList.size() - 1);
        assertThat(testCombattant.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testCombattant.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testCombattant.getDateNais()).isEqualTo(UPDATED_DATE_NAIS);
        assertThat(testCombattant.getLieuNais()).isEqualTo(UPDATED_LIEU_NAIS);
        assertThat(testCombattant.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testCombattant.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingCombattant() throws Exception {
        int databaseSizeBeforeUpdate = combattantRepository.findAll().size();
        combattant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCombattantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, combattant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(combattant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Combattant in the database
        List<Combattant> combattantList = combattantRepository.findAll();
        assertThat(combattantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCombattant() throws Exception {
        int databaseSizeBeforeUpdate = combattantRepository.findAll().size();
        combattant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCombattantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(combattant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Combattant in the database
        List<Combattant> combattantList = combattantRepository.findAll();
        assertThat(combattantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCombattant() throws Exception {
        int databaseSizeBeforeUpdate = combattantRepository.findAll().size();
        combattant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCombattantMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(combattant))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Combattant in the database
        List<Combattant> combattantList = combattantRepository.findAll();
        assertThat(combattantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCombattant() throws Exception {
        // Initialize the database
        combattantRepository.saveAndFlush(combattant);

        int databaseSizeBeforeDelete = combattantRepository.findAll().size();

        // Delete the combattant
        restCombattantMockMvc
            .perform(delete(ENTITY_API_URL_ID, combattant.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Combattant> combattantList = combattantRepository.findAll();
        assertThat(combattantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
