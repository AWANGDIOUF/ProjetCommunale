package agir.gouv.sn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import agir.gouv.sn.IntegrationTest;
import agir.gouv.sn.domain.Vidange;
import agir.gouv.sn.repository.VidangeRepository;
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
 * Integration tests for the {@link VidangeResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VidangeResourceIT {

    private static final String DEFAULT_NOM_VIDEUR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_VIDEUR = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM_VIDEUR = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM_VIDEUR = "BBBBBBBBBB";

    private static final String DEFAULT_TEL_1 = "AAAAAAAAAA";
    private static final String UPDATED_TEL_1 = "BBBBBBBBBB";

    private static final String DEFAULT_TEL_2 = "AAAAAAAAAA";
    private static final String UPDATED_TEL_2 = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/vidanges";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VidangeRepository vidangeRepository;

    @Mock
    private VidangeRepository vidangeRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVidangeMockMvc;

    private Vidange vidange;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vidange createEntity(EntityManager em) {
        Vidange vidange = new Vidange()
            .nomVideur(DEFAULT_NOM_VIDEUR)
            .prenomVideur(DEFAULT_PRENOM_VIDEUR)
            .tel1(DEFAULT_TEL_1)
            .tel2(DEFAULT_TEL_2);
        return vidange;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vidange createUpdatedEntity(EntityManager em) {
        Vidange vidange = new Vidange()
            .nomVideur(UPDATED_NOM_VIDEUR)
            .prenomVideur(UPDATED_PRENOM_VIDEUR)
            .tel1(UPDATED_TEL_1)
            .tel2(UPDATED_TEL_2);
        return vidange;
    }

    @BeforeEach
    public void initTest() {
        vidange = createEntity(em);
    }

    @Test
    @Transactional
    void createVidange() throws Exception {
        int databaseSizeBeforeCreate = vidangeRepository.findAll().size();
        // Create the Vidange
        restVidangeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vidange)))
            .andExpect(status().isCreated());

        // Validate the Vidange in the database
        List<Vidange> vidangeList = vidangeRepository.findAll();
        assertThat(vidangeList).hasSize(databaseSizeBeforeCreate + 1);
        Vidange testVidange = vidangeList.get(vidangeList.size() - 1);
        assertThat(testVidange.getNomVideur()).isEqualTo(DEFAULT_NOM_VIDEUR);
        assertThat(testVidange.getPrenomVideur()).isEqualTo(DEFAULT_PRENOM_VIDEUR);
        assertThat(testVidange.getTel1()).isEqualTo(DEFAULT_TEL_1);
        assertThat(testVidange.getTel2()).isEqualTo(DEFAULT_TEL_2);
    }

    @Test
    @Transactional
    void createVidangeWithExistingId() throws Exception {
        // Create the Vidange with an existing ID
        vidange.setId(1L);

        int databaseSizeBeforeCreate = vidangeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVidangeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vidange)))
            .andExpect(status().isBadRequest());

        // Validate the Vidange in the database
        List<Vidange> vidangeList = vidangeRepository.findAll();
        assertThat(vidangeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVidanges() throws Exception {
        // Initialize the database
        vidangeRepository.saveAndFlush(vidange);

        // Get all the vidangeList
        restVidangeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vidange.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomVideur").value(hasItem(DEFAULT_NOM_VIDEUR)))
            .andExpect(jsonPath("$.[*].prenomVideur").value(hasItem(DEFAULT_PRENOM_VIDEUR)))
            .andExpect(jsonPath("$.[*].tel1").value(hasItem(DEFAULT_TEL_1)))
            .andExpect(jsonPath("$.[*].tel2").value(hasItem(DEFAULT_TEL_2)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVidangesWithEagerRelationshipsIsEnabled() throws Exception {
        when(vidangeRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVidangeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(vidangeRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVidangesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(vidangeRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVidangeMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(vidangeRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getVidange() throws Exception {
        // Initialize the database
        vidangeRepository.saveAndFlush(vidange);

        // Get the vidange
        restVidangeMockMvc
            .perform(get(ENTITY_API_URL_ID, vidange.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vidange.getId().intValue()))
            .andExpect(jsonPath("$.nomVideur").value(DEFAULT_NOM_VIDEUR))
            .andExpect(jsonPath("$.prenomVideur").value(DEFAULT_PRENOM_VIDEUR))
            .andExpect(jsonPath("$.tel1").value(DEFAULT_TEL_1))
            .andExpect(jsonPath("$.tel2").value(DEFAULT_TEL_2));
    }

    @Test
    @Transactional
    void getNonExistingVidange() throws Exception {
        // Get the vidange
        restVidangeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVidange() throws Exception {
        // Initialize the database
        vidangeRepository.saveAndFlush(vidange);

        int databaseSizeBeforeUpdate = vidangeRepository.findAll().size();

        // Update the vidange
        Vidange updatedVidange = vidangeRepository.findById(vidange.getId()).get();
        // Disconnect from session so that the updates on updatedVidange are not directly saved in db
        em.detach(updatedVidange);
        updatedVidange.nomVideur(UPDATED_NOM_VIDEUR).prenomVideur(UPDATED_PRENOM_VIDEUR).tel1(UPDATED_TEL_1).tel2(UPDATED_TEL_2);

        restVidangeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVidange.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVidange))
            )
            .andExpect(status().isOk());

        // Validate the Vidange in the database
        List<Vidange> vidangeList = vidangeRepository.findAll();
        assertThat(vidangeList).hasSize(databaseSizeBeforeUpdate);
        Vidange testVidange = vidangeList.get(vidangeList.size() - 1);
        assertThat(testVidange.getNomVideur()).isEqualTo(UPDATED_NOM_VIDEUR);
        assertThat(testVidange.getPrenomVideur()).isEqualTo(UPDATED_PRENOM_VIDEUR);
        assertThat(testVidange.getTel1()).isEqualTo(UPDATED_TEL_1);
        assertThat(testVidange.getTel2()).isEqualTo(UPDATED_TEL_2);
    }

    @Test
    @Transactional
    void putNonExistingVidange() throws Exception {
        int databaseSizeBeforeUpdate = vidangeRepository.findAll().size();
        vidange.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVidangeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vidange.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vidange))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vidange in the database
        List<Vidange> vidangeList = vidangeRepository.findAll();
        assertThat(vidangeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVidange() throws Exception {
        int databaseSizeBeforeUpdate = vidangeRepository.findAll().size();
        vidange.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVidangeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vidange))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vidange in the database
        List<Vidange> vidangeList = vidangeRepository.findAll();
        assertThat(vidangeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVidange() throws Exception {
        int databaseSizeBeforeUpdate = vidangeRepository.findAll().size();
        vidange.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVidangeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vidange)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vidange in the database
        List<Vidange> vidangeList = vidangeRepository.findAll();
        assertThat(vidangeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVidangeWithPatch() throws Exception {
        // Initialize the database
        vidangeRepository.saveAndFlush(vidange);

        int databaseSizeBeforeUpdate = vidangeRepository.findAll().size();

        // Update the vidange using partial update
        Vidange partialUpdatedVidange = new Vidange();
        partialUpdatedVidange.setId(vidange.getId());

        partialUpdatedVidange.tel2(UPDATED_TEL_2);

        restVidangeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVidange.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVidange))
            )
            .andExpect(status().isOk());

        // Validate the Vidange in the database
        List<Vidange> vidangeList = vidangeRepository.findAll();
        assertThat(vidangeList).hasSize(databaseSizeBeforeUpdate);
        Vidange testVidange = vidangeList.get(vidangeList.size() - 1);
        assertThat(testVidange.getNomVideur()).isEqualTo(DEFAULT_NOM_VIDEUR);
        assertThat(testVidange.getPrenomVideur()).isEqualTo(DEFAULT_PRENOM_VIDEUR);
        assertThat(testVidange.getTel1()).isEqualTo(DEFAULT_TEL_1);
        assertThat(testVidange.getTel2()).isEqualTo(UPDATED_TEL_2);
    }

    @Test
    @Transactional
    void fullUpdateVidangeWithPatch() throws Exception {
        // Initialize the database
        vidangeRepository.saveAndFlush(vidange);

        int databaseSizeBeforeUpdate = vidangeRepository.findAll().size();

        // Update the vidange using partial update
        Vidange partialUpdatedVidange = new Vidange();
        partialUpdatedVidange.setId(vidange.getId());

        partialUpdatedVidange.nomVideur(UPDATED_NOM_VIDEUR).prenomVideur(UPDATED_PRENOM_VIDEUR).tel1(UPDATED_TEL_1).tel2(UPDATED_TEL_2);

        restVidangeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVidange.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVidange))
            )
            .andExpect(status().isOk());

        // Validate the Vidange in the database
        List<Vidange> vidangeList = vidangeRepository.findAll();
        assertThat(vidangeList).hasSize(databaseSizeBeforeUpdate);
        Vidange testVidange = vidangeList.get(vidangeList.size() - 1);
        assertThat(testVidange.getNomVideur()).isEqualTo(UPDATED_NOM_VIDEUR);
        assertThat(testVidange.getPrenomVideur()).isEqualTo(UPDATED_PRENOM_VIDEUR);
        assertThat(testVidange.getTel1()).isEqualTo(UPDATED_TEL_1);
        assertThat(testVidange.getTel2()).isEqualTo(UPDATED_TEL_2);
    }

    @Test
    @Transactional
    void patchNonExistingVidange() throws Exception {
        int databaseSizeBeforeUpdate = vidangeRepository.findAll().size();
        vidange.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVidangeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vidange.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vidange))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vidange in the database
        List<Vidange> vidangeList = vidangeRepository.findAll();
        assertThat(vidangeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVidange() throws Exception {
        int databaseSizeBeforeUpdate = vidangeRepository.findAll().size();
        vidange.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVidangeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vidange))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vidange in the database
        List<Vidange> vidangeList = vidangeRepository.findAll();
        assertThat(vidangeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVidange() throws Exception {
        int databaseSizeBeforeUpdate = vidangeRepository.findAll().size();
        vidange.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVidangeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vidange)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vidange in the database
        List<Vidange> vidangeList = vidangeRepository.findAll();
        assertThat(vidangeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVidange() throws Exception {
        // Initialize the database
        vidangeRepository.saveAndFlush(vidange);

        int databaseSizeBeforeDelete = vidangeRepository.findAll().size();

        // Delete the vidange
        restVidangeMockMvc
            .perform(delete(ENTITY_API_URL_ID, vidange.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vidange> vidangeList = vidangeRepository.findAll();
        assertThat(vidangeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
