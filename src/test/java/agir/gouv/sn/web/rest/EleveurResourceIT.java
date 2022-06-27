package agir.gouv.sn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import agir.gouv.sn.IntegrationTest;
import agir.gouv.sn.domain.Eleveur;
import agir.gouv.sn.domain.enumeration.TypeElevage;
import agir.gouv.sn.repository.EleveurRepository;
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
 * Integration tests for the {@link EleveurResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EleveurResourceIT {

    private static final String DEFAULT_NOM_ELEVEUR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_ELEVEUR = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM_ELEVEUR = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM_ELEVEUR = "BBBBBBBBBB";

    private static final String DEFAULT_TEL_ELEVEUR = "AAAAAAAAAA";
    private static final String UPDATED_TEL_ELEVEUR = "BBBBBBBBBB";

    private static final String DEFAULT_TEL_1_ELEVEUR = "AAAAAAAAAA";
    private static final String UPDATED_TEL_1_ELEVEUR = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final TypeElevage DEFAULT_NOM_ELEVAGE = TypeElevage.Moutons;
    private static final TypeElevage UPDATED_NOM_ELEVAGE = TypeElevage.Volailles;

    private static final String DEFAULT_DESCRIPTION_ACTIVITE = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_ACTIVITE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/eleveurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EleveurRepository eleveurRepository;

    @Mock
    private EleveurRepository eleveurRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEleveurMockMvc;

    private Eleveur eleveur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Eleveur createEntity(EntityManager em) {
        Eleveur eleveur = new Eleveur()
            .nomEleveur(DEFAULT_NOM_ELEVEUR)
            .prenomEleveur(DEFAULT_PRENOM_ELEVEUR)
            .telEleveur(DEFAULT_TEL_ELEVEUR)
            .tel1Eleveur(DEFAULT_TEL_1_ELEVEUR)
            .adresse(DEFAULT_ADRESSE)
            .nomElevage(DEFAULT_NOM_ELEVAGE)
            .descriptionActivite(DEFAULT_DESCRIPTION_ACTIVITE);
        return eleveur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Eleveur createUpdatedEntity(EntityManager em) {
        Eleveur eleveur = new Eleveur()
            .nomEleveur(UPDATED_NOM_ELEVEUR)
            .prenomEleveur(UPDATED_PRENOM_ELEVEUR)
            .telEleveur(UPDATED_TEL_ELEVEUR)
            .tel1Eleveur(UPDATED_TEL_1_ELEVEUR)
            .adresse(UPDATED_ADRESSE)
            .nomElevage(UPDATED_NOM_ELEVAGE)
            .descriptionActivite(UPDATED_DESCRIPTION_ACTIVITE);
        return eleveur;
    }

    @BeforeEach
    public void initTest() {
        eleveur = createEntity(em);
    }

    @Test
    @Transactional
    void createEleveur() throws Exception {
        int databaseSizeBeforeCreate = eleveurRepository.findAll().size();
        // Create the Eleveur
        restEleveurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eleveur)))
            .andExpect(status().isCreated());

        // Validate the Eleveur in the database
        List<Eleveur> eleveurList = eleveurRepository.findAll();
        assertThat(eleveurList).hasSize(databaseSizeBeforeCreate + 1);
        Eleveur testEleveur = eleveurList.get(eleveurList.size() - 1);
        assertThat(testEleveur.getNomEleveur()).isEqualTo(DEFAULT_NOM_ELEVEUR);
        assertThat(testEleveur.getPrenomEleveur()).isEqualTo(DEFAULT_PRENOM_ELEVEUR);
        assertThat(testEleveur.getTelEleveur()).isEqualTo(DEFAULT_TEL_ELEVEUR);
        assertThat(testEleveur.getTel1Eleveur()).isEqualTo(DEFAULT_TEL_1_ELEVEUR);
        assertThat(testEleveur.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testEleveur.getNomElevage()).isEqualTo(DEFAULT_NOM_ELEVAGE);
        assertThat(testEleveur.getDescriptionActivite()).isEqualTo(DEFAULT_DESCRIPTION_ACTIVITE);
    }

    @Test
    @Transactional
    void createEleveurWithExistingId() throws Exception {
        // Create the Eleveur with an existing ID
        eleveur.setId(1L);

        int databaseSizeBeforeCreate = eleveurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEleveurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eleveur)))
            .andExpect(status().isBadRequest());

        // Validate the Eleveur in the database
        List<Eleveur> eleveurList = eleveurRepository.findAll();
        assertThat(eleveurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEleveurs() throws Exception {
        // Initialize the database
        eleveurRepository.saveAndFlush(eleveur);

        // Get all the eleveurList
        restEleveurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eleveur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomEleveur").value(hasItem(DEFAULT_NOM_ELEVEUR)))
            .andExpect(jsonPath("$.[*].prenomEleveur").value(hasItem(DEFAULT_PRENOM_ELEVEUR)))
            .andExpect(jsonPath("$.[*].telEleveur").value(hasItem(DEFAULT_TEL_ELEVEUR)))
            .andExpect(jsonPath("$.[*].tel1Eleveur").value(hasItem(DEFAULT_TEL_1_ELEVEUR)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].nomElevage").value(hasItem(DEFAULT_NOM_ELEVAGE.toString())))
            .andExpect(jsonPath("$.[*].descriptionActivite").value(hasItem(DEFAULT_DESCRIPTION_ACTIVITE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEleveursWithEagerRelationshipsIsEnabled() throws Exception {
        when(eleveurRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEleveurMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(eleveurRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEleveursWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(eleveurRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEleveurMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(eleveurRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getEleveur() throws Exception {
        // Initialize the database
        eleveurRepository.saveAndFlush(eleveur);

        // Get the eleveur
        restEleveurMockMvc
            .perform(get(ENTITY_API_URL_ID, eleveur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eleveur.getId().intValue()))
            .andExpect(jsonPath("$.nomEleveur").value(DEFAULT_NOM_ELEVEUR))
            .andExpect(jsonPath("$.prenomEleveur").value(DEFAULT_PRENOM_ELEVEUR))
            .andExpect(jsonPath("$.telEleveur").value(DEFAULT_TEL_ELEVEUR))
            .andExpect(jsonPath("$.tel1Eleveur").value(DEFAULT_TEL_1_ELEVEUR))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.nomElevage").value(DEFAULT_NOM_ELEVAGE.toString()))
            .andExpect(jsonPath("$.descriptionActivite").value(DEFAULT_DESCRIPTION_ACTIVITE));
    }

    @Test
    @Transactional
    void getNonExistingEleveur() throws Exception {
        // Get the eleveur
        restEleveurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEleveur() throws Exception {
        // Initialize the database
        eleveurRepository.saveAndFlush(eleveur);

        int databaseSizeBeforeUpdate = eleveurRepository.findAll().size();

        // Update the eleveur
        Eleveur updatedEleveur = eleveurRepository.findById(eleveur.getId()).get();
        // Disconnect from session so that the updates on updatedEleveur are not directly saved in db
        em.detach(updatedEleveur);
        updatedEleveur
            .nomEleveur(UPDATED_NOM_ELEVEUR)
            .prenomEleveur(UPDATED_PRENOM_ELEVEUR)
            .telEleveur(UPDATED_TEL_ELEVEUR)
            .tel1Eleveur(UPDATED_TEL_1_ELEVEUR)
            .adresse(UPDATED_ADRESSE)
            .nomElevage(UPDATED_NOM_ELEVAGE)
            .descriptionActivite(UPDATED_DESCRIPTION_ACTIVITE);

        restEleveurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEleveur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEleveur))
            )
            .andExpect(status().isOk());

        // Validate the Eleveur in the database
        List<Eleveur> eleveurList = eleveurRepository.findAll();
        assertThat(eleveurList).hasSize(databaseSizeBeforeUpdate);
        Eleveur testEleveur = eleveurList.get(eleveurList.size() - 1);
        assertThat(testEleveur.getNomEleveur()).isEqualTo(UPDATED_NOM_ELEVEUR);
        assertThat(testEleveur.getPrenomEleveur()).isEqualTo(UPDATED_PRENOM_ELEVEUR);
        assertThat(testEleveur.getTelEleveur()).isEqualTo(UPDATED_TEL_ELEVEUR);
        assertThat(testEleveur.getTel1Eleveur()).isEqualTo(UPDATED_TEL_1_ELEVEUR);
        assertThat(testEleveur.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testEleveur.getNomElevage()).isEqualTo(UPDATED_NOM_ELEVAGE);
        assertThat(testEleveur.getDescriptionActivite()).isEqualTo(UPDATED_DESCRIPTION_ACTIVITE);
    }

    @Test
    @Transactional
    void putNonExistingEleveur() throws Exception {
        int databaseSizeBeforeUpdate = eleveurRepository.findAll().size();
        eleveur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEleveurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eleveur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eleveur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Eleveur in the database
        List<Eleveur> eleveurList = eleveurRepository.findAll();
        assertThat(eleveurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEleveur() throws Exception {
        int databaseSizeBeforeUpdate = eleveurRepository.findAll().size();
        eleveur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEleveurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eleveur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Eleveur in the database
        List<Eleveur> eleveurList = eleveurRepository.findAll();
        assertThat(eleveurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEleveur() throws Exception {
        int databaseSizeBeforeUpdate = eleveurRepository.findAll().size();
        eleveur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEleveurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eleveur)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Eleveur in the database
        List<Eleveur> eleveurList = eleveurRepository.findAll();
        assertThat(eleveurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEleveurWithPatch() throws Exception {
        // Initialize the database
        eleveurRepository.saveAndFlush(eleveur);

        int databaseSizeBeforeUpdate = eleveurRepository.findAll().size();

        // Update the eleveur using partial update
        Eleveur partialUpdatedEleveur = new Eleveur();
        partialUpdatedEleveur.setId(eleveur.getId());

        partialUpdatedEleveur.prenomEleveur(UPDATED_PRENOM_ELEVEUR).nomElevage(UPDATED_NOM_ELEVAGE);

        restEleveurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEleveur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEleveur))
            )
            .andExpect(status().isOk());

        // Validate the Eleveur in the database
        List<Eleveur> eleveurList = eleveurRepository.findAll();
        assertThat(eleveurList).hasSize(databaseSizeBeforeUpdate);
        Eleveur testEleveur = eleveurList.get(eleveurList.size() - 1);
        assertThat(testEleveur.getNomEleveur()).isEqualTo(DEFAULT_NOM_ELEVEUR);
        assertThat(testEleveur.getPrenomEleveur()).isEqualTo(UPDATED_PRENOM_ELEVEUR);
        assertThat(testEleveur.getTelEleveur()).isEqualTo(DEFAULT_TEL_ELEVEUR);
        assertThat(testEleveur.getTel1Eleveur()).isEqualTo(DEFAULT_TEL_1_ELEVEUR);
        assertThat(testEleveur.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testEleveur.getNomElevage()).isEqualTo(UPDATED_NOM_ELEVAGE);
        assertThat(testEleveur.getDescriptionActivite()).isEqualTo(DEFAULT_DESCRIPTION_ACTIVITE);
    }

    @Test
    @Transactional
    void fullUpdateEleveurWithPatch() throws Exception {
        // Initialize the database
        eleveurRepository.saveAndFlush(eleveur);

        int databaseSizeBeforeUpdate = eleveurRepository.findAll().size();

        // Update the eleveur using partial update
        Eleveur partialUpdatedEleveur = new Eleveur();
        partialUpdatedEleveur.setId(eleveur.getId());

        partialUpdatedEleveur
            .nomEleveur(UPDATED_NOM_ELEVEUR)
            .prenomEleveur(UPDATED_PRENOM_ELEVEUR)
            .telEleveur(UPDATED_TEL_ELEVEUR)
            .tel1Eleveur(UPDATED_TEL_1_ELEVEUR)
            .adresse(UPDATED_ADRESSE)
            .nomElevage(UPDATED_NOM_ELEVAGE)
            .descriptionActivite(UPDATED_DESCRIPTION_ACTIVITE);

        restEleveurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEleveur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEleveur))
            )
            .andExpect(status().isOk());

        // Validate the Eleveur in the database
        List<Eleveur> eleveurList = eleveurRepository.findAll();
        assertThat(eleveurList).hasSize(databaseSizeBeforeUpdate);
        Eleveur testEleveur = eleveurList.get(eleveurList.size() - 1);
        assertThat(testEleveur.getNomEleveur()).isEqualTo(UPDATED_NOM_ELEVEUR);
        assertThat(testEleveur.getPrenomEleveur()).isEqualTo(UPDATED_PRENOM_ELEVEUR);
        assertThat(testEleveur.getTelEleveur()).isEqualTo(UPDATED_TEL_ELEVEUR);
        assertThat(testEleveur.getTel1Eleveur()).isEqualTo(UPDATED_TEL_1_ELEVEUR);
        assertThat(testEleveur.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testEleveur.getNomElevage()).isEqualTo(UPDATED_NOM_ELEVAGE);
        assertThat(testEleveur.getDescriptionActivite()).isEqualTo(UPDATED_DESCRIPTION_ACTIVITE);
    }

    @Test
    @Transactional
    void patchNonExistingEleveur() throws Exception {
        int databaseSizeBeforeUpdate = eleveurRepository.findAll().size();
        eleveur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEleveurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eleveur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eleveur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Eleveur in the database
        List<Eleveur> eleveurList = eleveurRepository.findAll();
        assertThat(eleveurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEleveur() throws Exception {
        int databaseSizeBeforeUpdate = eleveurRepository.findAll().size();
        eleveur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEleveurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eleveur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Eleveur in the database
        List<Eleveur> eleveurList = eleveurRepository.findAll();
        assertThat(eleveurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEleveur() throws Exception {
        int databaseSizeBeforeUpdate = eleveurRepository.findAll().size();
        eleveur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEleveurMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(eleveur)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Eleveur in the database
        List<Eleveur> eleveurList = eleveurRepository.findAll();
        assertThat(eleveurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEleveur() throws Exception {
        // Initialize the database
        eleveurRepository.saveAndFlush(eleveur);

        int databaseSizeBeforeDelete = eleveurRepository.findAll().size();

        // Delete the eleveur
        restEleveurMockMvc
            .perform(delete(ENTITY_API_URL_ID, eleveur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Eleveur> eleveurList = eleveurRepository.findAll();
        assertThat(eleveurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
