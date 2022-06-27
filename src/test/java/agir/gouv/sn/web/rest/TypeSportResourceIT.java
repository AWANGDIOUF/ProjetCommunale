package agir.gouv.sn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import agir.gouv.sn.IntegrationTest;
import agir.gouv.sn.domain.TypeSport;
import agir.gouv.sn.domain.enumeration.Sport;
import agir.gouv.sn.repository.TypeSportRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TypeSportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypeSportResourceIT {

    private static final Sport DEFAULT_SPORT = Sport.FOOTBALL;
    private static final Sport UPDATED_SPORT = Sport.BASKET;

    private static final String ENTITY_API_URL = "/api/type-sports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypeSportRepository typeSportRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeSportMockMvc;

    private TypeSport typeSport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeSport createEntity(EntityManager em) {
        TypeSport typeSport = new TypeSport().sport(DEFAULT_SPORT);
        return typeSport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeSport createUpdatedEntity(EntityManager em) {
        TypeSport typeSport = new TypeSport().sport(UPDATED_SPORT);
        return typeSport;
    }

    @BeforeEach
    public void initTest() {
        typeSport = createEntity(em);
    }

    @Test
    @Transactional
    void createTypeSport() throws Exception {
        int databaseSizeBeforeCreate = typeSportRepository.findAll().size();
        // Create the TypeSport
        restTypeSportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeSport)))
            .andExpect(status().isCreated());

        // Validate the TypeSport in the database
        List<TypeSport> typeSportList = typeSportRepository.findAll();
        assertThat(typeSportList).hasSize(databaseSizeBeforeCreate + 1);
        TypeSport testTypeSport = typeSportList.get(typeSportList.size() - 1);
        assertThat(testTypeSport.getSport()).isEqualTo(DEFAULT_SPORT);
    }

    @Test
    @Transactional
    void createTypeSportWithExistingId() throws Exception {
        // Create the TypeSport with an existing ID
        typeSport.setId(1L);

        int databaseSizeBeforeCreate = typeSportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeSportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeSport)))
            .andExpect(status().isBadRequest());

        // Validate the TypeSport in the database
        List<TypeSport> typeSportList = typeSportRepository.findAll();
        assertThat(typeSportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTypeSports() throws Exception {
        // Initialize the database
        typeSportRepository.saveAndFlush(typeSport);

        // Get all the typeSportList
        restTypeSportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeSport.getId().intValue())))
            .andExpect(jsonPath("$.[*].sport").value(hasItem(DEFAULT_SPORT.toString())));
    }

    @Test
    @Transactional
    void getTypeSport() throws Exception {
        // Initialize the database
        typeSportRepository.saveAndFlush(typeSport);

        // Get the typeSport
        restTypeSportMockMvc
            .perform(get(ENTITY_API_URL_ID, typeSport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeSport.getId().intValue()))
            .andExpect(jsonPath("$.sport").value(DEFAULT_SPORT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTypeSport() throws Exception {
        // Get the typeSport
        restTypeSportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTypeSport() throws Exception {
        // Initialize the database
        typeSportRepository.saveAndFlush(typeSport);

        int databaseSizeBeforeUpdate = typeSportRepository.findAll().size();

        // Update the typeSport
        TypeSport updatedTypeSport = typeSportRepository.findById(typeSport.getId()).get();
        // Disconnect from session so that the updates on updatedTypeSport are not directly saved in db
        em.detach(updatedTypeSport);
        updatedTypeSport.sport(UPDATED_SPORT);

        restTypeSportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTypeSport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTypeSport))
            )
            .andExpect(status().isOk());

        // Validate the TypeSport in the database
        List<TypeSport> typeSportList = typeSportRepository.findAll();
        assertThat(typeSportList).hasSize(databaseSizeBeforeUpdate);
        TypeSport testTypeSport = typeSportList.get(typeSportList.size() - 1);
        assertThat(testTypeSport.getSport()).isEqualTo(UPDATED_SPORT);
    }

    @Test
    @Transactional
    void putNonExistingTypeSport() throws Exception {
        int databaseSizeBeforeUpdate = typeSportRepository.findAll().size();
        typeSport.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeSportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeSport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeSport))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeSport in the database
        List<TypeSport> typeSportList = typeSportRepository.findAll();
        assertThat(typeSportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypeSport() throws Exception {
        int databaseSizeBeforeUpdate = typeSportRepository.findAll().size();
        typeSport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeSportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeSport))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeSport in the database
        List<TypeSport> typeSportList = typeSportRepository.findAll();
        assertThat(typeSportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypeSport() throws Exception {
        int databaseSizeBeforeUpdate = typeSportRepository.findAll().size();
        typeSport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeSportMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeSport)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeSport in the database
        List<TypeSport> typeSportList = typeSportRepository.findAll();
        assertThat(typeSportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypeSportWithPatch() throws Exception {
        // Initialize the database
        typeSportRepository.saveAndFlush(typeSport);

        int databaseSizeBeforeUpdate = typeSportRepository.findAll().size();

        // Update the typeSport using partial update
        TypeSport partialUpdatedTypeSport = new TypeSport();
        partialUpdatedTypeSport.setId(typeSport.getId());

        restTypeSportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeSport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeSport))
            )
            .andExpect(status().isOk());

        // Validate the TypeSport in the database
        List<TypeSport> typeSportList = typeSportRepository.findAll();
        assertThat(typeSportList).hasSize(databaseSizeBeforeUpdate);
        TypeSport testTypeSport = typeSportList.get(typeSportList.size() - 1);
        assertThat(testTypeSport.getSport()).isEqualTo(DEFAULT_SPORT);
    }

    @Test
    @Transactional
    void fullUpdateTypeSportWithPatch() throws Exception {
        // Initialize the database
        typeSportRepository.saveAndFlush(typeSport);

        int databaseSizeBeforeUpdate = typeSportRepository.findAll().size();

        // Update the typeSport using partial update
        TypeSport partialUpdatedTypeSport = new TypeSport();
        partialUpdatedTypeSport.setId(typeSport.getId());

        partialUpdatedTypeSport.sport(UPDATED_SPORT);

        restTypeSportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeSport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeSport))
            )
            .andExpect(status().isOk());

        // Validate the TypeSport in the database
        List<TypeSport> typeSportList = typeSportRepository.findAll();
        assertThat(typeSportList).hasSize(databaseSizeBeforeUpdate);
        TypeSport testTypeSport = typeSportList.get(typeSportList.size() - 1);
        assertThat(testTypeSport.getSport()).isEqualTo(UPDATED_SPORT);
    }

    @Test
    @Transactional
    void patchNonExistingTypeSport() throws Exception {
        int databaseSizeBeforeUpdate = typeSportRepository.findAll().size();
        typeSport.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeSportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typeSport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeSport))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeSport in the database
        List<TypeSport> typeSportList = typeSportRepository.findAll();
        assertThat(typeSportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypeSport() throws Exception {
        int databaseSizeBeforeUpdate = typeSportRepository.findAll().size();
        typeSport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeSportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeSport))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeSport in the database
        List<TypeSport> typeSportList = typeSportRepository.findAll();
        assertThat(typeSportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypeSport() throws Exception {
        int databaseSizeBeforeUpdate = typeSportRepository.findAll().size();
        typeSport.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeSportMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(typeSport))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeSport in the database
        List<TypeSport> typeSportList = typeSportRepository.findAll();
        assertThat(typeSportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypeSport() throws Exception {
        // Initialize the database
        typeSportRepository.saveAndFlush(typeSport);

        int databaseSizeBeforeDelete = typeSportRepository.findAll().size();

        // Delete the typeSport
        restTypeSportMockMvc
            .perform(delete(ENTITY_API_URL_ID, typeSport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypeSport> typeSportList = typeSportRepository.findAll();
        assertThat(typeSportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
