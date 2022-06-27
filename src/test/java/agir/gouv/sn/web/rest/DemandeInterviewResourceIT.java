package agir.gouv.sn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import agir.gouv.sn.IntegrationTest;
import agir.gouv.sn.domain.DemandeInterview;
import agir.gouv.sn.repository.DemandeInterviewRepository;
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

/**
 * Integration tests for the {@link DemandeInterviewResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DemandeInterviewResourceIT {

    private static final String DEFAULT_NOM_JOURNALISTE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_JOURNALISTE = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM_JOURNALISTE = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM_JOURNALISTE = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_SOCIETE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_SOCIETE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_JOURNALITE = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_JOURNALITE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_INTERVIEW = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_INTERVIEW = LocalDate.now(ZoneId.systemDefault());

    private static final Boolean DEFAULT_ETAT_DEMANDE = false;
    private static final Boolean UPDATED_ETAT_DEMANDE = true;

    private static final String ENTITY_API_URL = "/api/demande-interviews";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DemandeInterviewRepository demandeInterviewRepository;

    @Mock
    private DemandeInterviewRepository demandeInterviewRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDemandeInterviewMockMvc;

    private DemandeInterview demandeInterview;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DemandeInterview createEntity(EntityManager em) {
        DemandeInterview demandeInterview = new DemandeInterview()
            .nomJournaliste(DEFAULT_NOM_JOURNALISTE)
            .prenomJournaliste(DEFAULT_PRENOM_JOURNALISTE)
            .nomSociete(DEFAULT_NOM_SOCIETE)
            .emailJournalite(DEFAULT_EMAIL_JOURNALITE)
            .dateInterview(DEFAULT_DATE_INTERVIEW)
            .etatDemande(DEFAULT_ETAT_DEMANDE);
        return demandeInterview;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DemandeInterview createUpdatedEntity(EntityManager em) {
        DemandeInterview demandeInterview = new DemandeInterview()
            .nomJournaliste(UPDATED_NOM_JOURNALISTE)
            .prenomJournaliste(UPDATED_PRENOM_JOURNALISTE)
            .nomSociete(UPDATED_NOM_SOCIETE)
            .emailJournalite(UPDATED_EMAIL_JOURNALITE)
            .dateInterview(UPDATED_DATE_INTERVIEW)
            .etatDemande(UPDATED_ETAT_DEMANDE);
        return demandeInterview;
    }

    @BeforeEach
    public void initTest() {
        demandeInterview = createEntity(em);
    }

    @Test
    @Transactional
    void createDemandeInterview() throws Exception {
        int databaseSizeBeforeCreate = demandeInterviewRepository.findAll().size();
        // Create the DemandeInterview
        restDemandeInterviewMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demandeInterview))
            )
            .andExpect(status().isCreated());

        // Validate the DemandeInterview in the database
        List<DemandeInterview> demandeInterviewList = demandeInterviewRepository.findAll();
        assertThat(demandeInterviewList).hasSize(databaseSizeBeforeCreate + 1);
        DemandeInterview testDemandeInterview = demandeInterviewList.get(demandeInterviewList.size() - 1);
        assertThat(testDemandeInterview.getNomJournaliste()).isEqualTo(DEFAULT_NOM_JOURNALISTE);
        assertThat(testDemandeInterview.getPrenomJournaliste()).isEqualTo(DEFAULT_PRENOM_JOURNALISTE);
        assertThat(testDemandeInterview.getNomSociete()).isEqualTo(DEFAULT_NOM_SOCIETE);
        assertThat(testDemandeInterview.getEmailJournalite()).isEqualTo(DEFAULT_EMAIL_JOURNALITE);
        assertThat(testDemandeInterview.getDateInterview()).isEqualTo(DEFAULT_DATE_INTERVIEW);
        assertThat(testDemandeInterview.getEtatDemande()).isEqualTo(DEFAULT_ETAT_DEMANDE);
    }

    @Test
    @Transactional
    void createDemandeInterviewWithExistingId() throws Exception {
        // Create the DemandeInterview with an existing ID
        demandeInterview.setId(1L);

        int databaseSizeBeforeCreate = demandeInterviewRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDemandeInterviewMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demandeInterview))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeInterview in the database
        List<DemandeInterview> demandeInterviewList = demandeInterviewRepository.findAll();
        assertThat(demandeInterviewList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomSocieteIsRequired() throws Exception {
        int databaseSizeBeforeTest = demandeInterviewRepository.findAll().size();
        // set the field null
        demandeInterview.setNomSociete(null);

        // Create the DemandeInterview, which fails.

        restDemandeInterviewMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demandeInterview))
            )
            .andExpect(status().isBadRequest());

        List<DemandeInterview> demandeInterviewList = demandeInterviewRepository.findAll();
        assertThat(demandeInterviewList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailJournaliteIsRequired() throws Exception {
        int databaseSizeBeforeTest = demandeInterviewRepository.findAll().size();
        // set the field null
        demandeInterview.setEmailJournalite(null);

        // Create the DemandeInterview, which fails.

        restDemandeInterviewMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demandeInterview))
            )
            .andExpect(status().isBadRequest());

        List<DemandeInterview> demandeInterviewList = demandeInterviewRepository.findAll();
        assertThat(demandeInterviewList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDemandeInterviews() throws Exception {
        // Initialize the database
        demandeInterviewRepository.saveAndFlush(demandeInterview);

        // Get all the demandeInterviewList
        restDemandeInterviewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(demandeInterview.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomJournaliste").value(hasItem(DEFAULT_NOM_JOURNALISTE)))
            .andExpect(jsonPath("$.[*].prenomJournaliste").value(hasItem(DEFAULT_PRENOM_JOURNALISTE)))
            .andExpect(jsonPath("$.[*].nomSociete").value(hasItem(DEFAULT_NOM_SOCIETE)))
            .andExpect(jsonPath("$.[*].emailJournalite").value(hasItem(DEFAULT_EMAIL_JOURNALITE)))
            .andExpect(jsonPath("$.[*].dateInterview").value(hasItem(DEFAULT_DATE_INTERVIEW.toString())))
            .andExpect(jsonPath("$.[*].etatDemande").value(hasItem(DEFAULT_ETAT_DEMANDE.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDemandeInterviewsWithEagerRelationshipsIsEnabled() throws Exception {
        when(demandeInterviewRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDemandeInterviewMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(demandeInterviewRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDemandeInterviewsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(demandeInterviewRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDemandeInterviewMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(demandeInterviewRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getDemandeInterview() throws Exception {
        // Initialize the database
        demandeInterviewRepository.saveAndFlush(demandeInterview);

        // Get the demandeInterview
        restDemandeInterviewMockMvc
            .perform(get(ENTITY_API_URL_ID, demandeInterview.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(demandeInterview.getId().intValue()))
            .andExpect(jsonPath("$.nomJournaliste").value(DEFAULT_NOM_JOURNALISTE))
            .andExpect(jsonPath("$.prenomJournaliste").value(DEFAULT_PRENOM_JOURNALISTE))
            .andExpect(jsonPath("$.nomSociete").value(DEFAULT_NOM_SOCIETE))
            .andExpect(jsonPath("$.emailJournalite").value(DEFAULT_EMAIL_JOURNALITE))
            .andExpect(jsonPath("$.dateInterview").value(DEFAULT_DATE_INTERVIEW.toString()))
            .andExpect(jsonPath("$.etatDemande").value(DEFAULT_ETAT_DEMANDE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingDemandeInterview() throws Exception {
        // Get the demandeInterview
        restDemandeInterviewMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDemandeInterview() throws Exception {
        // Initialize the database
        demandeInterviewRepository.saveAndFlush(demandeInterview);

        int databaseSizeBeforeUpdate = demandeInterviewRepository.findAll().size();

        // Update the demandeInterview
        DemandeInterview updatedDemandeInterview = demandeInterviewRepository.findById(demandeInterview.getId()).get();
        // Disconnect from session so that the updates on updatedDemandeInterview are not directly saved in db
        em.detach(updatedDemandeInterview);
        updatedDemandeInterview
            .nomJournaliste(UPDATED_NOM_JOURNALISTE)
            .prenomJournaliste(UPDATED_PRENOM_JOURNALISTE)
            .nomSociete(UPDATED_NOM_SOCIETE)
            .emailJournalite(UPDATED_EMAIL_JOURNALITE)
            .dateInterview(UPDATED_DATE_INTERVIEW)
            .etatDemande(UPDATED_ETAT_DEMANDE);

        restDemandeInterviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDemandeInterview.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDemandeInterview))
            )
            .andExpect(status().isOk());

        // Validate the DemandeInterview in the database
        List<DemandeInterview> demandeInterviewList = demandeInterviewRepository.findAll();
        assertThat(demandeInterviewList).hasSize(databaseSizeBeforeUpdate);
        DemandeInterview testDemandeInterview = demandeInterviewList.get(demandeInterviewList.size() - 1);
        assertThat(testDemandeInterview.getNomJournaliste()).isEqualTo(UPDATED_NOM_JOURNALISTE);
        assertThat(testDemandeInterview.getPrenomJournaliste()).isEqualTo(UPDATED_PRENOM_JOURNALISTE);
        assertThat(testDemandeInterview.getNomSociete()).isEqualTo(UPDATED_NOM_SOCIETE);
        assertThat(testDemandeInterview.getEmailJournalite()).isEqualTo(UPDATED_EMAIL_JOURNALITE);
        assertThat(testDemandeInterview.getDateInterview()).isEqualTo(UPDATED_DATE_INTERVIEW);
        assertThat(testDemandeInterview.getEtatDemande()).isEqualTo(UPDATED_ETAT_DEMANDE);
    }

    @Test
    @Transactional
    void putNonExistingDemandeInterview() throws Exception {
        int databaseSizeBeforeUpdate = demandeInterviewRepository.findAll().size();
        demandeInterview.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemandeInterviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, demandeInterview.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandeInterview))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeInterview in the database
        List<DemandeInterview> demandeInterviewList = demandeInterviewRepository.findAll();
        assertThat(demandeInterviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDemandeInterview() throws Exception {
        int databaseSizeBeforeUpdate = demandeInterviewRepository.findAll().size();
        demandeInterview.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeInterviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demandeInterview))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeInterview in the database
        List<DemandeInterview> demandeInterviewList = demandeInterviewRepository.findAll();
        assertThat(demandeInterviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDemandeInterview() throws Exception {
        int databaseSizeBeforeUpdate = demandeInterviewRepository.findAll().size();
        demandeInterview.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeInterviewMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(demandeInterview))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DemandeInterview in the database
        List<DemandeInterview> demandeInterviewList = demandeInterviewRepository.findAll();
        assertThat(demandeInterviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDemandeInterviewWithPatch() throws Exception {
        // Initialize the database
        demandeInterviewRepository.saveAndFlush(demandeInterview);

        int databaseSizeBeforeUpdate = demandeInterviewRepository.findAll().size();

        // Update the demandeInterview using partial update
        DemandeInterview partialUpdatedDemandeInterview = new DemandeInterview();
        partialUpdatedDemandeInterview.setId(demandeInterview.getId());

        partialUpdatedDemandeInterview.nomJournaliste(UPDATED_NOM_JOURNALISTE).dateInterview(UPDATED_DATE_INTERVIEW);

        restDemandeInterviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDemandeInterview.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDemandeInterview))
            )
            .andExpect(status().isOk());

        // Validate the DemandeInterview in the database
        List<DemandeInterview> demandeInterviewList = demandeInterviewRepository.findAll();
        assertThat(demandeInterviewList).hasSize(databaseSizeBeforeUpdate);
        DemandeInterview testDemandeInterview = demandeInterviewList.get(demandeInterviewList.size() - 1);
        assertThat(testDemandeInterview.getNomJournaliste()).isEqualTo(UPDATED_NOM_JOURNALISTE);
        assertThat(testDemandeInterview.getPrenomJournaliste()).isEqualTo(DEFAULT_PRENOM_JOURNALISTE);
        assertThat(testDemandeInterview.getNomSociete()).isEqualTo(DEFAULT_NOM_SOCIETE);
        assertThat(testDemandeInterview.getEmailJournalite()).isEqualTo(DEFAULT_EMAIL_JOURNALITE);
        assertThat(testDemandeInterview.getDateInterview()).isEqualTo(UPDATED_DATE_INTERVIEW);
        assertThat(testDemandeInterview.getEtatDemande()).isEqualTo(DEFAULT_ETAT_DEMANDE);
    }

    @Test
    @Transactional
    void fullUpdateDemandeInterviewWithPatch() throws Exception {
        // Initialize the database
        demandeInterviewRepository.saveAndFlush(demandeInterview);

        int databaseSizeBeforeUpdate = demandeInterviewRepository.findAll().size();

        // Update the demandeInterview using partial update
        DemandeInterview partialUpdatedDemandeInterview = new DemandeInterview();
        partialUpdatedDemandeInterview.setId(demandeInterview.getId());

        partialUpdatedDemandeInterview
            .nomJournaliste(UPDATED_NOM_JOURNALISTE)
            .prenomJournaliste(UPDATED_PRENOM_JOURNALISTE)
            .nomSociete(UPDATED_NOM_SOCIETE)
            .emailJournalite(UPDATED_EMAIL_JOURNALITE)
            .dateInterview(UPDATED_DATE_INTERVIEW)
            .etatDemande(UPDATED_ETAT_DEMANDE);

        restDemandeInterviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDemandeInterview.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDemandeInterview))
            )
            .andExpect(status().isOk());

        // Validate the DemandeInterview in the database
        List<DemandeInterview> demandeInterviewList = demandeInterviewRepository.findAll();
        assertThat(demandeInterviewList).hasSize(databaseSizeBeforeUpdate);
        DemandeInterview testDemandeInterview = demandeInterviewList.get(demandeInterviewList.size() - 1);
        assertThat(testDemandeInterview.getNomJournaliste()).isEqualTo(UPDATED_NOM_JOURNALISTE);
        assertThat(testDemandeInterview.getPrenomJournaliste()).isEqualTo(UPDATED_PRENOM_JOURNALISTE);
        assertThat(testDemandeInterview.getNomSociete()).isEqualTo(UPDATED_NOM_SOCIETE);
        assertThat(testDemandeInterview.getEmailJournalite()).isEqualTo(UPDATED_EMAIL_JOURNALITE);
        assertThat(testDemandeInterview.getDateInterview()).isEqualTo(UPDATED_DATE_INTERVIEW);
        assertThat(testDemandeInterview.getEtatDemande()).isEqualTo(UPDATED_ETAT_DEMANDE);
    }

    @Test
    @Transactional
    void patchNonExistingDemandeInterview() throws Exception {
        int databaseSizeBeforeUpdate = demandeInterviewRepository.findAll().size();
        demandeInterview.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemandeInterviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, demandeInterview.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demandeInterview))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeInterview in the database
        List<DemandeInterview> demandeInterviewList = demandeInterviewRepository.findAll();
        assertThat(demandeInterviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDemandeInterview() throws Exception {
        int databaseSizeBeforeUpdate = demandeInterviewRepository.findAll().size();
        demandeInterview.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeInterviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demandeInterview))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemandeInterview in the database
        List<DemandeInterview> demandeInterviewList = demandeInterviewRepository.findAll();
        assertThat(demandeInterviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDemandeInterview() throws Exception {
        int databaseSizeBeforeUpdate = demandeInterviewRepository.findAll().size();
        demandeInterview.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemandeInterviewMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demandeInterview))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DemandeInterview in the database
        List<DemandeInterview> demandeInterviewList = demandeInterviewRepository.findAll();
        assertThat(demandeInterviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDemandeInterview() throws Exception {
        // Initialize the database
        demandeInterviewRepository.saveAndFlush(demandeInterview);

        int databaseSizeBeforeDelete = demandeInterviewRepository.findAll().size();

        // Delete the demandeInterview
        restDemandeInterviewMockMvc
            .perform(delete(ENTITY_API_URL_ID, demandeInterview.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DemandeInterview> demandeInterviewList = demandeInterviewRepository.findAll();
        assertThat(demandeInterviewList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
