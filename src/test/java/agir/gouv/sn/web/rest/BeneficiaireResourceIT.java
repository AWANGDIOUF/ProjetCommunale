package agir.gouv.sn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import agir.gouv.sn.IntegrationTest;
import agir.gouv.sn.domain.Beneficiaire;
import agir.gouv.sn.domain.enumeration.TypeBeneficiaire;
import agir.gouv.sn.repository.BeneficiaireRepository;
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
 * Integration tests for the {@link BeneficiaireResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BeneficiaireResourceIT {

    private static final TypeBeneficiaire DEFAULT_TYPE_BENEFICIAIRE = TypeBeneficiaire.Groupement;
    private static final TypeBeneficiaire UPDATED_TYPE_BENEFICIAIRE = TypeBeneficiaire.Association;

    private static final String DEFAULT_AUTRE_BENEFICIAIRE = "AAAAAAAAAA";
    private static final String UPDATED_AUTRE_BENEFICIAIRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_JUSTIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_JUSTIFICATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/beneficiaires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BeneficiaireRepository beneficiaireRepository;

    @Mock
    private BeneficiaireRepository beneficiaireRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBeneficiaireMockMvc;

    private Beneficiaire beneficiaire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Beneficiaire createEntity(EntityManager em) {
        Beneficiaire beneficiaire = new Beneficiaire()
            .typeBeneficiaire(DEFAULT_TYPE_BENEFICIAIRE)
            .autreBeneficiaire(DEFAULT_AUTRE_BENEFICIAIRE)
            .description(DEFAULT_DESCRIPTION)
            .justification(DEFAULT_JUSTIFICATION);
        return beneficiaire;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Beneficiaire createUpdatedEntity(EntityManager em) {
        Beneficiaire beneficiaire = new Beneficiaire()
            .typeBeneficiaire(UPDATED_TYPE_BENEFICIAIRE)
            .autreBeneficiaire(UPDATED_AUTRE_BENEFICIAIRE)
            .description(UPDATED_DESCRIPTION)
            .justification(UPDATED_JUSTIFICATION);
        return beneficiaire;
    }

    @BeforeEach
    public void initTest() {
        beneficiaire = createEntity(em);
    }

    @Test
    @Transactional
    void createBeneficiaire() throws Exception {
        int databaseSizeBeforeCreate = beneficiaireRepository.findAll().size();
        // Create the Beneficiaire
        restBeneficiaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(beneficiaire)))
            .andExpect(status().isCreated());

        // Validate the Beneficiaire in the database
        List<Beneficiaire> beneficiaireList = beneficiaireRepository.findAll();
        assertThat(beneficiaireList).hasSize(databaseSizeBeforeCreate + 1);
        Beneficiaire testBeneficiaire = beneficiaireList.get(beneficiaireList.size() - 1);
        assertThat(testBeneficiaire.getTypeBeneficiaire()).isEqualTo(DEFAULT_TYPE_BENEFICIAIRE);
        assertThat(testBeneficiaire.getAutreBeneficiaire()).isEqualTo(DEFAULT_AUTRE_BENEFICIAIRE);
        assertThat(testBeneficiaire.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBeneficiaire.getJustification()).isEqualTo(DEFAULT_JUSTIFICATION);
    }

    @Test
    @Transactional
    void createBeneficiaireWithExistingId() throws Exception {
        // Create the Beneficiaire with an existing ID
        beneficiaire.setId(1L);

        int databaseSizeBeforeCreate = beneficiaireRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBeneficiaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(beneficiaire)))
            .andExpect(status().isBadRequest());

        // Validate the Beneficiaire in the database
        List<Beneficiaire> beneficiaireList = beneficiaireRepository.findAll();
        assertThat(beneficiaireList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBeneficiaires() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get all the beneficiaireList
        restBeneficiaireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(beneficiaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeBeneficiaire").value(hasItem(DEFAULT_TYPE_BENEFICIAIRE.toString())))
            .andExpect(jsonPath("$.[*].autreBeneficiaire").value(hasItem(DEFAULT_AUTRE_BENEFICIAIRE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].justification").value(hasItem(DEFAULT_JUSTIFICATION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBeneficiairesWithEagerRelationshipsIsEnabled() throws Exception {
        when(beneficiaireRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBeneficiaireMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(beneficiaireRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBeneficiairesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(beneficiaireRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBeneficiaireMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(beneficiaireRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getBeneficiaire() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        // Get the beneficiaire
        restBeneficiaireMockMvc
            .perform(get(ENTITY_API_URL_ID, beneficiaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(beneficiaire.getId().intValue()))
            .andExpect(jsonPath("$.typeBeneficiaire").value(DEFAULT_TYPE_BENEFICIAIRE.toString()))
            .andExpect(jsonPath("$.autreBeneficiaire").value(DEFAULT_AUTRE_BENEFICIAIRE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.justification").value(DEFAULT_JUSTIFICATION));
    }

    @Test
    @Transactional
    void getNonExistingBeneficiaire() throws Exception {
        // Get the beneficiaire
        restBeneficiaireMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBeneficiaire() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        int databaseSizeBeforeUpdate = beneficiaireRepository.findAll().size();

        // Update the beneficiaire
        Beneficiaire updatedBeneficiaire = beneficiaireRepository.findById(beneficiaire.getId()).get();
        // Disconnect from session so that the updates on updatedBeneficiaire are not directly saved in db
        em.detach(updatedBeneficiaire);
        updatedBeneficiaire
            .typeBeneficiaire(UPDATED_TYPE_BENEFICIAIRE)
            .autreBeneficiaire(UPDATED_AUTRE_BENEFICIAIRE)
            .description(UPDATED_DESCRIPTION)
            .justification(UPDATED_JUSTIFICATION);

        restBeneficiaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBeneficiaire.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBeneficiaire))
            )
            .andExpect(status().isOk());

        // Validate the Beneficiaire in the database
        List<Beneficiaire> beneficiaireList = beneficiaireRepository.findAll();
        assertThat(beneficiaireList).hasSize(databaseSizeBeforeUpdate);
        Beneficiaire testBeneficiaire = beneficiaireList.get(beneficiaireList.size() - 1);
        assertThat(testBeneficiaire.getTypeBeneficiaire()).isEqualTo(UPDATED_TYPE_BENEFICIAIRE);
        assertThat(testBeneficiaire.getAutreBeneficiaire()).isEqualTo(UPDATED_AUTRE_BENEFICIAIRE);
        assertThat(testBeneficiaire.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBeneficiaire.getJustification()).isEqualTo(UPDATED_JUSTIFICATION);
    }

    @Test
    @Transactional
    void putNonExistingBeneficiaire() throws Exception {
        int databaseSizeBeforeUpdate = beneficiaireRepository.findAll().size();
        beneficiaire.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBeneficiaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, beneficiaire.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(beneficiaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Beneficiaire in the database
        List<Beneficiaire> beneficiaireList = beneficiaireRepository.findAll();
        assertThat(beneficiaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBeneficiaire() throws Exception {
        int databaseSizeBeforeUpdate = beneficiaireRepository.findAll().size();
        beneficiaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBeneficiaireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(beneficiaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Beneficiaire in the database
        List<Beneficiaire> beneficiaireList = beneficiaireRepository.findAll();
        assertThat(beneficiaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBeneficiaire() throws Exception {
        int databaseSizeBeforeUpdate = beneficiaireRepository.findAll().size();
        beneficiaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBeneficiaireMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(beneficiaire)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Beneficiaire in the database
        List<Beneficiaire> beneficiaireList = beneficiaireRepository.findAll();
        assertThat(beneficiaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBeneficiaireWithPatch() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        int databaseSizeBeforeUpdate = beneficiaireRepository.findAll().size();

        // Update the beneficiaire using partial update
        Beneficiaire partialUpdatedBeneficiaire = new Beneficiaire();
        partialUpdatedBeneficiaire.setId(beneficiaire.getId());

        partialUpdatedBeneficiaire.typeBeneficiaire(UPDATED_TYPE_BENEFICIAIRE);

        restBeneficiaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBeneficiaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBeneficiaire))
            )
            .andExpect(status().isOk());

        // Validate the Beneficiaire in the database
        List<Beneficiaire> beneficiaireList = beneficiaireRepository.findAll();
        assertThat(beneficiaireList).hasSize(databaseSizeBeforeUpdate);
        Beneficiaire testBeneficiaire = beneficiaireList.get(beneficiaireList.size() - 1);
        assertThat(testBeneficiaire.getTypeBeneficiaire()).isEqualTo(UPDATED_TYPE_BENEFICIAIRE);
        assertThat(testBeneficiaire.getAutreBeneficiaire()).isEqualTo(DEFAULT_AUTRE_BENEFICIAIRE);
        assertThat(testBeneficiaire.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testBeneficiaire.getJustification()).isEqualTo(DEFAULT_JUSTIFICATION);
    }

    @Test
    @Transactional
    void fullUpdateBeneficiaireWithPatch() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        int databaseSizeBeforeUpdate = beneficiaireRepository.findAll().size();

        // Update the beneficiaire using partial update
        Beneficiaire partialUpdatedBeneficiaire = new Beneficiaire();
        partialUpdatedBeneficiaire.setId(beneficiaire.getId());

        partialUpdatedBeneficiaire
            .typeBeneficiaire(UPDATED_TYPE_BENEFICIAIRE)
            .autreBeneficiaire(UPDATED_AUTRE_BENEFICIAIRE)
            .description(UPDATED_DESCRIPTION)
            .justification(UPDATED_JUSTIFICATION);

        restBeneficiaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBeneficiaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBeneficiaire))
            )
            .andExpect(status().isOk());

        // Validate the Beneficiaire in the database
        List<Beneficiaire> beneficiaireList = beneficiaireRepository.findAll();
        assertThat(beneficiaireList).hasSize(databaseSizeBeforeUpdate);
        Beneficiaire testBeneficiaire = beneficiaireList.get(beneficiaireList.size() - 1);
        assertThat(testBeneficiaire.getTypeBeneficiaire()).isEqualTo(UPDATED_TYPE_BENEFICIAIRE);
        assertThat(testBeneficiaire.getAutreBeneficiaire()).isEqualTo(UPDATED_AUTRE_BENEFICIAIRE);
        assertThat(testBeneficiaire.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testBeneficiaire.getJustification()).isEqualTo(UPDATED_JUSTIFICATION);
    }

    @Test
    @Transactional
    void patchNonExistingBeneficiaire() throws Exception {
        int databaseSizeBeforeUpdate = beneficiaireRepository.findAll().size();
        beneficiaire.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBeneficiaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, beneficiaire.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(beneficiaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Beneficiaire in the database
        List<Beneficiaire> beneficiaireList = beneficiaireRepository.findAll();
        assertThat(beneficiaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBeneficiaire() throws Exception {
        int databaseSizeBeforeUpdate = beneficiaireRepository.findAll().size();
        beneficiaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBeneficiaireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(beneficiaire))
            )
            .andExpect(status().isBadRequest());

        // Validate the Beneficiaire in the database
        List<Beneficiaire> beneficiaireList = beneficiaireRepository.findAll();
        assertThat(beneficiaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBeneficiaire() throws Exception {
        int databaseSizeBeforeUpdate = beneficiaireRepository.findAll().size();
        beneficiaire.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBeneficiaireMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(beneficiaire))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Beneficiaire in the database
        List<Beneficiaire> beneficiaireList = beneficiaireRepository.findAll();
        assertThat(beneficiaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBeneficiaire() throws Exception {
        // Initialize the database
        beneficiaireRepository.saveAndFlush(beneficiaire);

        int databaseSizeBeforeDelete = beneficiaireRepository.findAll().size();

        // Delete the beneficiaire
        restBeneficiaireMockMvc
            .perform(delete(ENTITY_API_URL_ID, beneficiaire.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Beneficiaire> beneficiaireList = beneficiaireRepository.findAll();
        assertThat(beneficiaireList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
