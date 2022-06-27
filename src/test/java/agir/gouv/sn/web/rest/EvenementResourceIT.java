package agir.gouv.sn.web.rest;

import static agir.gouv.sn.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import agir.gouv.sn.IntegrationTest;
import agir.gouv.sn.domain.Evenement;
import agir.gouv.sn.repository.EvenementRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link EvenementResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EvenementResourceIT {

    private static final String DEFAULT_NOM_EVENEMENT = "AAAAAAAAAA";
    private static final String UPDATED_NOM_EVENEMENT = "BBBBBBBBBB";

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final String DEFAULT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTION = "BBBBBBBBBB";

    private static final String DEFAULT_DECISION = "AAAAAAAAAA";
    private static final String UPDATED_DECISION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DELAI_INSTRUCTION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DELAI_INSTRUCTION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DELAI_INSCRIPTION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DELAI_INSCRIPTION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_DELAI_VALIDATION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DELAI_VALIDATION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/evenements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EvenementRepository evenementRepository;

    @Mock
    private EvenementRepository evenementRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEvenementMockMvc;

    private Evenement evenement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Evenement createEntity(EntityManager em) {
        Evenement evenement = new Evenement()
            .nomEvenement(DEFAULT_NOM_EVENEMENT)
            .libelle(DEFAULT_LIBELLE)
            .action(DEFAULT_ACTION)
            .decision(DEFAULT_DECISION)
            .delaiInstruction(DEFAULT_DELAI_INSTRUCTION)
            .delaiInscription(DEFAULT_DELAI_INSCRIPTION)
            .delaiValidation(DEFAULT_DELAI_VALIDATION);
        return evenement;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Evenement createUpdatedEntity(EntityManager em) {
        Evenement evenement = new Evenement()
            .nomEvenement(UPDATED_NOM_EVENEMENT)
            .libelle(UPDATED_LIBELLE)
            .action(UPDATED_ACTION)
            .decision(UPDATED_DECISION)
            .delaiInstruction(UPDATED_DELAI_INSTRUCTION)
            .delaiInscription(UPDATED_DELAI_INSCRIPTION)
            .delaiValidation(UPDATED_DELAI_VALIDATION);
        return evenement;
    }

    @BeforeEach
    public void initTest() {
        evenement = createEntity(em);
    }

    @Test
    @Transactional
    void createEvenement() throws Exception {
        int databaseSizeBeforeCreate = evenementRepository.findAll().size();
        // Create the Evenement
        restEvenementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evenement)))
            .andExpect(status().isCreated());

        // Validate the Evenement in the database
        List<Evenement> evenementList = evenementRepository.findAll();
        assertThat(evenementList).hasSize(databaseSizeBeforeCreate + 1);
        Evenement testEvenement = evenementList.get(evenementList.size() - 1);
        assertThat(testEvenement.getNomEvenement()).isEqualTo(DEFAULT_NOM_EVENEMENT);
        assertThat(testEvenement.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testEvenement.getAction()).isEqualTo(DEFAULT_ACTION);
        assertThat(testEvenement.getDecision()).isEqualTo(DEFAULT_DECISION);
        assertThat(testEvenement.getDelaiInstruction()).isEqualTo(DEFAULT_DELAI_INSTRUCTION);
        assertThat(testEvenement.getDelaiInscription()).isEqualTo(DEFAULT_DELAI_INSCRIPTION);
        assertThat(testEvenement.getDelaiValidation()).isEqualTo(DEFAULT_DELAI_VALIDATION);
    }

    @Test
    @Transactional
    void createEvenementWithExistingId() throws Exception {
        // Create the Evenement with an existing ID
        evenement.setId(1L);

        int databaseSizeBeforeCreate = evenementRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEvenementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evenement)))
            .andExpect(status().isBadRequest());

        // Validate the Evenement in the database
        List<Evenement> evenementList = evenementRepository.findAll();
        assertThat(evenementList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEvenements() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get all the evenementList
        restEvenementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(evenement.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomEvenement").value(hasItem(DEFAULT_NOM_EVENEMENT)))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].decision").value(hasItem(DEFAULT_DECISION)))
            .andExpect(jsonPath("$.[*].delaiInstruction").value(hasItem(sameInstant(DEFAULT_DELAI_INSTRUCTION))))
            .andExpect(jsonPath("$.[*].delaiInscription").value(hasItem(sameInstant(DEFAULT_DELAI_INSCRIPTION))))
            .andExpect(jsonPath("$.[*].delaiValidation").value(hasItem(sameInstant(DEFAULT_DELAI_VALIDATION))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEvenementsWithEagerRelationshipsIsEnabled() throws Exception {
        when(evenementRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEvenementMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(evenementRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEvenementsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(evenementRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEvenementMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(evenementRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getEvenement() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        // Get the evenement
        restEvenementMockMvc
            .perform(get(ENTITY_API_URL_ID, evenement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(evenement.getId().intValue()))
            .andExpect(jsonPath("$.nomEvenement").value(DEFAULT_NOM_EVENEMENT))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION))
            .andExpect(jsonPath("$.decision").value(DEFAULT_DECISION))
            .andExpect(jsonPath("$.delaiInstruction").value(sameInstant(DEFAULT_DELAI_INSTRUCTION)))
            .andExpect(jsonPath("$.delaiInscription").value(sameInstant(DEFAULT_DELAI_INSCRIPTION)))
            .andExpect(jsonPath("$.delaiValidation").value(sameInstant(DEFAULT_DELAI_VALIDATION)));
    }

    @Test
    @Transactional
    void getNonExistingEvenement() throws Exception {
        // Get the evenement
        restEvenementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEvenement() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        int databaseSizeBeforeUpdate = evenementRepository.findAll().size();

        // Update the evenement
        Evenement updatedEvenement = evenementRepository.findById(evenement.getId()).get();
        // Disconnect from session so that the updates on updatedEvenement are not directly saved in db
        em.detach(updatedEvenement);
        updatedEvenement
            .nomEvenement(UPDATED_NOM_EVENEMENT)
            .libelle(UPDATED_LIBELLE)
            .action(UPDATED_ACTION)
            .decision(UPDATED_DECISION)
            .delaiInstruction(UPDATED_DELAI_INSTRUCTION)
            .delaiInscription(UPDATED_DELAI_INSCRIPTION)
            .delaiValidation(UPDATED_DELAI_VALIDATION);

        restEvenementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEvenement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEvenement))
            )
            .andExpect(status().isOk());

        // Validate the Evenement in the database
        List<Evenement> evenementList = evenementRepository.findAll();
        assertThat(evenementList).hasSize(databaseSizeBeforeUpdate);
        Evenement testEvenement = evenementList.get(evenementList.size() - 1);
        assertThat(testEvenement.getNomEvenement()).isEqualTo(UPDATED_NOM_EVENEMENT);
        assertThat(testEvenement.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testEvenement.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testEvenement.getDecision()).isEqualTo(UPDATED_DECISION);
        assertThat(testEvenement.getDelaiInstruction()).isEqualTo(UPDATED_DELAI_INSTRUCTION);
        assertThat(testEvenement.getDelaiInscription()).isEqualTo(UPDATED_DELAI_INSCRIPTION);
        assertThat(testEvenement.getDelaiValidation()).isEqualTo(UPDATED_DELAI_VALIDATION);
    }

    @Test
    @Transactional
    void putNonExistingEvenement() throws Exception {
        int databaseSizeBeforeUpdate = evenementRepository.findAll().size();
        evenement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEvenementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, evenement.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(evenement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evenement in the database
        List<Evenement> evenementList = evenementRepository.findAll();
        assertThat(evenementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEvenement() throws Exception {
        int databaseSizeBeforeUpdate = evenementRepository.findAll().size();
        evenement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvenementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(evenement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evenement in the database
        List<Evenement> evenementList = evenementRepository.findAll();
        assertThat(evenementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEvenement() throws Exception {
        int databaseSizeBeforeUpdate = evenementRepository.findAll().size();
        evenement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvenementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(evenement)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Evenement in the database
        List<Evenement> evenementList = evenementRepository.findAll();
        assertThat(evenementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEvenementWithPatch() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        int databaseSizeBeforeUpdate = evenementRepository.findAll().size();

        // Update the evenement using partial update
        Evenement partialUpdatedEvenement = new Evenement();
        partialUpdatedEvenement.setId(evenement.getId());

        partialUpdatedEvenement
            .libelle(UPDATED_LIBELLE)
            .action(UPDATED_ACTION)
            .decision(UPDATED_DECISION)
            .delaiInstruction(UPDATED_DELAI_INSTRUCTION)
            .delaiInscription(UPDATED_DELAI_INSCRIPTION)
            .delaiValidation(UPDATED_DELAI_VALIDATION);

        restEvenementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvenement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEvenement))
            )
            .andExpect(status().isOk());

        // Validate the Evenement in the database
        List<Evenement> evenementList = evenementRepository.findAll();
        assertThat(evenementList).hasSize(databaseSizeBeforeUpdate);
        Evenement testEvenement = evenementList.get(evenementList.size() - 1);
        assertThat(testEvenement.getNomEvenement()).isEqualTo(DEFAULT_NOM_EVENEMENT);
        assertThat(testEvenement.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testEvenement.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testEvenement.getDecision()).isEqualTo(UPDATED_DECISION);
        assertThat(testEvenement.getDelaiInstruction()).isEqualTo(UPDATED_DELAI_INSTRUCTION);
        assertThat(testEvenement.getDelaiInscription()).isEqualTo(UPDATED_DELAI_INSCRIPTION);
        assertThat(testEvenement.getDelaiValidation()).isEqualTo(UPDATED_DELAI_VALIDATION);
    }

    @Test
    @Transactional
    void fullUpdateEvenementWithPatch() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        int databaseSizeBeforeUpdate = evenementRepository.findAll().size();

        // Update the evenement using partial update
        Evenement partialUpdatedEvenement = new Evenement();
        partialUpdatedEvenement.setId(evenement.getId());

        partialUpdatedEvenement
            .nomEvenement(UPDATED_NOM_EVENEMENT)
            .libelle(UPDATED_LIBELLE)
            .action(UPDATED_ACTION)
            .decision(UPDATED_DECISION)
            .delaiInstruction(UPDATED_DELAI_INSTRUCTION)
            .delaiInscription(UPDATED_DELAI_INSCRIPTION)
            .delaiValidation(UPDATED_DELAI_VALIDATION);

        restEvenementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEvenement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEvenement))
            )
            .andExpect(status().isOk());

        // Validate the Evenement in the database
        List<Evenement> evenementList = evenementRepository.findAll();
        assertThat(evenementList).hasSize(databaseSizeBeforeUpdate);
        Evenement testEvenement = evenementList.get(evenementList.size() - 1);
        assertThat(testEvenement.getNomEvenement()).isEqualTo(UPDATED_NOM_EVENEMENT);
        assertThat(testEvenement.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testEvenement.getAction()).isEqualTo(UPDATED_ACTION);
        assertThat(testEvenement.getDecision()).isEqualTo(UPDATED_DECISION);
        assertThat(testEvenement.getDelaiInstruction()).isEqualTo(UPDATED_DELAI_INSTRUCTION);
        assertThat(testEvenement.getDelaiInscription()).isEqualTo(UPDATED_DELAI_INSCRIPTION);
        assertThat(testEvenement.getDelaiValidation()).isEqualTo(UPDATED_DELAI_VALIDATION);
    }

    @Test
    @Transactional
    void patchNonExistingEvenement() throws Exception {
        int databaseSizeBeforeUpdate = evenementRepository.findAll().size();
        evenement.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEvenementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, evenement.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(evenement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evenement in the database
        List<Evenement> evenementList = evenementRepository.findAll();
        assertThat(evenementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEvenement() throws Exception {
        int databaseSizeBeforeUpdate = evenementRepository.findAll().size();
        evenement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvenementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(evenement))
            )
            .andExpect(status().isBadRequest());

        // Validate the Evenement in the database
        List<Evenement> evenementList = evenementRepository.findAll();
        assertThat(evenementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEvenement() throws Exception {
        int databaseSizeBeforeUpdate = evenementRepository.findAll().size();
        evenement.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEvenementMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(evenement))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Evenement in the database
        List<Evenement> evenementList = evenementRepository.findAll();
        assertThat(evenementList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEvenement() throws Exception {
        // Initialize the database
        evenementRepository.saveAndFlush(evenement);

        int databaseSizeBeforeDelete = evenementRepository.findAll().size();

        // Delete the evenement
        restEvenementMockMvc
            .perform(delete(ENTITY_API_URL_ID, evenement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Evenement> evenementList = evenementRepository.findAll();
        assertThat(evenementList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
