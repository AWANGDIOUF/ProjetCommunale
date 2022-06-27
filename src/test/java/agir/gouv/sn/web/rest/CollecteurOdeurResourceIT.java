package agir.gouv.sn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import agir.gouv.sn.IntegrationTest;
import agir.gouv.sn.domain.CollecteurOdeur;
import agir.gouv.sn.repository.CollecteurOdeurRepository;
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
 * Integration tests for the {@link CollecteurOdeurResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CollecteurOdeurResourceIT {

    private static final String DEFAULT_NOM_COLLECTEUR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_COLLECTEUR = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM_COLLECTEUR = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM_COLLECTEUR = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_TEL_1 = "AAAAAAAAAA";
    private static final String UPDATED_TEL_1 = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/collecteur-odeurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CollecteurOdeurRepository collecteurOdeurRepository;

    @Mock
    private CollecteurOdeurRepository collecteurOdeurRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCollecteurOdeurMockMvc;

    private CollecteurOdeur collecteurOdeur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CollecteurOdeur createEntity(EntityManager em) {
        CollecteurOdeur collecteurOdeur = new CollecteurOdeur()
            .nomCollecteur(DEFAULT_NOM_COLLECTEUR)
            .prenomCollecteur(DEFAULT_PRENOM_COLLECTEUR)
            .date(DEFAULT_DATE)
            .tel1(DEFAULT_TEL_1);
        return collecteurOdeur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CollecteurOdeur createUpdatedEntity(EntityManager em) {
        CollecteurOdeur collecteurOdeur = new CollecteurOdeur()
            .nomCollecteur(UPDATED_NOM_COLLECTEUR)
            .prenomCollecteur(UPDATED_PRENOM_COLLECTEUR)
            .date(UPDATED_DATE)
            .tel1(UPDATED_TEL_1);
        return collecteurOdeur;
    }

    @BeforeEach
    public void initTest() {
        collecteurOdeur = createEntity(em);
    }

    @Test
    @Transactional
    void createCollecteurOdeur() throws Exception {
        int databaseSizeBeforeCreate = collecteurOdeurRepository.findAll().size();
        // Create the CollecteurOdeur
        restCollecteurOdeurMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(collecteurOdeur))
            )
            .andExpect(status().isCreated());

        // Validate the CollecteurOdeur in the database
        List<CollecteurOdeur> collecteurOdeurList = collecteurOdeurRepository.findAll();
        assertThat(collecteurOdeurList).hasSize(databaseSizeBeforeCreate + 1);
        CollecteurOdeur testCollecteurOdeur = collecteurOdeurList.get(collecteurOdeurList.size() - 1);
        assertThat(testCollecteurOdeur.getNomCollecteur()).isEqualTo(DEFAULT_NOM_COLLECTEUR);
        assertThat(testCollecteurOdeur.getPrenomCollecteur()).isEqualTo(DEFAULT_PRENOM_COLLECTEUR);
        assertThat(testCollecteurOdeur.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testCollecteurOdeur.getTel1()).isEqualTo(DEFAULT_TEL_1);
    }

    @Test
    @Transactional
    void createCollecteurOdeurWithExistingId() throws Exception {
        // Create the CollecteurOdeur with an existing ID
        collecteurOdeur.setId(1L);

        int databaseSizeBeforeCreate = collecteurOdeurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCollecteurOdeurMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(collecteurOdeur))
            )
            .andExpect(status().isBadRequest());

        // Validate the CollecteurOdeur in the database
        List<CollecteurOdeur> collecteurOdeurList = collecteurOdeurRepository.findAll();
        assertThat(collecteurOdeurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCollecteurOdeurs() throws Exception {
        // Initialize the database
        collecteurOdeurRepository.saveAndFlush(collecteurOdeur);

        // Get all the collecteurOdeurList
        restCollecteurOdeurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(collecteurOdeur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomCollecteur").value(hasItem(DEFAULT_NOM_COLLECTEUR)))
            .andExpect(jsonPath("$.[*].prenomCollecteur").value(hasItem(DEFAULT_PRENOM_COLLECTEUR)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].tel1").value(hasItem(DEFAULT_TEL_1)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCollecteurOdeursWithEagerRelationshipsIsEnabled() throws Exception {
        when(collecteurOdeurRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCollecteurOdeurMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(collecteurOdeurRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCollecteurOdeursWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(collecteurOdeurRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCollecteurOdeurMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(collecteurOdeurRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getCollecteurOdeur() throws Exception {
        // Initialize the database
        collecteurOdeurRepository.saveAndFlush(collecteurOdeur);

        // Get the collecteurOdeur
        restCollecteurOdeurMockMvc
            .perform(get(ENTITY_API_URL_ID, collecteurOdeur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(collecteurOdeur.getId().intValue()))
            .andExpect(jsonPath("$.nomCollecteur").value(DEFAULT_NOM_COLLECTEUR))
            .andExpect(jsonPath("$.prenomCollecteur").value(DEFAULT_PRENOM_COLLECTEUR))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.tel1").value(DEFAULT_TEL_1));
    }

    @Test
    @Transactional
    void getNonExistingCollecteurOdeur() throws Exception {
        // Get the collecteurOdeur
        restCollecteurOdeurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCollecteurOdeur() throws Exception {
        // Initialize the database
        collecteurOdeurRepository.saveAndFlush(collecteurOdeur);

        int databaseSizeBeforeUpdate = collecteurOdeurRepository.findAll().size();

        // Update the collecteurOdeur
        CollecteurOdeur updatedCollecteurOdeur = collecteurOdeurRepository.findById(collecteurOdeur.getId()).get();
        // Disconnect from session so that the updates on updatedCollecteurOdeur are not directly saved in db
        em.detach(updatedCollecteurOdeur);
        updatedCollecteurOdeur
            .nomCollecteur(UPDATED_NOM_COLLECTEUR)
            .prenomCollecteur(UPDATED_PRENOM_COLLECTEUR)
            .date(UPDATED_DATE)
            .tel1(UPDATED_TEL_1);

        restCollecteurOdeurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCollecteurOdeur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCollecteurOdeur))
            )
            .andExpect(status().isOk());

        // Validate the CollecteurOdeur in the database
        List<CollecteurOdeur> collecteurOdeurList = collecteurOdeurRepository.findAll();
        assertThat(collecteurOdeurList).hasSize(databaseSizeBeforeUpdate);
        CollecteurOdeur testCollecteurOdeur = collecteurOdeurList.get(collecteurOdeurList.size() - 1);
        assertThat(testCollecteurOdeur.getNomCollecteur()).isEqualTo(UPDATED_NOM_COLLECTEUR);
        assertThat(testCollecteurOdeur.getPrenomCollecteur()).isEqualTo(UPDATED_PRENOM_COLLECTEUR);
        assertThat(testCollecteurOdeur.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testCollecteurOdeur.getTel1()).isEqualTo(UPDATED_TEL_1);
    }

    @Test
    @Transactional
    void putNonExistingCollecteurOdeur() throws Exception {
        int databaseSizeBeforeUpdate = collecteurOdeurRepository.findAll().size();
        collecteurOdeur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCollecteurOdeurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, collecteurOdeur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collecteurOdeur))
            )
            .andExpect(status().isBadRequest());

        // Validate the CollecteurOdeur in the database
        List<CollecteurOdeur> collecteurOdeurList = collecteurOdeurRepository.findAll();
        assertThat(collecteurOdeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCollecteurOdeur() throws Exception {
        int databaseSizeBeforeUpdate = collecteurOdeurRepository.findAll().size();
        collecteurOdeur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollecteurOdeurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(collecteurOdeur))
            )
            .andExpect(status().isBadRequest());

        // Validate the CollecteurOdeur in the database
        List<CollecteurOdeur> collecteurOdeurList = collecteurOdeurRepository.findAll();
        assertThat(collecteurOdeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCollecteurOdeur() throws Exception {
        int databaseSizeBeforeUpdate = collecteurOdeurRepository.findAll().size();
        collecteurOdeur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollecteurOdeurMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(collecteurOdeur))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CollecteurOdeur in the database
        List<CollecteurOdeur> collecteurOdeurList = collecteurOdeurRepository.findAll();
        assertThat(collecteurOdeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCollecteurOdeurWithPatch() throws Exception {
        // Initialize the database
        collecteurOdeurRepository.saveAndFlush(collecteurOdeur);

        int databaseSizeBeforeUpdate = collecteurOdeurRepository.findAll().size();

        // Update the collecteurOdeur using partial update
        CollecteurOdeur partialUpdatedCollecteurOdeur = new CollecteurOdeur();
        partialUpdatedCollecteurOdeur.setId(collecteurOdeur.getId());

        partialUpdatedCollecteurOdeur.prenomCollecteur(UPDATED_PRENOM_COLLECTEUR).tel1(UPDATED_TEL_1);

        restCollecteurOdeurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCollecteurOdeur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCollecteurOdeur))
            )
            .andExpect(status().isOk());

        // Validate the CollecteurOdeur in the database
        List<CollecteurOdeur> collecteurOdeurList = collecteurOdeurRepository.findAll();
        assertThat(collecteurOdeurList).hasSize(databaseSizeBeforeUpdate);
        CollecteurOdeur testCollecteurOdeur = collecteurOdeurList.get(collecteurOdeurList.size() - 1);
        assertThat(testCollecteurOdeur.getNomCollecteur()).isEqualTo(DEFAULT_NOM_COLLECTEUR);
        assertThat(testCollecteurOdeur.getPrenomCollecteur()).isEqualTo(UPDATED_PRENOM_COLLECTEUR);
        assertThat(testCollecteurOdeur.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testCollecteurOdeur.getTel1()).isEqualTo(UPDATED_TEL_1);
    }

    @Test
    @Transactional
    void fullUpdateCollecteurOdeurWithPatch() throws Exception {
        // Initialize the database
        collecteurOdeurRepository.saveAndFlush(collecteurOdeur);

        int databaseSizeBeforeUpdate = collecteurOdeurRepository.findAll().size();

        // Update the collecteurOdeur using partial update
        CollecteurOdeur partialUpdatedCollecteurOdeur = new CollecteurOdeur();
        partialUpdatedCollecteurOdeur.setId(collecteurOdeur.getId());

        partialUpdatedCollecteurOdeur
            .nomCollecteur(UPDATED_NOM_COLLECTEUR)
            .prenomCollecteur(UPDATED_PRENOM_COLLECTEUR)
            .date(UPDATED_DATE)
            .tel1(UPDATED_TEL_1);

        restCollecteurOdeurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCollecteurOdeur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCollecteurOdeur))
            )
            .andExpect(status().isOk());

        // Validate the CollecteurOdeur in the database
        List<CollecteurOdeur> collecteurOdeurList = collecteurOdeurRepository.findAll();
        assertThat(collecteurOdeurList).hasSize(databaseSizeBeforeUpdate);
        CollecteurOdeur testCollecteurOdeur = collecteurOdeurList.get(collecteurOdeurList.size() - 1);
        assertThat(testCollecteurOdeur.getNomCollecteur()).isEqualTo(UPDATED_NOM_COLLECTEUR);
        assertThat(testCollecteurOdeur.getPrenomCollecteur()).isEqualTo(UPDATED_PRENOM_COLLECTEUR);
        assertThat(testCollecteurOdeur.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testCollecteurOdeur.getTel1()).isEqualTo(UPDATED_TEL_1);
    }

    @Test
    @Transactional
    void patchNonExistingCollecteurOdeur() throws Exception {
        int databaseSizeBeforeUpdate = collecteurOdeurRepository.findAll().size();
        collecteurOdeur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCollecteurOdeurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, collecteurOdeur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(collecteurOdeur))
            )
            .andExpect(status().isBadRequest());

        // Validate the CollecteurOdeur in the database
        List<CollecteurOdeur> collecteurOdeurList = collecteurOdeurRepository.findAll();
        assertThat(collecteurOdeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCollecteurOdeur() throws Exception {
        int databaseSizeBeforeUpdate = collecteurOdeurRepository.findAll().size();
        collecteurOdeur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollecteurOdeurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(collecteurOdeur))
            )
            .andExpect(status().isBadRequest());

        // Validate the CollecteurOdeur in the database
        List<CollecteurOdeur> collecteurOdeurList = collecteurOdeurRepository.findAll();
        assertThat(collecteurOdeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCollecteurOdeur() throws Exception {
        int databaseSizeBeforeUpdate = collecteurOdeurRepository.findAll().size();
        collecteurOdeur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCollecteurOdeurMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(collecteurOdeur))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CollecteurOdeur in the database
        List<CollecteurOdeur> collecteurOdeurList = collecteurOdeurRepository.findAll();
        assertThat(collecteurOdeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCollecteurOdeur() throws Exception {
        // Initialize the database
        collecteurOdeurRepository.saveAndFlush(collecteurOdeur);

        int databaseSizeBeforeDelete = collecteurOdeurRepository.findAll().size();

        // Delete the collecteurOdeur
        restCollecteurOdeurMockMvc
            .perform(delete(ENTITY_API_URL_ID, collecteurOdeur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CollecteurOdeur> collecteurOdeurList = collecteurOdeurRepository.findAll();
        assertThat(collecteurOdeurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
