package agir.gouv.sn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import agir.gouv.sn.IntegrationTest;
import agir.gouv.sn.domain.InterviewsArtiste;
import agir.gouv.sn.repository.InterviewsArtisteRepository;
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
 * Integration tests for the {@link InterviewsArtisteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InterviewsArtisteResourceIT {

    private static final String DEFAULT_TITRE = "AAAAAAAAAA";
    private static final String UPDATED_TITRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_LIEN = "AAAAAAAAAA";
    private static final String UPDATED_LIEN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/interviews-artistes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private InterviewsArtisteRepository interviewsArtisteRepository;

    @Mock
    private InterviewsArtisteRepository interviewsArtisteRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInterviewsArtisteMockMvc;

    private InterviewsArtiste interviewsArtiste;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InterviewsArtiste createEntity(EntityManager em) {
        InterviewsArtiste interviewsArtiste = new InterviewsArtiste()
            .titre(DEFAULT_TITRE)
            .description(DEFAULT_DESCRIPTION)
            .lien(DEFAULT_LIEN);
        return interviewsArtiste;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InterviewsArtiste createUpdatedEntity(EntityManager em) {
        InterviewsArtiste interviewsArtiste = new InterviewsArtiste()
            .titre(UPDATED_TITRE)
            .description(UPDATED_DESCRIPTION)
            .lien(UPDATED_LIEN);
        return interviewsArtiste;
    }

    @BeforeEach
    public void initTest() {
        interviewsArtiste = createEntity(em);
    }

    @Test
    @Transactional
    void createInterviewsArtiste() throws Exception {
        int databaseSizeBeforeCreate = interviewsArtisteRepository.findAll().size();
        // Create the InterviewsArtiste
        restInterviewsArtisteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(interviewsArtiste))
            )
            .andExpect(status().isCreated());

        // Validate the InterviewsArtiste in the database
        List<InterviewsArtiste> interviewsArtisteList = interviewsArtisteRepository.findAll();
        assertThat(interviewsArtisteList).hasSize(databaseSizeBeforeCreate + 1);
        InterviewsArtiste testInterviewsArtiste = interviewsArtisteList.get(interviewsArtisteList.size() - 1);
        assertThat(testInterviewsArtiste.getTitre()).isEqualTo(DEFAULT_TITRE);
        assertThat(testInterviewsArtiste.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testInterviewsArtiste.getLien()).isEqualTo(DEFAULT_LIEN);
    }

    @Test
    @Transactional
    void createInterviewsArtisteWithExistingId() throws Exception {
        // Create the InterviewsArtiste with an existing ID
        interviewsArtiste.setId(1L);

        int databaseSizeBeforeCreate = interviewsArtisteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInterviewsArtisteMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(interviewsArtiste))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterviewsArtiste in the database
        List<InterviewsArtiste> interviewsArtisteList = interviewsArtisteRepository.findAll();
        assertThat(interviewsArtisteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllInterviewsArtistes() throws Exception {
        // Initialize the database
        interviewsArtisteRepository.saveAndFlush(interviewsArtiste);

        // Get all the interviewsArtisteList
        restInterviewsArtisteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(interviewsArtiste.getId().intValue())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].lien").value(hasItem(DEFAULT_LIEN)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInterviewsArtistesWithEagerRelationshipsIsEnabled() throws Exception {
        when(interviewsArtisteRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInterviewsArtisteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(interviewsArtisteRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInterviewsArtistesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(interviewsArtisteRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInterviewsArtisteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(interviewsArtisteRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getInterviewsArtiste() throws Exception {
        // Initialize the database
        interviewsArtisteRepository.saveAndFlush(interviewsArtiste);

        // Get the interviewsArtiste
        restInterviewsArtisteMockMvc
            .perform(get(ENTITY_API_URL_ID, interviewsArtiste.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(interviewsArtiste.getId().intValue()))
            .andExpect(jsonPath("$.titre").value(DEFAULT_TITRE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.lien").value(DEFAULT_LIEN));
    }

    @Test
    @Transactional
    void getNonExistingInterviewsArtiste() throws Exception {
        // Get the interviewsArtiste
        restInterviewsArtisteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewInterviewsArtiste() throws Exception {
        // Initialize the database
        interviewsArtisteRepository.saveAndFlush(interviewsArtiste);

        int databaseSizeBeforeUpdate = interviewsArtisteRepository.findAll().size();

        // Update the interviewsArtiste
        InterviewsArtiste updatedInterviewsArtiste = interviewsArtisteRepository.findById(interviewsArtiste.getId()).get();
        // Disconnect from session so that the updates on updatedInterviewsArtiste are not directly saved in db
        em.detach(updatedInterviewsArtiste);
        updatedInterviewsArtiste.titre(UPDATED_TITRE).description(UPDATED_DESCRIPTION).lien(UPDATED_LIEN);

        restInterviewsArtisteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInterviewsArtiste.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedInterviewsArtiste))
            )
            .andExpect(status().isOk());

        // Validate the InterviewsArtiste in the database
        List<InterviewsArtiste> interviewsArtisteList = interviewsArtisteRepository.findAll();
        assertThat(interviewsArtisteList).hasSize(databaseSizeBeforeUpdate);
        InterviewsArtiste testInterviewsArtiste = interviewsArtisteList.get(interviewsArtisteList.size() - 1);
        assertThat(testInterviewsArtiste.getTitre()).isEqualTo(UPDATED_TITRE);
        assertThat(testInterviewsArtiste.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testInterviewsArtiste.getLien()).isEqualTo(UPDATED_LIEN);
    }

    @Test
    @Transactional
    void putNonExistingInterviewsArtiste() throws Exception {
        int databaseSizeBeforeUpdate = interviewsArtisteRepository.findAll().size();
        interviewsArtiste.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterviewsArtisteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, interviewsArtiste.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interviewsArtiste))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterviewsArtiste in the database
        List<InterviewsArtiste> interviewsArtisteList = interviewsArtisteRepository.findAll();
        assertThat(interviewsArtisteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInterviewsArtiste() throws Exception {
        int databaseSizeBeforeUpdate = interviewsArtisteRepository.findAll().size();
        interviewsArtiste.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterviewsArtisteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(interviewsArtiste))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterviewsArtiste in the database
        List<InterviewsArtiste> interviewsArtisteList = interviewsArtisteRepository.findAll();
        assertThat(interviewsArtisteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInterviewsArtiste() throws Exception {
        int databaseSizeBeforeUpdate = interviewsArtisteRepository.findAll().size();
        interviewsArtiste.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterviewsArtisteMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(interviewsArtiste))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InterviewsArtiste in the database
        List<InterviewsArtiste> interviewsArtisteList = interviewsArtisteRepository.findAll();
        assertThat(interviewsArtisteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInterviewsArtisteWithPatch() throws Exception {
        // Initialize the database
        interviewsArtisteRepository.saveAndFlush(interviewsArtiste);

        int databaseSizeBeforeUpdate = interviewsArtisteRepository.findAll().size();

        // Update the interviewsArtiste using partial update
        InterviewsArtiste partialUpdatedInterviewsArtiste = new InterviewsArtiste();
        partialUpdatedInterviewsArtiste.setId(interviewsArtiste.getId());

        partialUpdatedInterviewsArtiste.titre(UPDATED_TITRE).description(UPDATED_DESCRIPTION).lien(UPDATED_LIEN);

        restInterviewsArtisteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInterviewsArtiste.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInterviewsArtiste))
            )
            .andExpect(status().isOk());

        // Validate the InterviewsArtiste in the database
        List<InterviewsArtiste> interviewsArtisteList = interviewsArtisteRepository.findAll();
        assertThat(interviewsArtisteList).hasSize(databaseSizeBeforeUpdate);
        InterviewsArtiste testInterviewsArtiste = interviewsArtisteList.get(interviewsArtisteList.size() - 1);
        assertThat(testInterviewsArtiste.getTitre()).isEqualTo(UPDATED_TITRE);
        assertThat(testInterviewsArtiste.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testInterviewsArtiste.getLien()).isEqualTo(UPDATED_LIEN);
    }

    @Test
    @Transactional
    void fullUpdateInterviewsArtisteWithPatch() throws Exception {
        // Initialize the database
        interviewsArtisteRepository.saveAndFlush(interviewsArtiste);

        int databaseSizeBeforeUpdate = interviewsArtisteRepository.findAll().size();

        // Update the interviewsArtiste using partial update
        InterviewsArtiste partialUpdatedInterviewsArtiste = new InterviewsArtiste();
        partialUpdatedInterviewsArtiste.setId(interviewsArtiste.getId());

        partialUpdatedInterviewsArtiste.titre(UPDATED_TITRE).description(UPDATED_DESCRIPTION).lien(UPDATED_LIEN);

        restInterviewsArtisteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInterviewsArtiste.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInterviewsArtiste))
            )
            .andExpect(status().isOk());

        // Validate the InterviewsArtiste in the database
        List<InterviewsArtiste> interviewsArtisteList = interviewsArtisteRepository.findAll();
        assertThat(interviewsArtisteList).hasSize(databaseSizeBeforeUpdate);
        InterviewsArtiste testInterviewsArtiste = interviewsArtisteList.get(interviewsArtisteList.size() - 1);
        assertThat(testInterviewsArtiste.getTitre()).isEqualTo(UPDATED_TITRE);
        assertThat(testInterviewsArtiste.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testInterviewsArtiste.getLien()).isEqualTo(UPDATED_LIEN);
    }

    @Test
    @Transactional
    void patchNonExistingInterviewsArtiste() throws Exception {
        int databaseSizeBeforeUpdate = interviewsArtisteRepository.findAll().size();
        interviewsArtiste.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterviewsArtisteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, interviewsArtiste.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(interviewsArtiste))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterviewsArtiste in the database
        List<InterviewsArtiste> interviewsArtisteList = interviewsArtisteRepository.findAll();
        assertThat(interviewsArtisteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInterviewsArtiste() throws Exception {
        int databaseSizeBeforeUpdate = interviewsArtisteRepository.findAll().size();
        interviewsArtiste.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterviewsArtisteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(interviewsArtiste))
            )
            .andExpect(status().isBadRequest());

        // Validate the InterviewsArtiste in the database
        List<InterviewsArtiste> interviewsArtisteList = interviewsArtisteRepository.findAll();
        assertThat(interviewsArtisteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInterviewsArtiste() throws Exception {
        int databaseSizeBeforeUpdate = interviewsArtisteRepository.findAll().size();
        interviewsArtiste.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInterviewsArtisteMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(interviewsArtiste))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InterviewsArtiste in the database
        List<InterviewsArtiste> interviewsArtisteList = interviewsArtisteRepository.findAll();
        assertThat(interviewsArtisteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInterviewsArtiste() throws Exception {
        // Initialize the database
        interviewsArtisteRepository.saveAndFlush(interviewsArtiste);

        int databaseSizeBeforeDelete = interviewsArtisteRepository.findAll().size();

        // Delete the interviewsArtiste
        restInterviewsArtisteMockMvc
            .perform(delete(ENTITY_API_URL_ID, interviewsArtiste.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InterviewsArtiste> interviewsArtisteList = interviewsArtisteRepository.findAll();
        assertThat(interviewsArtisteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
