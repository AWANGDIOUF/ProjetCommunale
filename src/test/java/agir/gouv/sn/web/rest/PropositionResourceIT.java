package agir.gouv.sn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import agir.gouv.sn.IntegrationTest;
import agir.gouv.sn.domain.Proposition;
import agir.gouv.sn.repository.PropositionRepository;
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
 * Integration tests for the {@link PropositionResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PropositionResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/propositions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PropositionRepository propositionRepository;

    @Mock
    private PropositionRepository propositionRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPropositionMockMvc;

    private Proposition proposition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proposition createEntity(EntityManager em) {
        Proposition proposition = new Proposition().description(DEFAULT_DESCRIPTION);
        return proposition;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Proposition createUpdatedEntity(EntityManager em) {
        Proposition proposition = new Proposition().description(UPDATED_DESCRIPTION);
        return proposition;
    }

    @BeforeEach
    public void initTest() {
        proposition = createEntity(em);
    }

    @Test
    @Transactional
    void createProposition() throws Exception {
        int databaseSizeBeforeCreate = propositionRepository.findAll().size();
        // Create the Proposition
        restPropositionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proposition)))
            .andExpect(status().isCreated());

        // Validate the Proposition in the database
        List<Proposition> propositionList = propositionRepository.findAll();
        assertThat(propositionList).hasSize(databaseSizeBeforeCreate + 1);
        Proposition testProposition = propositionList.get(propositionList.size() - 1);
        assertThat(testProposition.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createPropositionWithExistingId() throws Exception {
        // Create the Proposition with an existing ID
        proposition.setId(1L);

        int databaseSizeBeforeCreate = propositionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPropositionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proposition)))
            .andExpect(status().isBadRequest());

        // Validate the Proposition in the database
        List<Proposition> propositionList = propositionRepository.findAll();
        assertThat(propositionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPropositions() throws Exception {
        // Initialize the database
        propositionRepository.saveAndFlush(proposition);

        // Get all the propositionList
        restPropositionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proposition.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPropositionsWithEagerRelationshipsIsEnabled() throws Exception {
        when(propositionRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPropositionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(propositionRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPropositionsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(propositionRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPropositionMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(propositionRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getProposition() throws Exception {
        // Initialize the database
        propositionRepository.saveAndFlush(proposition);

        // Get the proposition
        restPropositionMockMvc
            .perform(get(ENTITY_API_URL_ID, proposition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(proposition.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProposition() throws Exception {
        // Get the proposition
        restPropositionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProposition() throws Exception {
        // Initialize the database
        propositionRepository.saveAndFlush(proposition);

        int databaseSizeBeforeUpdate = propositionRepository.findAll().size();

        // Update the proposition
        Proposition updatedProposition = propositionRepository.findById(proposition.getId()).get();
        // Disconnect from session so that the updates on updatedProposition are not directly saved in db
        em.detach(updatedProposition);
        updatedProposition.description(UPDATED_DESCRIPTION);

        restPropositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProposition.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProposition))
            )
            .andExpect(status().isOk());

        // Validate the Proposition in the database
        List<Proposition> propositionList = propositionRepository.findAll();
        assertThat(propositionList).hasSize(databaseSizeBeforeUpdate);
        Proposition testProposition = propositionList.get(propositionList.size() - 1);
        assertThat(testProposition.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingProposition() throws Exception {
        int databaseSizeBeforeUpdate = propositionRepository.findAll().size();
        proposition.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPropositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, proposition.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(proposition))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proposition in the database
        List<Proposition> propositionList = propositionRepository.findAll();
        assertThat(propositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProposition() throws Exception {
        int databaseSizeBeforeUpdate = propositionRepository.findAll().size();
        proposition.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPropositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(proposition))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proposition in the database
        List<Proposition> propositionList = propositionRepository.findAll();
        assertThat(propositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProposition() throws Exception {
        int databaseSizeBeforeUpdate = propositionRepository.findAll().size();
        proposition.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPropositionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(proposition)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Proposition in the database
        List<Proposition> propositionList = propositionRepository.findAll();
        assertThat(propositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePropositionWithPatch() throws Exception {
        // Initialize the database
        propositionRepository.saveAndFlush(proposition);

        int databaseSizeBeforeUpdate = propositionRepository.findAll().size();

        // Update the proposition using partial update
        Proposition partialUpdatedProposition = new Proposition();
        partialUpdatedProposition.setId(proposition.getId());

        partialUpdatedProposition.description(UPDATED_DESCRIPTION);

        restPropositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProposition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProposition))
            )
            .andExpect(status().isOk());

        // Validate the Proposition in the database
        List<Proposition> propositionList = propositionRepository.findAll();
        assertThat(propositionList).hasSize(databaseSizeBeforeUpdate);
        Proposition testProposition = propositionList.get(propositionList.size() - 1);
        assertThat(testProposition.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdatePropositionWithPatch() throws Exception {
        // Initialize the database
        propositionRepository.saveAndFlush(proposition);

        int databaseSizeBeforeUpdate = propositionRepository.findAll().size();

        // Update the proposition using partial update
        Proposition partialUpdatedProposition = new Proposition();
        partialUpdatedProposition.setId(proposition.getId());

        partialUpdatedProposition.description(UPDATED_DESCRIPTION);

        restPropositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProposition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProposition))
            )
            .andExpect(status().isOk());

        // Validate the Proposition in the database
        List<Proposition> propositionList = propositionRepository.findAll();
        assertThat(propositionList).hasSize(databaseSizeBeforeUpdate);
        Proposition testProposition = propositionList.get(propositionList.size() - 1);
        assertThat(testProposition.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingProposition() throws Exception {
        int databaseSizeBeforeUpdate = propositionRepository.findAll().size();
        proposition.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPropositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, proposition.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(proposition))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proposition in the database
        List<Proposition> propositionList = propositionRepository.findAll();
        assertThat(propositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProposition() throws Exception {
        int databaseSizeBeforeUpdate = propositionRepository.findAll().size();
        proposition.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPropositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(proposition))
            )
            .andExpect(status().isBadRequest());

        // Validate the Proposition in the database
        List<Proposition> propositionList = propositionRepository.findAll();
        assertThat(propositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProposition() throws Exception {
        int databaseSizeBeforeUpdate = propositionRepository.findAll().size();
        proposition.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPropositionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(proposition))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Proposition in the database
        List<Proposition> propositionList = propositionRepository.findAll();
        assertThat(propositionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProposition() throws Exception {
        // Initialize the database
        propositionRepository.saveAndFlush(proposition);

        int databaseSizeBeforeDelete = propositionRepository.findAll().size();

        // Delete the proposition
        restPropositionMockMvc
            .perform(delete(ENTITY_API_URL_ID, proposition.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Proposition> propositionList = propositionRepository.findAll();
        assertThat(propositionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
