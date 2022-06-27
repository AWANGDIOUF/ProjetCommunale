package agir.gouv.sn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import agir.gouv.sn.IntegrationTest;
import agir.gouv.sn.domain.TypeVaccin;
import agir.gouv.sn.repository.TypeVaccinRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link TypeVaccinResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypeVaccinResourceIT {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_OBJECTIF = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_OBJECTIF = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/type-vaccins";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypeVaccinRepository typeVaccinRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeVaccinMockMvc;

    private TypeVaccin typeVaccin;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeVaccin createEntity(EntityManager em) {
        TypeVaccin typeVaccin = new TypeVaccin().libelle(DEFAULT_LIBELLE).objectif(DEFAULT_OBJECTIF);
        return typeVaccin;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeVaccin createUpdatedEntity(EntityManager em) {
        TypeVaccin typeVaccin = new TypeVaccin().libelle(UPDATED_LIBELLE).objectif(UPDATED_OBJECTIF);
        return typeVaccin;
    }

    @BeforeEach
    public void initTest() {
        typeVaccin = createEntity(em);
    }

    @Test
    @Transactional
    void createTypeVaccin() throws Exception {
        int databaseSizeBeforeCreate = typeVaccinRepository.findAll().size();
        // Create the TypeVaccin
        restTypeVaccinMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeVaccin)))
            .andExpect(status().isCreated());

        // Validate the TypeVaccin in the database
        List<TypeVaccin> typeVaccinList = typeVaccinRepository.findAll();
        assertThat(typeVaccinList).hasSize(databaseSizeBeforeCreate + 1);
        TypeVaccin testTypeVaccin = typeVaccinList.get(typeVaccinList.size() - 1);
        assertThat(testTypeVaccin.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testTypeVaccin.getObjectif()).isEqualTo(DEFAULT_OBJECTIF);
    }

    @Test
    @Transactional
    void createTypeVaccinWithExistingId() throws Exception {
        // Create the TypeVaccin with an existing ID
        typeVaccin.setId(1L);

        int databaseSizeBeforeCreate = typeVaccinRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeVaccinMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeVaccin)))
            .andExpect(status().isBadRequest());

        // Validate the TypeVaccin in the database
        List<TypeVaccin> typeVaccinList = typeVaccinRepository.findAll();
        assertThat(typeVaccinList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTypeVaccins() throws Exception {
        // Initialize the database
        typeVaccinRepository.saveAndFlush(typeVaccin);

        // Get all the typeVaccinList
        restTypeVaccinMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeVaccin.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE)))
            .andExpect(jsonPath("$.[*].objectif").value(hasItem(DEFAULT_OBJECTIF.toString())));
    }

    @Test
    @Transactional
    void getTypeVaccin() throws Exception {
        // Initialize the database
        typeVaccinRepository.saveAndFlush(typeVaccin);

        // Get the typeVaccin
        restTypeVaccinMockMvc
            .perform(get(ENTITY_API_URL_ID, typeVaccin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeVaccin.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE))
            .andExpect(jsonPath("$.objectif").value(DEFAULT_OBJECTIF.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTypeVaccin() throws Exception {
        // Get the typeVaccin
        restTypeVaccinMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTypeVaccin() throws Exception {
        // Initialize the database
        typeVaccinRepository.saveAndFlush(typeVaccin);

        int databaseSizeBeforeUpdate = typeVaccinRepository.findAll().size();

        // Update the typeVaccin
        TypeVaccin updatedTypeVaccin = typeVaccinRepository.findById(typeVaccin.getId()).get();
        // Disconnect from session so that the updates on updatedTypeVaccin are not directly saved in db
        em.detach(updatedTypeVaccin);
        updatedTypeVaccin.libelle(UPDATED_LIBELLE).objectif(UPDATED_OBJECTIF);

        restTypeVaccinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTypeVaccin.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTypeVaccin))
            )
            .andExpect(status().isOk());

        // Validate the TypeVaccin in the database
        List<TypeVaccin> typeVaccinList = typeVaccinRepository.findAll();
        assertThat(typeVaccinList).hasSize(databaseSizeBeforeUpdate);
        TypeVaccin testTypeVaccin = typeVaccinList.get(typeVaccinList.size() - 1);
        assertThat(testTypeVaccin.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testTypeVaccin.getObjectif()).isEqualTo(UPDATED_OBJECTIF);
    }

    @Test
    @Transactional
    void putNonExistingTypeVaccin() throws Exception {
        int databaseSizeBeforeUpdate = typeVaccinRepository.findAll().size();
        typeVaccin.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeVaccinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeVaccin.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeVaccin))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeVaccin in the database
        List<TypeVaccin> typeVaccinList = typeVaccinRepository.findAll();
        assertThat(typeVaccinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypeVaccin() throws Exception {
        int databaseSizeBeforeUpdate = typeVaccinRepository.findAll().size();
        typeVaccin.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeVaccinMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeVaccin))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeVaccin in the database
        List<TypeVaccin> typeVaccinList = typeVaccinRepository.findAll();
        assertThat(typeVaccinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypeVaccin() throws Exception {
        int databaseSizeBeforeUpdate = typeVaccinRepository.findAll().size();
        typeVaccin.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeVaccinMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeVaccin)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeVaccin in the database
        List<TypeVaccin> typeVaccinList = typeVaccinRepository.findAll();
        assertThat(typeVaccinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypeVaccinWithPatch() throws Exception {
        // Initialize the database
        typeVaccinRepository.saveAndFlush(typeVaccin);

        int databaseSizeBeforeUpdate = typeVaccinRepository.findAll().size();

        // Update the typeVaccin using partial update
        TypeVaccin partialUpdatedTypeVaccin = new TypeVaccin();
        partialUpdatedTypeVaccin.setId(typeVaccin.getId());

        restTypeVaccinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeVaccin.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeVaccin))
            )
            .andExpect(status().isOk());

        // Validate the TypeVaccin in the database
        List<TypeVaccin> typeVaccinList = typeVaccinRepository.findAll();
        assertThat(typeVaccinList).hasSize(databaseSizeBeforeUpdate);
        TypeVaccin testTypeVaccin = typeVaccinList.get(typeVaccinList.size() - 1);
        assertThat(testTypeVaccin.getLibelle()).isEqualTo(DEFAULT_LIBELLE);
        assertThat(testTypeVaccin.getObjectif()).isEqualTo(DEFAULT_OBJECTIF);
    }

    @Test
    @Transactional
    void fullUpdateTypeVaccinWithPatch() throws Exception {
        // Initialize the database
        typeVaccinRepository.saveAndFlush(typeVaccin);

        int databaseSizeBeforeUpdate = typeVaccinRepository.findAll().size();

        // Update the typeVaccin using partial update
        TypeVaccin partialUpdatedTypeVaccin = new TypeVaccin();
        partialUpdatedTypeVaccin.setId(typeVaccin.getId());

        partialUpdatedTypeVaccin.libelle(UPDATED_LIBELLE).objectif(UPDATED_OBJECTIF);

        restTypeVaccinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeVaccin.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeVaccin))
            )
            .andExpect(status().isOk());

        // Validate the TypeVaccin in the database
        List<TypeVaccin> typeVaccinList = typeVaccinRepository.findAll();
        assertThat(typeVaccinList).hasSize(databaseSizeBeforeUpdate);
        TypeVaccin testTypeVaccin = typeVaccinList.get(typeVaccinList.size() - 1);
        assertThat(testTypeVaccin.getLibelle()).isEqualTo(UPDATED_LIBELLE);
        assertThat(testTypeVaccin.getObjectif()).isEqualTo(UPDATED_OBJECTIF);
    }

    @Test
    @Transactional
    void patchNonExistingTypeVaccin() throws Exception {
        int databaseSizeBeforeUpdate = typeVaccinRepository.findAll().size();
        typeVaccin.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeVaccinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typeVaccin.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeVaccin))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeVaccin in the database
        List<TypeVaccin> typeVaccinList = typeVaccinRepository.findAll();
        assertThat(typeVaccinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypeVaccin() throws Exception {
        int databaseSizeBeforeUpdate = typeVaccinRepository.findAll().size();
        typeVaccin.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeVaccinMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeVaccin))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeVaccin in the database
        List<TypeVaccin> typeVaccinList = typeVaccinRepository.findAll();
        assertThat(typeVaccinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypeVaccin() throws Exception {
        int databaseSizeBeforeUpdate = typeVaccinRepository.findAll().size();
        typeVaccin.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeVaccinMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(typeVaccin))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeVaccin in the database
        List<TypeVaccin> typeVaccinList = typeVaccinRepository.findAll();
        assertThat(typeVaccinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypeVaccin() throws Exception {
        // Initialize the database
        typeVaccinRepository.saveAndFlush(typeVaccin);

        int databaseSizeBeforeDelete = typeVaccinRepository.findAll().size();

        // Delete the typeVaccin
        restTypeVaccinMockMvc
            .perform(delete(ENTITY_API_URL_ID, typeVaccin.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypeVaccin> typeVaccinList = typeVaccinRepository.findAll();
        assertThat(typeVaccinList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
