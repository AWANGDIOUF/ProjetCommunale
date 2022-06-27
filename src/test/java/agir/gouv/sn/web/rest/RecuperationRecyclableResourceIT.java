package agir.gouv.sn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import agir.gouv.sn.IntegrationTest;
import agir.gouv.sn.domain.RecuperationRecyclable;
import agir.gouv.sn.repository.RecuperationRecyclableRepository;
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
 * Integration tests for the {@link RecuperationRecyclableResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RecuperationRecyclableResourceIT {

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_LIEU = "AAAAAAAAAA";
    private static final String UPDATED_LIEU = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/recuperation-recyclables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RecuperationRecyclableRepository recuperationRecyclableRepository;

    @Mock
    private RecuperationRecyclableRepository recuperationRecyclableRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRecuperationRecyclableMockMvc;

    private RecuperationRecyclable recuperationRecyclable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecuperationRecyclable createEntity(EntityManager em) {
        RecuperationRecyclable recuperationRecyclable = new RecuperationRecyclable().nom(DEFAULT_NOM).date(DEFAULT_DATE).lieu(DEFAULT_LIEU);
        return recuperationRecyclable;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RecuperationRecyclable createUpdatedEntity(EntityManager em) {
        RecuperationRecyclable recuperationRecyclable = new RecuperationRecyclable().nom(UPDATED_NOM).date(UPDATED_DATE).lieu(UPDATED_LIEU);
        return recuperationRecyclable;
    }

    @BeforeEach
    public void initTest() {
        recuperationRecyclable = createEntity(em);
    }

    @Test
    @Transactional
    void createRecuperationRecyclable() throws Exception {
        int databaseSizeBeforeCreate = recuperationRecyclableRepository.findAll().size();
        // Create the RecuperationRecyclable
        restRecuperationRecyclableMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recuperationRecyclable))
            )
            .andExpect(status().isCreated());

        // Validate the RecuperationRecyclable in the database
        List<RecuperationRecyclable> recuperationRecyclableList = recuperationRecyclableRepository.findAll();
        assertThat(recuperationRecyclableList).hasSize(databaseSizeBeforeCreate + 1);
        RecuperationRecyclable testRecuperationRecyclable = recuperationRecyclableList.get(recuperationRecyclableList.size() - 1);
        assertThat(testRecuperationRecyclable.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testRecuperationRecyclable.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testRecuperationRecyclable.getLieu()).isEqualTo(DEFAULT_LIEU);
    }

    @Test
    @Transactional
    void createRecuperationRecyclableWithExistingId() throws Exception {
        // Create the RecuperationRecyclable with an existing ID
        recuperationRecyclable.setId(1L);

        int databaseSizeBeforeCreate = recuperationRecyclableRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRecuperationRecyclableMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recuperationRecyclable))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecuperationRecyclable in the database
        List<RecuperationRecyclable> recuperationRecyclableList = recuperationRecyclableRepository.findAll();
        assertThat(recuperationRecyclableList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRecuperationRecyclables() throws Exception {
        // Initialize the database
        recuperationRecyclableRepository.saveAndFlush(recuperationRecyclable);

        // Get all the recuperationRecyclableList
        restRecuperationRecyclableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(recuperationRecyclable.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].lieu").value(hasItem(DEFAULT_LIEU)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRecuperationRecyclablesWithEagerRelationshipsIsEnabled() throws Exception {
        when(recuperationRecyclableRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRecuperationRecyclableMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(recuperationRecyclableRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRecuperationRecyclablesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(recuperationRecyclableRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRecuperationRecyclableMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(recuperationRecyclableRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getRecuperationRecyclable() throws Exception {
        // Initialize the database
        recuperationRecyclableRepository.saveAndFlush(recuperationRecyclable);

        // Get the recuperationRecyclable
        restRecuperationRecyclableMockMvc
            .perform(get(ENTITY_API_URL_ID, recuperationRecyclable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(recuperationRecyclable.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.lieu").value(DEFAULT_LIEU));
    }

    @Test
    @Transactional
    void getNonExistingRecuperationRecyclable() throws Exception {
        // Get the recuperationRecyclable
        restRecuperationRecyclableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRecuperationRecyclable() throws Exception {
        // Initialize the database
        recuperationRecyclableRepository.saveAndFlush(recuperationRecyclable);

        int databaseSizeBeforeUpdate = recuperationRecyclableRepository.findAll().size();

        // Update the recuperationRecyclable
        RecuperationRecyclable updatedRecuperationRecyclable = recuperationRecyclableRepository
            .findById(recuperationRecyclable.getId())
            .get();
        // Disconnect from session so that the updates on updatedRecuperationRecyclable are not directly saved in db
        em.detach(updatedRecuperationRecyclable);
        updatedRecuperationRecyclable.nom(UPDATED_NOM).date(UPDATED_DATE).lieu(UPDATED_LIEU);

        restRecuperationRecyclableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRecuperationRecyclable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRecuperationRecyclable))
            )
            .andExpect(status().isOk());

        // Validate the RecuperationRecyclable in the database
        List<RecuperationRecyclable> recuperationRecyclableList = recuperationRecyclableRepository.findAll();
        assertThat(recuperationRecyclableList).hasSize(databaseSizeBeforeUpdate);
        RecuperationRecyclable testRecuperationRecyclable = recuperationRecyclableList.get(recuperationRecyclableList.size() - 1);
        assertThat(testRecuperationRecyclable.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testRecuperationRecyclable.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testRecuperationRecyclable.getLieu()).isEqualTo(UPDATED_LIEU);
    }

    @Test
    @Transactional
    void putNonExistingRecuperationRecyclable() throws Exception {
        int databaseSizeBeforeUpdate = recuperationRecyclableRepository.findAll().size();
        recuperationRecyclable.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecuperationRecyclableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, recuperationRecyclable.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recuperationRecyclable))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecuperationRecyclable in the database
        List<RecuperationRecyclable> recuperationRecyclableList = recuperationRecyclableRepository.findAll();
        assertThat(recuperationRecyclableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRecuperationRecyclable() throws Exception {
        int databaseSizeBeforeUpdate = recuperationRecyclableRepository.findAll().size();
        recuperationRecyclable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecuperationRecyclableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recuperationRecyclable))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecuperationRecyclable in the database
        List<RecuperationRecyclable> recuperationRecyclableList = recuperationRecyclableRepository.findAll();
        assertThat(recuperationRecyclableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRecuperationRecyclable() throws Exception {
        int databaseSizeBeforeUpdate = recuperationRecyclableRepository.findAll().size();
        recuperationRecyclable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecuperationRecyclableMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(recuperationRecyclable))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecuperationRecyclable in the database
        List<RecuperationRecyclable> recuperationRecyclableList = recuperationRecyclableRepository.findAll();
        assertThat(recuperationRecyclableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRecuperationRecyclableWithPatch() throws Exception {
        // Initialize the database
        recuperationRecyclableRepository.saveAndFlush(recuperationRecyclable);

        int databaseSizeBeforeUpdate = recuperationRecyclableRepository.findAll().size();

        // Update the recuperationRecyclable using partial update
        RecuperationRecyclable partialUpdatedRecuperationRecyclable = new RecuperationRecyclable();
        partialUpdatedRecuperationRecyclable.setId(recuperationRecyclable.getId());

        partialUpdatedRecuperationRecyclable.lieu(UPDATED_LIEU);

        restRecuperationRecyclableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecuperationRecyclable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecuperationRecyclable))
            )
            .andExpect(status().isOk());

        // Validate the RecuperationRecyclable in the database
        List<RecuperationRecyclable> recuperationRecyclableList = recuperationRecyclableRepository.findAll();
        assertThat(recuperationRecyclableList).hasSize(databaseSizeBeforeUpdate);
        RecuperationRecyclable testRecuperationRecyclable = recuperationRecyclableList.get(recuperationRecyclableList.size() - 1);
        assertThat(testRecuperationRecyclable.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testRecuperationRecyclable.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testRecuperationRecyclable.getLieu()).isEqualTo(UPDATED_LIEU);
    }

    @Test
    @Transactional
    void fullUpdateRecuperationRecyclableWithPatch() throws Exception {
        // Initialize the database
        recuperationRecyclableRepository.saveAndFlush(recuperationRecyclable);

        int databaseSizeBeforeUpdate = recuperationRecyclableRepository.findAll().size();

        // Update the recuperationRecyclable using partial update
        RecuperationRecyclable partialUpdatedRecuperationRecyclable = new RecuperationRecyclable();
        partialUpdatedRecuperationRecyclable.setId(recuperationRecyclable.getId());

        partialUpdatedRecuperationRecyclable.nom(UPDATED_NOM).date(UPDATED_DATE).lieu(UPDATED_LIEU);

        restRecuperationRecyclableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRecuperationRecyclable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRecuperationRecyclable))
            )
            .andExpect(status().isOk());

        // Validate the RecuperationRecyclable in the database
        List<RecuperationRecyclable> recuperationRecyclableList = recuperationRecyclableRepository.findAll();
        assertThat(recuperationRecyclableList).hasSize(databaseSizeBeforeUpdate);
        RecuperationRecyclable testRecuperationRecyclable = recuperationRecyclableList.get(recuperationRecyclableList.size() - 1);
        assertThat(testRecuperationRecyclable.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testRecuperationRecyclable.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testRecuperationRecyclable.getLieu()).isEqualTo(UPDATED_LIEU);
    }

    @Test
    @Transactional
    void patchNonExistingRecuperationRecyclable() throws Exception {
        int databaseSizeBeforeUpdate = recuperationRecyclableRepository.findAll().size();
        recuperationRecyclable.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRecuperationRecyclableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, recuperationRecyclable.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recuperationRecyclable))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecuperationRecyclable in the database
        List<RecuperationRecyclable> recuperationRecyclableList = recuperationRecyclableRepository.findAll();
        assertThat(recuperationRecyclableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRecuperationRecyclable() throws Exception {
        int databaseSizeBeforeUpdate = recuperationRecyclableRepository.findAll().size();
        recuperationRecyclable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecuperationRecyclableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recuperationRecyclable))
            )
            .andExpect(status().isBadRequest());

        // Validate the RecuperationRecyclable in the database
        List<RecuperationRecyclable> recuperationRecyclableList = recuperationRecyclableRepository.findAll();
        assertThat(recuperationRecyclableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRecuperationRecyclable() throws Exception {
        int databaseSizeBeforeUpdate = recuperationRecyclableRepository.findAll().size();
        recuperationRecyclable.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRecuperationRecyclableMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(recuperationRecyclable))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RecuperationRecyclable in the database
        List<RecuperationRecyclable> recuperationRecyclableList = recuperationRecyclableRepository.findAll();
        assertThat(recuperationRecyclableList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRecuperationRecyclable() throws Exception {
        // Initialize the database
        recuperationRecyclableRepository.saveAndFlush(recuperationRecyclable);

        int databaseSizeBeforeDelete = recuperationRecyclableRepository.findAll().size();

        // Delete the recuperationRecyclable
        restRecuperationRecyclableMockMvc
            .perform(delete(ENTITY_API_URL_ID, recuperationRecyclable.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RecuperationRecyclable> recuperationRecyclableList = recuperationRecyclableRepository.findAll();
        assertThat(recuperationRecyclableList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
