package agir.gouv.sn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import agir.gouv.sn.IntegrationTest;
import agir.gouv.sn.domain.Ensegnant;
import agir.gouv.sn.repository.EnsegnantRepository;
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

/**
 * Integration tests for the {@link EnsegnantResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EnsegnantResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TEL = "AAAAAAAAAA";
    private static final String UPDATED_TEL = "BBBBBBBBBB";

    private static final String DEFAULT_TEL_1 = "AAAAAAAAAA";
    private static final String UPDATED_TEL_1 = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ensegnants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EnsegnantRepository ensegnantRepository;

    @Mock
    private EnsegnantRepository ensegnantRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEnsegnantMockMvc;

    private Ensegnant ensegnant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ensegnant createEntity(EntityManager em) {
        Ensegnant ensegnant = new Ensegnant()
            .nom(DEFAULT_NOM)
            .prenom(DEFAULT_PRENOM)
            .email(DEFAULT_EMAIL)
            .tel(DEFAULT_TEL)
            .tel1(DEFAULT_TEL_1);
        return ensegnant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ensegnant createUpdatedEntity(EntityManager em) {
        Ensegnant ensegnant = new Ensegnant()
            .nom(UPDATED_NOM)
            .prenom(UPDATED_PRENOM)
            .email(UPDATED_EMAIL)
            .tel(UPDATED_TEL)
            .tel1(UPDATED_TEL_1);
        return ensegnant;
    }

    @BeforeEach
    public void initTest() {
        ensegnant = createEntity(em);
    }

    @Test
    @Transactional
    void createEnsegnant() throws Exception {
        int databaseSizeBeforeCreate = ensegnantRepository.findAll().size();
        // Create the Ensegnant
        restEnsegnantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ensegnant)))
            .andExpect(status().isCreated());

        // Validate the Ensegnant in the database
        List<Ensegnant> ensegnantList = ensegnantRepository.findAll();
        assertThat(ensegnantList).hasSize(databaseSizeBeforeCreate + 1);
        Ensegnant testEnsegnant = ensegnantList.get(ensegnantList.size() - 1);
        assertThat(testEnsegnant.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testEnsegnant.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testEnsegnant.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEnsegnant.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testEnsegnant.getTel1()).isEqualTo(DEFAULT_TEL_1);
    }

    @Test
    @Transactional
    void createEnsegnantWithExistingId() throws Exception {
        // Create the Ensegnant with an existing ID
        ensegnant.setId(1L);

        int databaseSizeBeforeCreate = ensegnantRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnsegnantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ensegnant)))
            .andExpect(status().isBadRequest());

        // Validate the Ensegnant in the database
        List<Ensegnant> ensegnantList = ensegnantRepository.findAll();
        assertThat(ensegnantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = ensegnantRepository.findAll().size();
        // set the field null
        ensegnant.setEmail(null);

        // Create the Ensegnant, which fails.

        restEnsegnantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ensegnant)))
            .andExpect(status().isBadRequest());

        List<Ensegnant> ensegnantList = ensegnantRepository.findAll();
        assertThat(ensegnantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelIsRequired() throws Exception {
        int databaseSizeBeforeTest = ensegnantRepository.findAll().size();
        // set the field null
        ensegnant.setTel(null);

        // Create the Ensegnant, which fails.

        restEnsegnantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ensegnant)))
            .andExpect(status().isBadRequest());

        List<Ensegnant> ensegnantList = ensegnantRepository.findAll();
        assertThat(ensegnantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEnsegnants() throws Exception {
        // Initialize the database
        ensegnantRepository.saveAndFlush(ensegnant);

        // Get all the ensegnantList
        restEnsegnantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ensegnant.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL)))
            .andExpect(jsonPath("$.[*].tel1").value(hasItem(DEFAULT_TEL_1)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEnsegnantsWithEagerRelationshipsIsEnabled() throws Exception {
        when(ensegnantRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEnsegnantMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(ensegnantRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEnsegnantsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(ensegnantRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEnsegnantMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(ensegnantRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getEnsegnant() throws Exception {
        // Initialize the database
        ensegnantRepository.saveAndFlush(ensegnant);

        // Get the ensegnant
        restEnsegnantMockMvc
            .perform(get(ENTITY_API_URL_ID, ensegnant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ensegnant.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL))
            .andExpect(jsonPath("$.tel1").value(DEFAULT_TEL_1));
    }

    @Test
    @Transactional
    void getNonExistingEnsegnant() throws Exception {
        // Get the ensegnant
        restEnsegnantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEnsegnant() throws Exception {
        // Initialize the database
        ensegnantRepository.saveAndFlush(ensegnant);

        int databaseSizeBeforeUpdate = ensegnantRepository.findAll().size();

        // Update the ensegnant
        Ensegnant updatedEnsegnant = ensegnantRepository.findById(ensegnant.getId()).get();
        // Disconnect from session so that the updates on updatedEnsegnant are not directly saved in db
        em.detach(updatedEnsegnant);
        updatedEnsegnant.nom(UPDATED_NOM).prenom(UPDATED_PRENOM).email(UPDATED_EMAIL).tel(UPDATED_TEL).tel1(UPDATED_TEL_1);

        restEnsegnantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEnsegnant.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEnsegnant))
            )
            .andExpect(status().isOk());

        // Validate the Ensegnant in the database
        List<Ensegnant> ensegnantList = ensegnantRepository.findAll();
        assertThat(ensegnantList).hasSize(databaseSizeBeforeUpdate);
        Ensegnant testEnsegnant = ensegnantList.get(ensegnantList.size() - 1);
        assertThat(testEnsegnant.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testEnsegnant.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testEnsegnant.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEnsegnant.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testEnsegnant.getTel1()).isEqualTo(UPDATED_TEL_1);
    }

    @Test
    @Transactional
    void putNonExistingEnsegnant() throws Exception {
        int databaseSizeBeforeUpdate = ensegnantRepository.findAll().size();
        ensegnant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnsegnantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ensegnant.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ensegnant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ensegnant in the database
        List<Ensegnant> ensegnantList = ensegnantRepository.findAll();
        assertThat(ensegnantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEnsegnant() throws Exception {
        int databaseSizeBeforeUpdate = ensegnantRepository.findAll().size();
        ensegnant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnsegnantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ensegnant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ensegnant in the database
        List<Ensegnant> ensegnantList = ensegnantRepository.findAll();
        assertThat(ensegnantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEnsegnant() throws Exception {
        int databaseSizeBeforeUpdate = ensegnantRepository.findAll().size();
        ensegnant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnsegnantMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ensegnant)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ensegnant in the database
        List<Ensegnant> ensegnantList = ensegnantRepository.findAll();
        assertThat(ensegnantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEnsegnantWithPatch() throws Exception {
        // Initialize the database
        ensegnantRepository.saveAndFlush(ensegnant);

        int databaseSizeBeforeUpdate = ensegnantRepository.findAll().size();

        // Update the ensegnant using partial update
        Ensegnant partialUpdatedEnsegnant = new Ensegnant();
        partialUpdatedEnsegnant.setId(ensegnant.getId());

        partialUpdatedEnsegnant.nom(UPDATED_NOM).tel1(UPDATED_TEL_1);

        restEnsegnantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnsegnant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEnsegnant))
            )
            .andExpect(status().isOk());

        // Validate the Ensegnant in the database
        List<Ensegnant> ensegnantList = ensegnantRepository.findAll();
        assertThat(ensegnantList).hasSize(databaseSizeBeforeUpdate);
        Ensegnant testEnsegnant = ensegnantList.get(ensegnantList.size() - 1);
        assertThat(testEnsegnant.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testEnsegnant.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testEnsegnant.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEnsegnant.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testEnsegnant.getTel1()).isEqualTo(UPDATED_TEL_1);
    }

    @Test
    @Transactional
    void fullUpdateEnsegnantWithPatch() throws Exception {
        // Initialize the database
        ensegnantRepository.saveAndFlush(ensegnant);

        int databaseSizeBeforeUpdate = ensegnantRepository.findAll().size();

        // Update the ensegnant using partial update
        Ensegnant partialUpdatedEnsegnant = new Ensegnant();
        partialUpdatedEnsegnant.setId(ensegnant.getId());

        partialUpdatedEnsegnant.nom(UPDATED_NOM).prenom(UPDATED_PRENOM).email(UPDATED_EMAIL).tel(UPDATED_TEL).tel1(UPDATED_TEL_1);

        restEnsegnantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnsegnant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEnsegnant))
            )
            .andExpect(status().isOk());

        // Validate the Ensegnant in the database
        List<Ensegnant> ensegnantList = ensegnantRepository.findAll();
        assertThat(ensegnantList).hasSize(databaseSizeBeforeUpdate);
        Ensegnant testEnsegnant = ensegnantList.get(ensegnantList.size() - 1);
        assertThat(testEnsegnant.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testEnsegnant.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testEnsegnant.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEnsegnant.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testEnsegnant.getTel1()).isEqualTo(UPDATED_TEL_1);
    }

    @Test
    @Transactional
    void patchNonExistingEnsegnant() throws Exception {
        int databaseSizeBeforeUpdate = ensegnantRepository.findAll().size();
        ensegnant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnsegnantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ensegnant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ensegnant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ensegnant in the database
        List<Ensegnant> ensegnantList = ensegnantRepository.findAll();
        assertThat(ensegnantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEnsegnant() throws Exception {
        int databaseSizeBeforeUpdate = ensegnantRepository.findAll().size();
        ensegnant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnsegnantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ensegnant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ensegnant in the database
        List<Ensegnant> ensegnantList = ensegnantRepository.findAll();
        assertThat(ensegnantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEnsegnant() throws Exception {
        int databaseSizeBeforeUpdate = ensegnantRepository.findAll().size();
        ensegnant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnsegnantMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ensegnant))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ensegnant in the database
        List<Ensegnant> ensegnantList = ensegnantRepository.findAll();
        assertThat(ensegnantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEnsegnant() throws Exception {
        // Initialize the database
        ensegnantRepository.saveAndFlush(ensegnant);

        int databaseSizeBeforeDelete = ensegnantRepository.findAll().size();

        // Delete the ensegnant
        restEnsegnantMockMvc
            .perform(delete(ENTITY_API_URL_ID, ensegnant.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ensegnant> ensegnantList = ensegnantRepository.findAll();
        assertThat(ensegnantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
