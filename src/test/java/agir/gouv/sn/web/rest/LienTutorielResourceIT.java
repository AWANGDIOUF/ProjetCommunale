package agir.gouv.sn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import agir.gouv.sn.IntegrationTest;
import agir.gouv.sn.domain.LienTutoriel;
import agir.gouv.sn.repository.LienTutorielRepository;
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
 * Integration tests for the {@link LienTutorielResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LienTutorielResourceIT {

    private static final String DEFAULT_DESCRIPTION_LIEN = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_LIEN = "BBBBBBBBBB";

    private static final String DEFAULT_LIEN = "AAAAAAAAAA";
    private static final String UPDATED_LIEN = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/lien-tutoriels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LienTutorielRepository lienTutorielRepository;

    @Mock
    private LienTutorielRepository lienTutorielRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLienTutorielMockMvc;

    private LienTutoriel lienTutoriel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LienTutoriel createEntity(EntityManager em) {
        LienTutoriel lienTutoriel = new LienTutoriel().descriptionLien(DEFAULT_DESCRIPTION_LIEN).lien(DEFAULT_LIEN);
        return lienTutoriel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LienTutoriel createUpdatedEntity(EntityManager em) {
        LienTutoriel lienTutoriel = new LienTutoriel().descriptionLien(UPDATED_DESCRIPTION_LIEN).lien(UPDATED_LIEN);
        return lienTutoriel;
    }

    @BeforeEach
    public void initTest() {
        lienTutoriel = createEntity(em);
    }

    @Test
    @Transactional
    void createLienTutoriel() throws Exception {
        int databaseSizeBeforeCreate = lienTutorielRepository.findAll().size();
        // Create the LienTutoriel
        restLienTutorielMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lienTutoriel)))
            .andExpect(status().isCreated());

        // Validate the LienTutoriel in the database
        List<LienTutoriel> lienTutorielList = lienTutorielRepository.findAll();
        assertThat(lienTutorielList).hasSize(databaseSizeBeforeCreate + 1);
        LienTutoriel testLienTutoriel = lienTutorielList.get(lienTutorielList.size() - 1);
        assertThat(testLienTutoriel.getDescriptionLien()).isEqualTo(DEFAULT_DESCRIPTION_LIEN);
        assertThat(testLienTutoriel.getLien()).isEqualTo(DEFAULT_LIEN);
    }

    @Test
    @Transactional
    void createLienTutorielWithExistingId() throws Exception {
        // Create the LienTutoriel with an existing ID
        lienTutoriel.setId(1L);

        int databaseSizeBeforeCreate = lienTutorielRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLienTutorielMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lienTutoriel)))
            .andExpect(status().isBadRequest());

        // Validate the LienTutoriel in the database
        List<LienTutoriel> lienTutorielList = lienTutorielRepository.findAll();
        assertThat(lienTutorielList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLienTutoriels() throws Exception {
        // Initialize the database
        lienTutorielRepository.saveAndFlush(lienTutoriel);

        // Get all the lienTutorielList
        restLienTutorielMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lienTutoriel.getId().intValue())))
            .andExpect(jsonPath("$.[*].descriptionLien").value(hasItem(DEFAULT_DESCRIPTION_LIEN)))
            .andExpect(jsonPath("$.[*].lien").value(hasItem(DEFAULT_LIEN)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLienTutorielsWithEagerRelationshipsIsEnabled() throws Exception {
        when(lienTutorielRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLienTutorielMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(lienTutorielRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLienTutorielsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(lienTutorielRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLienTutorielMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(lienTutorielRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getLienTutoriel() throws Exception {
        // Initialize the database
        lienTutorielRepository.saveAndFlush(lienTutoriel);

        // Get the lienTutoriel
        restLienTutorielMockMvc
            .perform(get(ENTITY_API_URL_ID, lienTutoriel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lienTutoriel.getId().intValue()))
            .andExpect(jsonPath("$.descriptionLien").value(DEFAULT_DESCRIPTION_LIEN))
            .andExpect(jsonPath("$.lien").value(DEFAULT_LIEN));
    }

    @Test
    @Transactional
    void getNonExistingLienTutoriel() throws Exception {
        // Get the lienTutoriel
        restLienTutorielMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLienTutoriel() throws Exception {
        // Initialize the database
        lienTutorielRepository.saveAndFlush(lienTutoriel);

        int databaseSizeBeforeUpdate = lienTutorielRepository.findAll().size();

        // Update the lienTutoriel
        LienTutoriel updatedLienTutoriel = lienTutorielRepository.findById(lienTutoriel.getId()).get();
        // Disconnect from session so that the updates on updatedLienTutoriel are not directly saved in db
        em.detach(updatedLienTutoriel);
        updatedLienTutoriel.descriptionLien(UPDATED_DESCRIPTION_LIEN).lien(UPDATED_LIEN);

        restLienTutorielMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLienTutoriel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLienTutoriel))
            )
            .andExpect(status().isOk());

        // Validate the LienTutoriel in the database
        List<LienTutoriel> lienTutorielList = lienTutorielRepository.findAll();
        assertThat(lienTutorielList).hasSize(databaseSizeBeforeUpdate);
        LienTutoriel testLienTutoriel = lienTutorielList.get(lienTutorielList.size() - 1);
        assertThat(testLienTutoriel.getDescriptionLien()).isEqualTo(UPDATED_DESCRIPTION_LIEN);
        assertThat(testLienTutoriel.getLien()).isEqualTo(UPDATED_LIEN);
    }

    @Test
    @Transactional
    void putNonExistingLienTutoriel() throws Exception {
        int databaseSizeBeforeUpdate = lienTutorielRepository.findAll().size();
        lienTutoriel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLienTutorielMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lienTutoriel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lienTutoriel))
            )
            .andExpect(status().isBadRequest());

        // Validate the LienTutoriel in the database
        List<LienTutoriel> lienTutorielList = lienTutorielRepository.findAll();
        assertThat(lienTutorielList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLienTutoriel() throws Exception {
        int databaseSizeBeforeUpdate = lienTutorielRepository.findAll().size();
        lienTutoriel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLienTutorielMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lienTutoriel))
            )
            .andExpect(status().isBadRequest());

        // Validate the LienTutoriel in the database
        List<LienTutoriel> lienTutorielList = lienTutorielRepository.findAll();
        assertThat(lienTutorielList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLienTutoriel() throws Exception {
        int databaseSizeBeforeUpdate = lienTutorielRepository.findAll().size();
        lienTutoriel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLienTutorielMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lienTutoriel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LienTutoriel in the database
        List<LienTutoriel> lienTutorielList = lienTutorielRepository.findAll();
        assertThat(lienTutorielList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLienTutorielWithPatch() throws Exception {
        // Initialize the database
        lienTutorielRepository.saveAndFlush(lienTutoriel);

        int databaseSizeBeforeUpdate = lienTutorielRepository.findAll().size();

        // Update the lienTutoriel using partial update
        LienTutoriel partialUpdatedLienTutoriel = new LienTutoriel();
        partialUpdatedLienTutoriel.setId(lienTutoriel.getId());

        restLienTutorielMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLienTutoriel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLienTutoriel))
            )
            .andExpect(status().isOk());

        // Validate the LienTutoriel in the database
        List<LienTutoriel> lienTutorielList = lienTutorielRepository.findAll();
        assertThat(lienTutorielList).hasSize(databaseSizeBeforeUpdate);
        LienTutoriel testLienTutoriel = lienTutorielList.get(lienTutorielList.size() - 1);
        assertThat(testLienTutoriel.getDescriptionLien()).isEqualTo(DEFAULT_DESCRIPTION_LIEN);
        assertThat(testLienTutoriel.getLien()).isEqualTo(DEFAULT_LIEN);
    }

    @Test
    @Transactional
    void fullUpdateLienTutorielWithPatch() throws Exception {
        // Initialize the database
        lienTutorielRepository.saveAndFlush(lienTutoriel);

        int databaseSizeBeforeUpdate = lienTutorielRepository.findAll().size();

        // Update the lienTutoriel using partial update
        LienTutoriel partialUpdatedLienTutoriel = new LienTutoriel();
        partialUpdatedLienTutoriel.setId(lienTutoriel.getId());

        partialUpdatedLienTutoriel.descriptionLien(UPDATED_DESCRIPTION_LIEN).lien(UPDATED_LIEN);

        restLienTutorielMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLienTutoriel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLienTutoriel))
            )
            .andExpect(status().isOk());

        // Validate the LienTutoriel in the database
        List<LienTutoriel> lienTutorielList = lienTutorielRepository.findAll();
        assertThat(lienTutorielList).hasSize(databaseSizeBeforeUpdate);
        LienTutoriel testLienTutoriel = lienTutorielList.get(lienTutorielList.size() - 1);
        assertThat(testLienTutoriel.getDescriptionLien()).isEqualTo(UPDATED_DESCRIPTION_LIEN);
        assertThat(testLienTutoriel.getLien()).isEqualTo(UPDATED_LIEN);
    }

    @Test
    @Transactional
    void patchNonExistingLienTutoriel() throws Exception {
        int databaseSizeBeforeUpdate = lienTutorielRepository.findAll().size();
        lienTutoriel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLienTutorielMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lienTutoriel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lienTutoriel))
            )
            .andExpect(status().isBadRequest());

        // Validate the LienTutoriel in the database
        List<LienTutoriel> lienTutorielList = lienTutorielRepository.findAll();
        assertThat(lienTutorielList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLienTutoriel() throws Exception {
        int databaseSizeBeforeUpdate = lienTutorielRepository.findAll().size();
        lienTutoriel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLienTutorielMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lienTutoriel))
            )
            .andExpect(status().isBadRequest());

        // Validate the LienTutoriel in the database
        List<LienTutoriel> lienTutorielList = lienTutorielRepository.findAll();
        assertThat(lienTutorielList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLienTutoriel() throws Exception {
        int databaseSizeBeforeUpdate = lienTutorielRepository.findAll().size();
        lienTutoriel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLienTutorielMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(lienTutoriel))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LienTutoriel in the database
        List<LienTutoriel> lienTutorielList = lienTutorielRepository.findAll();
        assertThat(lienTutorielList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLienTutoriel() throws Exception {
        // Initialize the database
        lienTutorielRepository.saveAndFlush(lienTutoriel);

        int databaseSizeBeforeDelete = lienTutorielRepository.findAll().size();

        // Delete the lienTutoriel
        restLienTutorielMockMvc
            .perform(delete(ENTITY_API_URL_ID, lienTutoriel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LienTutoriel> lienTutorielList = lienTutorielRepository.findAll();
        assertThat(lienTutorielList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
