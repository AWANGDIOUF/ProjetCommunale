package agir.gouv.sn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import agir.gouv.sn.IntegrationTest;
import agir.gouv.sn.domain.UtilisationInternet;
import agir.gouv.sn.domain.enumeration.Domaine;
import agir.gouv.sn.domain.enumeration.Profil;
import agir.gouv.sn.repository.UtilisationInternetRepository;
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
 * Integration tests for the {@link UtilisationInternetResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UtilisationInternetResourceIT {

    private static final Profil DEFAULT_PROFIL = Profil.Commercant;
    private static final Profil UPDATED_PROFIL = Profil.Chercheur_emploi;

    private static final String DEFAULT_AUTRE = "AAAAAAAAAA";
    private static final String UPDATED_AUTRE = "BBBBBBBBBB";

    private static final Domaine DEFAULT_DOMAINE = Domaine.Chanteurs;
    private static final Domaine UPDATED_DOMAINE = Domaine.Danseurs;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/utilisation-internets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UtilisationInternetRepository utilisationInternetRepository;

    @Mock
    private UtilisationInternetRepository utilisationInternetRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUtilisationInternetMockMvc;

    private UtilisationInternet utilisationInternet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UtilisationInternet createEntity(EntityManager em) {
        UtilisationInternet utilisationInternet = new UtilisationInternet()
            .profil(DEFAULT_PROFIL)
            .autre(DEFAULT_AUTRE)
            .domaine(DEFAULT_DOMAINE)
            .description(DEFAULT_DESCRIPTION);
        return utilisationInternet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UtilisationInternet createUpdatedEntity(EntityManager em) {
        UtilisationInternet utilisationInternet = new UtilisationInternet()
            .profil(UPDATED_PROFIL)
            .autre(UPDATED_AUTRE)
            .domaine(UPDATED_DOMAINE)
            .description(UPDATED_DESCRIPTION);
        return utilisationInternet;
    }

    @BeforeEach
    public void initTest() {
        utilisationInternet = createEntity(em);
    }

    @Test
    @Transactional
    void createUtilisationInternet() throws Exception {
        int databaseSizeBeforeCreate = utilisationInternetRepository.findAll().size();
        // Create the UtilisationInternet
        restUtilisationInternetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(utilisationInternet))
            )
            .andExpect(status().isCreated());

        // Validate the UtilisationInternet in the database
        List<UtilisationInternet> utilisationInternetList = utilisationInternetRepository.findAll();
        assertThat(utilisationInternetList).hasSize(databaseSizeBeforeCreate + 1);
        UtilisationInternet testUtilisationInternet = utilisationInternetList.get(utilisationInternetList.size() - 1);
        assertThat(testUtilisationInternet.getProfil()).isEqualTo(DEFAULT_PROFIL);
        assertThat(testUtilisationInternet.getAutre()).isEqualTo(DEFAULT_AUTRE);
        assertThat(testUtilisationInternet.getDomaine()).isEqualTo(DEFAULT_DOMAINE);
        assertThat(testUtilisationInternet.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createUtilisationInternetWithExistingId() throws Exception {
        // Create the UtilisationInternet with an existing ID
        utilisationInternet.setId(1L);

        int databaseSizeBeforeCreate = utilisationInternetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUtilisationInternetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(utilisationInternet))
            )
            .andExpect(status().isBadRequest());

        // Validate the UtilisationInternet in the database
        List<UtilisationInternet> utilisationInternetList = utilisationInternetRepository.findAll();
        assertThat(utilisationInternetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUtilisationInternets() throws Exception {
        // Initialize the database
        utilisationInternetRepository.saveAndFlush(utilisationInternet);

        // Get all the utilisationInternetList
        restUtilisationInternetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(utilisationInternet.getId().intValue())))
            .andExpect(jsonPath("$.[*].profil").value(hasItem(DEFAULT_PROFIL.toString())))
            .andExpect(jsonPath("$.[*].autre").value(hasItem(DEFAULT_AUTRE)))
            .andExpect(jsonPath("$.[*].domaine").value(hasItem(DEFAULT_DOMAINE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUtilisationInternetsWithEagerRelationshipsIsEnabled() throws Exception {
        when(utilisationInternetRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUtilisationInternetMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(utilisationInternetRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUtilisationInternetsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(utilisationInternetRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUtilisationInternetMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(utilisationInternetRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getUtilisationInternet() throws Exception {
        // Initialize the database
        utilisationInternetRepository.saveAndFlush(utilisationInternet);

        // Get the utilisationInternet
        restUtilisationInternetMockMvc
            .perform(get(ENTITY_API_URL_ID, utilisationInternet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(utilisationInternet.getId().intValue()))
            .andExpect(jsonPath("$.profil").value(DEFAULT_PROFIL.toString()))
            .andExpect(jsonPath("$.autre").value(DEFAULT_AUTRE))
            .andExpect(jsonPath("$.domaine").value(DEFAULT_DOMAINE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUtilisationInternet() throws Exception {
        // Get the utilisationInternet
        restUtilisationInternetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewUtilisationInternet() throws Exception {
        // Initialize the database
        utilisationInternetRepository.saveAndFlush(utilisationInternet);

        int databaseSizeBeforeUpdate = utilisationInternetRepository.findAll().size();

        // Update the utilisationInternet
        UtilisationInternet updatedUtilisationInternet = utilisationInternetRepository.findById(utilisationInternet.getId()).get();
        // Disconnect from session so that the updates on updatedUtilisationInternet are not directly saved in db
        em.detach(updatedUtilisationInternet);
        updatedUtilisationInternet.profil(UPDATED_PROFIL).autre(UPDATED_AUTRE).domaine(UPDATED_DOMAINE).description(UPDATED_DESCRIPTION);

        restUtilisationInternetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUtilisationInternet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUtilisationInternet))
            )
            .andExpect(status().isOk());

        // Validate the UtilisationInternet in the database
        List<UtilisationInternet> utilisationInternetList = utilisationInternetRepository.findAll();
        assertThat(utilisationInternetList).hasSize(databaseSizeBeforeUpdate);
        UtilisationInternet testUtilisationInternet = utilisationInternetList.get(utilisationInternetList.size() - 1);
        assertThat(testUtilisationInternet.getProfil()).isEqualTo(UPDATED_PROFIL);
        assertThat(testUtilisationInternet.getAutre()).isEqualTo(UPDATED_AUTRE);
        assertThat(testUtilisationInternet.getDomaine()).isEqualTo(UPDATED_DOMAINE);
        assertThat(testUtilisationInternet.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingUtilisationInternet() throws Exception {
        int databaseSizeBeforeUpdate = utilisationInternetRepository.findAll().size();
        utilisationInternet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUtilisationInternetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, utilisationInternet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(utilisationInternet))
            )
            .andExpect(status().isBadRequest());

        // Validate the UtilisationInternet in the database
        List<UtilisationInternet> utilisationInternetList = utilisationInternetRepository.findAll();
        assertThat(utilisationInternetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUtilisationInternet() throws Exception {
        int databaseSizeBeforeUpdate = utilisationInternetRepository.findAll().size();
        utilisationInternet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilisationInternetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(utilisationInternet))
            )
            .andExpect(status().isBadRequest());

        // Validate the UtilisationInternet in the database
        List<UtilisationInternet> utilisationInternetList = utilisationInternetRepository.findAll();
        assertThat(utilisationInternetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUtilisationInternet() throws Exception {
        int databaseSizeBeforeUpdate = utilisationInternetRepository.findAll().size();
        utilisationInternet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilisationInternetMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(utilisationInternet))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UtilisationInternet in the database
        List<UtilisationInternet> utilisationInternetList = utilisationInternetRepository.findAll();
        assertThat(utilisationInternetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUtilisationInternetWithPatch() throws Exception {
        // Initialize the database
        utilisationInternetRepository.saveAndFlush(utilisationInternet);

        int databaseSizeBeforeUpdate = utilisationInternetRepository.findAll().size();

        // Update the utilisationInternet using partial update
        UtilisationInternet partialUpdatedUtilisationInternet = new UtilisationInternet();
        partialUpdatedUtilisationInternet.setId(utilisationInternet.getId());

        partialUpdatedUtilisationInternet.profil(UPDATED_PROFIL).description(UPDATED_DESCRIPTION);

        restUtilisationInternetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUtilisationInternet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUtilisationInternet))
            )
            .andExpect(status().isOk());

        // Validate the UtilisationInternet in the database
        List<UtilisationInternet> utilisationInternetList = utilisationInternetRepository.findAll();
        assertThat(utilisationInternetList).hasSize(databaseSizeBeforeUpdate);
        UtilisationInternet testUtilisationInternet = utilisationInternetList.get(utilisationInternetList.size() - 1);
        assertThat(testUtilisationInternet.getProfil()).isEqualTo(UPDATED_PROFIL);
        assertThat(testUtilisationInternet.getAutre()).isEqualTo(DEFAULT_AUTRE);
        assertThat(testUtilisationInternet.getDomaine()).isEqualTo(DEFAULT_DOMAINE);
        assertThat(testUtilisationInternet.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateUtilisationInternetWithPatch() throws Exception {
        // Initialize the database
        utilisationInternetRepository.saveAndFlush(utilisationInternet);

        int databaseSizeBeforeUpdate = utilisationInternetRepository.findAll().size();

        // Update the utilisationInternet using partial update
        UtilisationInternet partialUpdatedUtilisationInternet = new UtilisationInternet();
        partialUpdatedUtilisationInternet.setId(utilisationInternet.getId());

        partialUpdatedUtilisationInternet
            .profil(UPDATED_PROFIL)
            .autre(UPDATED_AUTRE)
            .domaine(UPDATED_DOMAINE)
            .description(UPDATED_DESCRIPTION);

        restUtilisationInternetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUtilisationInternet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUtilisationInternet))
            )
            .andExpect(status().isOk());

        // Validate the UtilisationInternet in the database
        List<UtilisationInternet> utilisationInternetList = utilisationInternetRepository.findAll();
        assertThat(utilisationInternetList).hasSize(databaseSizeBeforeUpdate);
        UtilisationInternet testUtilisationInternet = utilisationInternetList.get(utilisationInternetList.size() - 1);
        assertThat(testUtilisationInternet.getProfil()).isEqualTo(UPDATED_PROFIL);
        assertThat(testUtilisationInternet.getAutre()).isEqualTo(UPDATED_AUTRE);
        assertThat(testUtilisationInternet.getDomaine()).isEqualTo(UPDATED_DOMAINE);
        assertThat(testUtilisationInternet.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingUtilisationInternet() throws Exception {
        int databaseSizeBeforeUpdate = utilisationInternetRepository.findAll().size();
        utilisationInternet.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUtilisationInternetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, utilisationInternet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(utilisationInternet))
            )
            .andExpect(status().isBadRequest());

        // Validate the UtilisationInternet in the database
        List<UtilisationInternet> utilisationInternetList = utilisationInternetRepository.findAll();
        assertThat(utilisationInternetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUtilisationInternet() throws Exception {
        int databaseSizeBeforeUpdate = utilisationInternetRepository.findAll().size();
        utilisationInternet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilisationInternetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(utilisationInternet))
            )
            .andExpect(status().isBadRequest());

        // Validate the UtilisationInternet in the database
        List<UtilisationInternet> utilisationInternetList = utilisationInternetRepository.findAll();
        assertThat(utilisationInternetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUtilisationInternet() throws Exception {
        int databaseSizeBeforeUpdate = utilisationInternetRepository.findAll().size();
        utilisationInternet.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilisationInternetMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(utilisationInternet))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UtilisationInternet in the database
        List<UtilisationInternet> utilisationInternetList = utilisationInternetRepository.findAll();
        assertThat(utilisationInternetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUtilisationInternet() throws Exception {
        // Initialize the database
        utilisationInternetRepository.saveAndFlush(utilisationInternet);

        int databaseSizeBeforeDelete = utilisationInternetRepository.findAll().size();

        // Delete the utilisationInternet
        restUtilisationInternetMockMvc
            .perform(delete(ENTITY_API_URL_ID, utilisationInternet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UtilisationInternet> utilisationInternetList = utilisationInternetRepository.findAll();
        assertThat(utilisationInternetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
