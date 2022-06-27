package agir.gouv.sn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import agir.gouv.sn.IntegrationTest;
import agir.gouv.sn.domain.Entrepreneur;
import agir.gouv.sn.repository.EntrepreneurRepository;
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
 * Integration tests for the {@link EntrepreneurResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EntrepreneurResourceIT {

    private static final String DEFAULT_NOM_ENTREPRENEUR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_ENTREPRENEUR = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM_ENTREPRENEUR = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM_ENTREPRENEUR = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_ENTREPRENEUR = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_ENTREPRENEUR = "BBBBBBBBBB";

    private static final String DEFAULT_TEL_ENTREPRENEUR = "AAAAAAAAAA";
    private static final String UPDATED_TEL_ENTREPRENEUR = "BBBBBBBBBB";

    private static final String DEFAULT_TEL_1_ENTREPRENEUR = "AAAAAAAAAA";
    private static final String UPDATED_TEL_1_ENTREPRENEUR = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/entrepreneurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EntrepreneurRepository entrepreneurRepository;

    @Mock
    private EntrepreneurRepository entrepreneurRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEntrepreneurMockMvc;

    private Entrepreneur entrepreneur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Entrepreneur createEntity(EntityManager em) {
        Entrepreneur entrepreneur = new Entrepreneur()
            .nomEntrepreneur(DEFAULT_NOM_ENTREPRENEUR)
            .prenomEntrepreneur(DEFAULT_PRENOM_ENTREPRENEUR)
            .emailEntrepreneur(DEFAULT_EMAIL_ENTREPRENEUR)
            .telEntrepreneur(DEFAULT_TEL_ENTREPRENEUR)
            .tel1Entrepreneur(DEFAULT_TEL_1_ENTREPRENEUR);
        return entrepreneur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Entrepreneur createUpdatedEntity(EntityManager em) {
        Entrepreneur entrepreneur = new Entrepreneur()
            .nomEntrepreneur(UPDATED_NOM_ENTREPRENEUR)
            .prenomEntrepreneur(UPDATED_PRENOM_ENTREPRENEUR)
            .emailEntrepreneur(UPDATED_EMAIL_ENTREPRENEUR)
            .telEntrepreneur(UPDATED_TEL_ENTREPRENEUR)
            .tel1Entrepreneur(UPDATED_TEL_1_ENTREPRENEUR);
        return entrepreneur;
    }

    @BeforeEach
    public void initTest() {
        entrepreneur = createEntity(em);
    }

    @Test
    @Transactional
    void createEntrepreneur() throws Exception {
        int databaseSizeBeforeCreate = entrepreneurRepository.findAll().size();
        // Create the Entrepreneur
        restEntrepreneurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(entrepreneur)))
            .andExpect(status().isCreated());

        // Validate the Entrepreneur in the database
        List<Entrepreneur> entrepreneurList = entrepreneurRepository.findAll();
        assertThat(entrepreneurList).hasSize(databaseSizeBeforeCreate + 1);
        Entrepreneur testEntrepreneur = entrepreneurList.get(entrepreneurList.size() - 1);
        assertThat(testEntrepreneur.getNomEntrepreneur()).isEqualTo(DEFAULT_NOM_ENTREPRENEUR);
        assertThat(testEntrepreneur.getPrenomEntrepreneur()).isEqualTo(DEFAULT_PRENOM_ENTREPRENEUR);
        assertThat(testEntrepreneur.getEmailEntrepreneur()).isEqualTo(DEFAULT_EMAIL_ENTREPRENEUR);
        assertThat(testEntrepreneur.getTelEntrepreneur()).isEqualTo(DEFAULT_TEL_ENTREPRENEUR);
        assertThat(testEntrepreneur.getTel1Entrepreneur()).isEqualTo(DEFAULT_TEL_1_ENTREPRENEUR);
    }

    @Test
    @Transactional
    void createEntrepreneurWithExistingId() throws Exception {
        // Create the Entrepreneur with an existing ID
        entrepreneur.setId(1L);

        int databaseSizeBeforeCreate = entrepreneurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntrepreneurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(entrepreneur)))
            .andExpect(status().isBadRequest());

        // Validate the Entrepreneur in the database
        List<Entrepreneur> entrepreneurList = entrepreneurRepository.findAll();
        assertThat(entrepreneurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomEntrepreneurIsRequired() throws Exception {
        int databaseSizeBeforeTest = entrepreneurRepository.findAll().size();
        // set the field null
        entrepreneur.setNomEntrepreneur(null);

        // Create the Entrepreneur, which fails.

        restEntrepreneurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(entrepreneur)))
            .andExpect(status().isBadRequest());

        List<Entrepreneur> entrepreneurList = entrepreneurRepository.findAll();
        assertThat(entrepreneurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelEntrepreneurIsRequired() throws Exception {
        int databaseSizeBeforeTest = entrepreneurRepository.findAll().size();
        // set the field null
        entrepreneur.setTelEntrepreneur(null);

        // Create the Entrepreneur, which fails.

        restEntrepreneurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(entrepreneur)))
            .andExpect(status().isBadRequest());

        List<Entrepreneur> entrepreneurList = entrepreneurRepository.findAll();
        assertThat(entrepreneurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEntrepreneurs() throws Exception {
        // Initialize the database
        entrepreneurRepository.saveAndFlush(entrepreneur);

        // Get all the entrepreneurList
        restEntrepreneurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entrepreneur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomEntrepreneur").value(hasItem(DEFAULT_NOM_ENTREPRENEUR)))
            .andExpect(jsonPath("$.[*].prenomEntrepreneur").value(hasItem(DEFAULT_PRENOM_ENTREPRENEUR)))
            .andExpect(jsonPath("$.[*].emailEntrepreneur").value(hasItem(DEFAULT_EMAIL_ENTREPRENEUR)))
            .andExpect(jsonPath("$.[*].telEntrepreneur").value(hasItem(DEFAULT_TEL_ENTREPRENEUR)))
            .andExpect(jsonPath("$.[*].tel1Entrepreneur").value(hasItem(DEFAULT_TEL_1_ENTREPRENEUR)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEntrepreneursWithEagerRelationshipsIsEnabled() throws Exception {
        when(entrepreneurRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEntrepreneurMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(entrepreneurRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEntrepreneursWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(entrepreneurRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEntrepreneurMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(entrepreneurRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getEntrepreneur() throws Exception {
        // Initialize the database
        entrepreneurRepository.saveAndFlush(entrepreneur);

        // Get the entrepreneur
        restEntrepreneurMockMvc
            .perform(get(ENTITY_API_URL_ID, entrepreneur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(entrepreneur.getId().intValue()))
            .andExpect(jsonPath("$.nomEntrepreneur").value(DEFAULT_NOM_ENTREPRENEUR))
            .andExpect(jsonPath("$.prenomEntrepreneur").value(DEFAULT_PRENOM_ENTREPRENEUR))
            .andExpect(jsonPath("$.emailEntrepreneur").value(DEFAULT_EMAIL_ENTREPRENEUR))
            .andExpect(jsonPath("$.telEntrepreneur").value(DEFAULT_TEL_ENTREPRENEUR))
            .andExpect(jsonPath("$.tel1Entrepreneur").value(DEFAULT_TEL_1_ENTREPRENEUR));
    }

    @Test
    @Transactional
    void getNonExistingEntrepreneur() throws Exception {
        // Get the entrepreneur
        restEntrepreneurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEntrepreneur() throws Exception {
        // Initialize the database
        entrepreneurRepository.saveAndFlush(entrepreneur);

        int databaseSizeBeforeUpdate = entrepreneurRepository.findAll().size();

        // Update the entrepreneur
        Entrepreneur updatedEntrepreneur = entrepreneurRepository.findById(entrepreneur.getId()).get();
        // Disconnect from session so that the updates on updatedEntrepreneur are not directly saved in db
        em.detach(updatedEntrepreneur);
        updatedEntrepreneur
            .nomEntrepreneur(UPDATED_NOM_ENTREPRENEUR)
            .prenomEntrepreneur(UPDATED_PRENOM_ENTREPRENEUR)
            .emailEntrepreneur(UPDATED_EMAIL_ENTREPRENEUR)
            .telEntrepreneur(UPDATED_TEL_ENTREPRENEUR)
            .tel1Entrepreneur(UPDATED_TEL_1_ENTREPRENEUR);

        restEntrepreneurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEntrepreneur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEntrepreneur))
            )
            .andExpect(status().isOk());

        // Validate the Entrepreneur in the database
        List<Entrepreneur> entrepreneurList = entrepreneurRepository.findAll();
        assertThat(entrepreneurList).hasSize(databaseSizeBeforeUpdate);
        Entrepreneur testEntrepreneur = entrepreneurList.get(entrepreneurList.size() - 1);
        assertThat(testEntrepreneur.getNomEntrepreneur()).isEqualTo(UPDATED_NOM_ENTREPRENEUR);
        assertThat(testEntrepreneur.getPrenomEntrepreneur()).isEqualTo(UPDATED_PRENOM_ENTREPRENEUR);
        assertThat(testEntrepreneur.getEmailEntrepreneur()).isEqualTo(UPDATED_EMAIL_ENTREPRENEUR);
        assertThat(testEntrepreneur.getTelEntrepreneur()).isEqualTo(UPDATED_TEL_ENTREPRENEUR);
        assertThat(testEntrepreneur.getTel1Entrepreneur()).isEqualTo(UPDATED_TEL_1_ENTREPRENEUR);
    }

    @Test
    @Transactional
    void putNonExistingEntrepreneur() throws Exception {
        int databaseSizeBeforeUpdate = entrepreneurRepository.findAll().size();
        entrepreneur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEntrepreneurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, entrepreneur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(entrepreneur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Entrepreneur in the database
        List<Entrepreneur> entrepreneurList = entrepreneurRepository.findAll();
        assertThat(entrepreneurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEntrepreneur() throws Exception {
        int databaseSizeBeforeUpdate = entrepreneurRepository.findAll().size();
        entrepreneur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntrepreneurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(entrepreneur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Entrepreneur in the database
        List<Entrepreneur> entrepreneurList = entrepreneurRepository.findAll();
        assertThat(entrepreneurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEntrepreneur() throws Exception {
        int databaseSizeBeforeUpdate = entrepreneurRepository.findAll().size();
        entrepreneur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntrepreneurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(entrepreneur)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Entrepreneur in the database
        List<Entrepreneur> entrepreneurList = entrepreneurRepository.findAll();
        assertThat(entrepreneurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEntrepreneurWithPatch() throws Exception {
        // Initialize the database
        entrepreneurRepository.saveAndFlush(entrepreneur);

        int databaseSizeBeforeUpdate = entrepreneurRepository.findAll().size();

        // Update the entrepreneur using partial update
        Entrepreneur partialUpdatedEntrepreneur = new Entrepreneur();
        partialUpdatedEntrepreneur.setId(entrepreneur.getId());

        partialUpdatedEntrepreneur.telEntrepreneur(UPDATED_TEL_ENTREPRENEUR);

        restEntrepreneurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEntrepreneur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEntrepreneur))
            )
            .andExpect(status().isOk());

        // Validate the Entrepreneur in the database
        List<Entrepreneur> entrepreneurList = entrepreneurRepository.findAll();
        assertThat(entrepreneurList).hasSize(databaseSizeBeforeUpdate);
        Entrepreneur testEntrepreneur = entrepreneurList.get(entrepreneurList.size() - 1);
        assertThat(testEntrepreneur.getNomEntrepreneur()).isEqualTo(DEFAULT_NOM_ENTREPRENEUR);
        assertThat(testEntrepreneur.getPrenomEntrepreneur()).isEqualTo(DEFAULT_PRENOM_ENTREPRENEUR);
        assertThat(testEntrepreneur.getEmailEntrepreneur()).isEqualTo(DEFAULT_EMAIL_ENTREPRENEUR);
        assertThat(testEntrepreneur.getTelEntrepreneur()).isEqualTo(UPDATED_TEL_ENTREPRENEUR);
        assertThat(testEntrepreneur.getTel1Entrepreneur()).isEqualTo(DEFAULT_TEL_1_ENTREPRENEUR);
    }

    @Test
    @Transactional
    void fullUpdateEntrepreneurWithPatch() throws Exception {
        // Initialize the database
        entrepreneurRepository.saveAndFlush(entrepreneur);

        int databaseSizeBeforeUpdate = entrepreneurRepository.findAll().size();

        // Update the entrepreneur using partial update
        Entrepreneur partialUpdatedEntrepreneur = new Entrepreneur();
        partialUpdatedEntrepreneur.setId(entrepreneur.getId());

        partialUpdatedEntrepreneur
            .nomEntrepreneur(UPDATED_NOM_ENTREPRENEUR)
            .prenomEntrepreneur(UPDATED_PRENOM_ENTREPRENEUR)
            .emailEntrepreneur(UPDATED_EMAIL_ENTREPRENEUR)
            .telEntrepreneur(UPDATED_TEL_ENTREPRENEUR)
            .tel1Entrepreneur(UPDATED_TEL_1_ENTREPRENEUR);

        restEntrepreneurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEntrepreneur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEntrepreneur))
            )
            .andExpect(status().isOk());

        // Validate the Entrepreneur in the database
        List<Entrepreneur> entrepreneurList = entrepreneurRepository.findAll();
        assertThat(entrepreneurList).hasSize(databaseSizeBeforeUpdate);
        Entrepreneur testEntrepreneur = entrepreneurList.get(entrepreneurList.size() - 1);
        assertThat(testEntrepreneur.getNomEntrepreneur()).isEqualTo(UPDATED_NOM_ENTREPRENEUR);
        assertThat(testEntrepreneur.getPrenomEntrepreneur()).isEqualTo(UPDATED_PRENOM_ENTREPRENEUR);
        assertThat(testEntrepreneur.getEmailEntrepreneur()).isEqualTo(UPDATED_EMAIL_ENTREPRENEUR);
        assertThat(testEntrepreneur.getTelEntrepreneur()).isEqualTo(UPDATED_TEL_ENTREPRENEUR);
        assertThat(testEntrepreneur.getTel1Entrepreneur()).isEqualTo(UPDATED_TEL_1_ENTREPRENEUR);
    }

    @Test
    @Transactional
    void patchNonExistingEntrepreneur() throws Exception {
        int databaseSizeBeforeUpdate = entrepreneurRepository.findAll().size();
        entrepreneur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEntrepreneurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, entrepreneur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(entrepreneur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Entrepreneur in the database
        List<Entrepreneur> entrepreneurList = entrepreneurRepository.findAll();
        assertThat(entrepreneurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEntrepreneur() throws Exception {
        int databaseSizeBeforeUpdate = entrepreneurRepository.findAll().size();
        entrepreneur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntrepreneurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(entrepreneur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Entrepreneur in the database
        List<Entrepreneur> entrepreneurList = entrepreneurRepository.findAll();
        assertThat(entrepreneurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEntrepreneur() throws Exception {
        int databaseSizeBeforeUpdate = entrepreneurRepository.findAll().size();
        entrepreneur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEntrepreneurMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(entrepreneur))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Entrepreneur in the database
        List<Entrepreneur> entrepreneurList = entrepreneurRepository.findAll();
        assertThat(entrepreneurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEntrepreneur() throws Exception {
        // Initialize the database
        entrepreneurRepository.saveAndFlush(entrepreneur);

        int databaseSizeBeforeDelete = entrepreneurRepository.findAll().size();

        // Delete the entrepreneur
        restEntrepreneurMockMvc
            .perform(delete(ENTITY_API_URL_ID, entrepreneur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Entrepreneur> entrepreneurList = entrepreneurRepository.findAll();
        assertThat(entrepreneurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
