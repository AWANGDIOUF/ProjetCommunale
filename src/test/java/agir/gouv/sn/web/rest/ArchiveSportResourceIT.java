package agir.gouv.sn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import agir.gouv.sn.IntegrationTest;
import agir.gouv.sn.domain.ArchiveSport;
import agir.gouv.sn.repository.ArchiveSportRepository;
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
 * Integration tests for the {@link ArchiveSportResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ArchiveSportResourceIT {

    private static final LocalDate DEFAULT_ANNEE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ANNEE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/archive-sports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ArchiveSportRepository archiveSportRepository;

    @Mock
    private ArchiveSportRepository archiveSportRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArchiveSportMockMvc;

    private ArchiveSport archiveSport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArchiveSport createEntity(EntityManager em) {
        ArchiveSport archiveSport = new ArchiveSport().annee(DEFAULT_ANNEE);
        return archiveSport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArchiveSport createUpdatedEntity(EntityManager em) {
        ArchiveSport archiveSport = new ArchiveSport().annee(UPDATED_ANNEE);
        return archiveSport;
    }

    @BeforeEach
    public void initTest() {
        archiveSport = createEntity(em);
    }

    @Test
    @Transactional
    void createArchiveSport() throws Exception {
        int databaseSizeBeforeCreate = archiveSportRepository.findAll().size();
        // Create the ArchiveSport
        restArchiveSportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(archiveSport)))
            .andExpect(status().isCreated());

        // Validate the ArchiveSport in the database
        List<ArchiveSport> archiveSportList = archiveSportRepository.findAll();
        assertThat(archiveSportList).hasSize(databaseSizeBeforeCreate + 1);
        ArchiveSport testArchiveSport = archiveSportList.get(archiveSportList.size() - 1);
        assertThat(testArchiveSport.getAnnee()).isEqualTo(DEFAULT_ANNEE);
    }

    @Test
    @Transactional
    void createArchiveSportWithExistingId() throws Exception {
        // Create the ArchiveSport with an existing ID
        archiveSport.setId(1L);

        int databaseSizeBeforeCreate = archiveSportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArchiveSportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(archiveSport)))
            .andExpect(status().isBadRequest());

        // Validate the ArchiveSport in the database
        List<ArchiveSport> archiveSportList = archiveSportRepository.findAll();
        assertThat(archiveSportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllArchiveSports() throws Exception {
        // Initialize the database
        archiveSportRepository.saveAndFlush(archiveSport);

        // Get all the archiveSportList
        restArchiveSportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(archiveSport.getId().intValue())))
            .andExpect(jsonPath("$.[*].annee").value(hasItem(DEFAULT_ANNEE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllArchiveSportsWithEagerRelationshipsIsEnabled() throws Exception {
        when(archiveSportRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restArchiveSportMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(archiveSportRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllArchiveSportsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(archiveSportRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restArchiveSportMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(archiveSportRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getArchiveSport() throws Exception {
        // Initialize the database
        archiveSportRepository.saveAndFlush(archiveSport);

        // Get the archiveSport
        restArchiveSportMockMvc
            .perform(get(ENTITY_API_URL_ID, archiveSport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(archiveSport.getId().intValue()))
            .andExpect(jsonPath("$.annee").value(DEFAULT_ANNEE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingArchiveSport() throws Exception {
        // Get the archiveSport
        restArchiveSportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewArchiveSport() throws Exception {
        // Initialize the database
        archiveSportRepository.saveAndFlush(archiveSport);

        int databaseSizeBeforeUpdate = archiveSportRepository.findAll().size();

        // Update the archiveSport
        ArchiveSport updatedArchiveSport = archiveSportRepository.findById(archiveSport.getId()).get();
        // Disconnect from session so that the updates on updatedArchiveSport are not directly saved in db
        em.detach(updatedArchiveSport);
        updatedArchiveSport.annee(UPDATED_ANNEE);

        restArchiveSportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedArchiveSport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedArchiveSport))
            )
            .andExpect(status().isOk());

        // Validate the ArchiveSport in the database
        List<ArchiveSport> archiveSportList = archiveSportRepository.findAll();
        assertThat(archiveSportList).hasSize(databaseSizeBeforeUpdate);
        ArchiveSport testArchiveSport = archiveSportList.get(archiveSportList.size() - 1);
        assertThat(testArchiveSport.getAnnee()).isEqualTo(UPDATED_ANNEE);
    }

    @Test
    @Transactional
    void putNonExistingArchiveSport() throws Exception {
        int databaseSizeBeforeUpdate = archiveSportRepository.findAll().size();
        archiveSport.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArchiveSportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, archiveSport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(archiveSport))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArchiveSport in the database
        List<ArchiveSport> archiveSportList = archiveSportRepository.findAll();
        assertThat(archiveSportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchArchiveSport() throws Exception {
        int databaseSizeBeforeUpdate = archiveSportRepository.findAll().size();
        archiveSport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArchiveSportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(archiveSport))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArchiveSport in the database
        List<ArchiveSport> archiveSportList = archiveSportRepository.findAll();
        assertThat(archiveSportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArchiveSport() throws Exception {
        int databaseSizeBeforeUpdate = archiveSportRepository.findAll().size();
        archiveSport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArchiveSportMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(archiveSport)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArchiveSport in the database
        List<ArchiveSport> archiveSportList = archiveSportRepository.findAll();
        assertThat(archiveSportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateArchiveSportWithPatch() throws Exception {
        // Initialize the database
        archiveSportRepository.saveAndFlush(archiveSport);

        int databaseSizeBeforeUpdate = archiveSportRepository.findAll().size();

        // Update the archiveSport using partial update
        ArchiveSport partialUpdatedArchiveSport = new ArchiveSport();
        partialUpdatedArchiveSport.setId(archiveSport.getId());

        partialUpdatedArchiveSport.annee(UPDATED_ANNEE);

        restArchiveSportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArchiveSport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArchiveSport))
            )
            .andExpect(status().isOk());

        // Validate the ArchiveSport in the database
        List<ArchiveSport> archiveSportList = archiveSportRepository.findAll();
        assertThat(archiveSportList).hasSize(databaseSizeBeforeUpdate);
        ArchiveSport testArchiveSport = archiveSportList.get(archiveSportList.size() - 1);
        assertThat(testArchiveSport.getAnnee()).isEqualTo(UPDATED_ANNEE);
    }

    @Test
    @Transactional
    void fullUpdateArchiveSportWithPatch() throws Exception {
        // Initialize the database
        archiveSportRepository.saveAndFlush(archiveSport);

        int databaseSizeBeforeUpdate = archiveSportRepository.findAll().size();

        // Update the archiveSport using partial update
        ArchiveSport partialUpdatedArchiveSport = new ArchiveSport();
        partialUpdatedArchiveSport.setId(archiveSport.getId());

        partialUpdatedArchiveSport.annee(UPDATED_ANNEE);

        restArchiveSportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArchiveSport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArchiveSport))
            )
            .andExpect(status().isOk());

        // Validate the ArchiveSport in the database
        List<ArchiveSport> archiveSportList = archiveSportRepository.findAll();
        assertThat(archiveSportList).hasSize(databaseSizeBeforeUpdate);
        ArchiveSport testArchiveSport = archiveSportList.get(archiveSportList.size() - 1);
        assertThat(testArchiveSport.getAnnee()).isEqualTo(UPDATED_ANNEE);
    }

    @Test
    @Transactional
    void patchNonExistingArchiveSport() throws Exception {
        int databaseSizeBeforeUpdate = archiveSportRepository.findAll().size();
        archiveSport.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArchiveSportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, archiveSport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(archiveSport))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArchiveSport in the database
        List<ArchiveSport> archiveSportList = archiveSportRepository.findAll();
        assertThat(archiveSportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArchiveSport() throws Exception {
        int databaseSizeBeforeUpdate = archiveSportRepository.findAll().size();
        archiveSport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArchiveSportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(archiveSport))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArchiveSport in the database
        List<ArchiveSport> archiveSportList = archiveSportRepository.findAll();
        assertThat(archiveSportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArchiveSport() throws Exception {
        int databaseSizeBeforeUpdate = archiveSportRepository.findAll().size();
        archiveSport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArchiveSportMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(archiveSport))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArchiveSport in the database
        List<ArchiveSport> archiveSportList = archiveSportRepository.findAll();
        assertThat(archiveSportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteArchiveSport() throws Exception {
        // Initialize the database
        archiveSportRepository.saveAndFlush(archiveSport);

        int databaseSizeBeforeDelete = archiveSportRepository.findAll().size();

        // Delete the archiveSport
        restArchiveSportMockMvc
            .perform(delete(ENTITY_API_URL_ID, archiveSport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ArchiveSport> archiveSportList = archiveSportRepository.findAll();
        assertThat(archiveSportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
