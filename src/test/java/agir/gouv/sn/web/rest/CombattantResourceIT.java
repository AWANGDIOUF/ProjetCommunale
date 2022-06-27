package agir.gouv.sn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import agir.gouv.sn.IntegrationTest;
import agir.gouv.sn.domain.Combattant;
import agir.gouv.sn.repository.CombattantRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link CombattantResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CombattantResourceIT {

    private static final String DEFAULT_NOM_COMBATTANT = "AAAAAAAAAA";
    private static final String UPDATED_NOM_COMBATTANT = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM_COMBATTANT = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM_COMBATTANT = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_NAIS_COMBATTANT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_NAIS_COMBATTANT = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_LIEU_NAIS_COMBATTANT = "AAAAAAAAAA";
    private static final String UPDATED_LIEU_NAIS_COMBATTANT = "BBBBBBBBBB";

    private static final byte[] DEFAULT_PHOTO_COMBATTANT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO_COMBATTANT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PHOTO_COMBATTANT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_COMBATTANT_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/combattants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CombattantRepository combattantRepository;

    @Mock
    private CombattantRepository combattantRepositoryMock;

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
            .nomCombattant(DEFAULT_NOM_COMBATTANT)
            .prenomCombattant(DEFAULT_PRENOM_COMBATTANT)
            .dateNaisCombattant(DEFAULT_DATE_NAIS_COMBATTANT)
            .lieuNaisCombattant(DEFAULT_LIEU_NAIS_COMBATTANT)
            .photoCombattant(DEFAULT_PHOTO_COMBATTANT)
            .photoCombattantContentType(DEFAULT_PHOTO_COMBATTANT_CONTENT_TYPE);
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
            .nomCombattant(UPDATED_NOM_COMBATTANT)
            .prenomCombattant(UPDATED_PRENOM_COMBATTANT)
            .dateNaisCombattant(UPDATED_DATE_NAIS_COMBATTANT)
            .lieuNaisCombattant(UPDATED_LIEU_NAIS_COMBATTANT)
            .photoCombattant(UPDATED_PHOTO_COMBATTANT)
            .photoCombattantContentType(UPDATED_PHOTO_COMBATTANT_CONTENT_TYPE);
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
        assertThat(testCombattant.getNomCombattant()).isEqualTo(DEFAULT_NOM_COMBATTANT);
        assertThat(testCombattant.getPrenomCombattant()).isEqualTo(DEFAULT_PRENOM_COMBATTANT);
        assertThat(testCombattant.getDateNaisCombattant()).isEqualTo(DEFAULT_DATE_NAIS_COMBATTANT);
        assertThat(testCombattant.getLieuNaisCombattant()).isEqualTo(DEFAULT_LIEU_NAIS_COMBATTANT);
        assertThat(testCombattant.getPhotoCombattant()).isEqualTo(DEFAULT_PHOTO_COMBATTANT);
        assertThat(testCombattant.getPhotoCombattantContentType()).isEqualTo(DEFAULT_PHOTO_COMBATTANT_CONTENT_TYPE);
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
            .andExpect(jsonPath("$.[*].nomCombattant").value(hasItem(DEFAULT_NOM_COMBATTANT)))
            .andExpect(jsonPath("$.[*].prenomCombattant").value(hasItem(DEFAULT_PRENOM_COMBATTANT)))
            .andExpect(jsonPath("$.[*].dateNaisCombattant").value(hasItem(DEFAULT_DATE_NAIS_COMBATTANT.toString())))
            .andExpect(jsonPath("$.[*].lieuNaisCombattant").value(hasItem(DEFAULT_LIEU_NAIS_COMBATTANT)))
            .andExpect(jsonPath("$.[*].photoCombattantContentType").value(hasItem(DEFAULT_PHOTO_COMBATTANT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photoCombattant").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO_COMBATTANT))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCombattantsWithEagerRelationshipsIsEnabled() throws Exception {
        when(combattantRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCombattantMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(combattantRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCombattantsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(combattantRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCombattantMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(combattantRepositoryMock, times(1)).findAllWithEagerRelationships(any());
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
            .andExpect(jsonPath("$.nomCombattant").value(DEFAULT_NOM_COMBATTANT))
            .andExpect(jsonPath("$.prenomCombattant").value(DEFAULT_PRENOM_COMBATTANT))
            .andExpect(jsonPath("$.dateNaisCombattant").value(DEFAULT_DATE_NAIS_COMBATTANT.toString()))
            .andExpect(jsonPath("$.lieuNaisCombattant").value(DEFAULT_LIEU_NAIS_COMBATTANT))
            .andExpect(jsonPath("$.photoCombattantContentType").value(DEFAULT_PHOTO_COMBATTANT_CONTENT_TYPE))
            .andExpect(jsonPath("$.photoCombattant").value(Base64Utils.encodeToString(DEFAULT_PHOTO_COMBATTANT)));
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
            .nomCombattant(UPDATED_NOM_COMBATTANT)
            .prenomCombattant(UPDATED_PRENOM_COMBATTANT)
            .dateNaisCombattant(UPDATED_DATE_NAIS_COMBATTANT)
            .lieuNaisCombattant(UPDATED_LIEU_NAIS_COMBATTANT)
            .photoCombattant(UPDATED_PHOTO_COMBATTANT)
            .photoCombattantContentType(UPDATED_PHOTO_COMBATTANT_CONTENT_TYPE);

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
        assertThat(testCombattant.getNomCombattant()).isEqualTo(UPDATED_NOM_COMBATTANT);
        assertThat(testCombattant.getPrenomCombattant()).isEqualTo(UPDATED_PRENOM_COMBATTANT);
        assertThat(testCombattant.getDateNaisCombattant()).isEqualTo(UPDATED_DATE_NAIS_COMBATTANT);
        assertThat(testCombattant.getLieuNaisCombattant()).isEqualTo(UPDATED_LIEU_NAIS_COMBATTANT);
        assertThat(testCombattant.getPhotoCombattant()).isEqualTo(UPDATED_PHOTO_COMBATTANT);
        assertThat(testCombattant.getPhotoCombattantContentType()).isEqualTo(UPDATED_PHOTO_COMBATTANT_CONTENT_TYPE);
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
            .prenomCombattant(UPDATED_PRENOM_COMBATTANT)
            .lieuNaisCombattant(UPDATED_LIEU_NAIS_COMBATTANT)
            .photoCombattant(UPDATED_PHOTO_COMBATTANT)
            .photoCombattantContentType(UPDATED_PHOTO_COMBATTANT_CONTENT_TYPE);

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
        assertThat(testCombattant.getNomCombattant()).isEqualTo(DEFAULT_NOM_COMBATTANT);
        assertThat(testCombattant.getPrenomCombattant()).isEqualTo(UPDATED_PRENOM_COMBATTANT);
        assertThat(testCombattant.getDateNaisCombattant()).isEqualTo(DEFAULT_DATE_NAIS_COMBATTANT);
        assertThat(testCombattant.getLieuNaisCombattant()).isEqualTo(UPDATED_LIEU_NAIS_COMBATTANT);
        assertThat(testCombattant.getPhotoCombattant()).isEqualTo(UPDATED_PHOTO_COMBATTANT);
        assertThat(testCombattant.getPhotoCombattantContentType()).isEqualTo(UPDATED_PHOTO_COMBATTANT_CONTENT_TYPE);
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
            .nomCombattant(UPDATED_NOM_COMBATTANT)
            .prenomCombattant(UPDATED_PRENOM_COMBATTANT)
            .dateNaisCombattant(UPDATED_DATE_NAIS_COMBATTANT)
            .lieuNaisCombattant(UPDATED_LIEU_NAIS_COMBATTANT)
            .photoCombattant(UPDATED_PHOTO_COMBATTANT)
            .photoCombattantContentType(UPDATED_PHOTO_COMBATTANT_CONTENT_TYPE);

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
        assertThat(testCombattant.getNomCombattant()).isEqualTo(UPDATED_NOM_COMBATTANT);
        assertThat(testCombattant.getPrenomCombattant()).isEqualTo(UPDATED_PRENOM_COMBATTANT);
        assertThat(testCombattant.getDateNaisCombattant()).isEqualTo(UPDATED_DATE_NAIS_COMBATTANT);
        assertThat(testCombattant.getLieuNaisCombattant()).isEqualTo(UPDATED_LIEU_NAIS_COMBATTANT);
        assertThat(testCombattant.getPhotoCombattant()).isEqualTo(UPDATED_PHOTO_COMBATTANT);
        assertThat(testCombattant.getPhotoCombattantContentType()).isEqualTo(UPDATED_PHOTO_COMBATTANT_CONTENT_TYPE);
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
