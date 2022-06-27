package agir.gouv.sn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import agir.gouv.sn.IntegrationTest;
import agir.gouv.sn.domain.Partenaires;
import agir.gouv.sn.repository.PartenairesRepository;
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
 * Integration tests for the {@link PartenairesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PartenairesResourceIT {

    private static final String DEFAULT_NOM_PARTENAIRE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_PARTENAIRE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_PARTENAIRE = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_PARTENAIRE = "BBBBBBBBBB";

    private static final String DEFAULT_TEL_PARTENAIRE = "AAAAAAAAAA";
    private static final String UPDATED_TEL_PARTENAIRE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/partenaires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PartenairesRepository partenairesRepository;

    @Mock
    private PartenairesRepository partenairesRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPartenairesMockMvc;

    private Partenaires partenaires;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Partenaires createEntity(EntityManager em) {
        Partenaires partenaires = new Partenaires()
            .nomPartenaire(DEFAULT_NOM_PARTENAIRE)
            .emailPartenaire(DEFAULT_EMAIL_PARTENAIRE)
            .telPartenaire(DEFAULT_TEL_PARTENAIRE)
            .description(DEFAULT_DESCRIPTION);
        return partenaires;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Partenaires createUpdatedEntity(EntityManager em) {
        Partenaires partenaires = new Partenaires()
            .nomPartenaire(UPDATED_NOM_PARTENAIRE)
            .emailPartenaire(UPDATED_EMAIL_PARTENAIRE)
            .telPartenaire(UPDATED_TEL_PARTENAIRE)
            .description(UPDATED_DESCRIPTION);
        return partenaires;
    }

    @BeforeEach
    public void initTest() {
        partenaires = createEntity(em);
    }

    @Test
    @Transactional
    void createPartenaires() throws Exception {
        int databaseSizeBeforeCreate = partenairesRepository.findAll().size();
        // Create the Partenaires
        restPartenairesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partenaires)))
            .andExpect(status().isCreated());

        // Validate the Partenaires in the database
        List<Partenaires> partenairesList = partenairesRepository.findAll();
        assertThat(partenairesList).hasSize(databaseSizeBeforeCreate + 1);
        Partenaires testPartenaires = partenairesList.get(partenairesList.size() - 1);
        assertThat(testPartenaires.getNomPartenaire()).isEqualTo(DEFAULT_NOM_PARTENAIRE);
        assertThat(testPartenaires.getEmailPartenaire()).isEqualTo(DEFAULT_EMAIL_PARTENAIRE);
        assertThat(testPartenaires.getTelPartenaire()).isEqualTo(DEFAULT_TEL_PARTENAIRE);
        assertThat(testPartenaires.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createPartenairesWithExistingId() throws Exception {
        // Create the Partenaires with an existing ID
        partenaires.setId(1L);

        int databaseSizeBeforeCreate = partenairesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartenairesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partenaires)))
            .andExpect(status().isBadRequest());

        // Validate the Partenaires in the database
        List<Partenaires> partenairesList = partenairesRepository.findAll();
        assertThat(partenairesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPartenaires() throws Exception {
        // Initialize the database
        partenairesRepository.saveAndFlush(partenaires);

        // Get all the partenairesList
        restPartenairesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partenaires.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomPartenaire").value(hasItem(DEFAULT_NOM_PARTENAIRE)))
            .andExpect(jsonPath("$.[*].emailPartenaire").value(hasItem(DEFAULT_EMAIL_PARTENAIRE)))
            .andExpect(jsonPath("$.[*].telPartenaire").value(hasItem(DEFAULT_TEL_PARTENAIRE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPartenairesWithEagerRelationshipsIsEnabled() throws Exception {
        when(partenairesRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPartenairesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(partenairesRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPartenairesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(partenairesRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPartenairesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(partenairesRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getPartenaires() throws Exception {
        // Initialize the database
        partenairesRepository.saveAndFlush(partenaires);

        // Get the partenaires
        restPartenairesMockMvc
            .perform(get(ENTITY_API_URL_ID, partenaires.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(partenaires.getId().intValue()))
            .andExpect(jsonPath("$.nomPartenaire").value(DEFAULT_NOM_PARTENAIRE))
            .andExpect(jsonPath("$.emailPartenaire").value(DEFAULT_EMAIL_PARTENAIRE))
            .andExpect(jsonPath("$.telPartenaire").value(DEFAULT_TEL_PARTENAIRE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingPartenaires() throws Exception {
        // Get the partenaires
        restPartenairesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPartenaires() throws Exception {
        // Initialize the database
        partenairesRepository.saveAndFlush(partenaires);

        int databaseSizeBeforeUpdate = partenairesRepository.findAll().size();

        // Update the partenaires
        Partenaires updatedPartenaires = partenairesRepository.findById(partenaires.getId()).get();
        // Disconnect from session so that the updates on updatedPartenaires are not directly saved in db
        em.detach(updatedPartenaires);
        updatedPartenaires
            .nomPartenaire(UPDATED_NOM_PARTENAIRE)
            .emailPartenaire(UPDATED_EMAIL_PARTENAIRE)
            .telPartenaire(UPDATED_TEL_PARTENAIRE)
            .description(UPDATED_DESCRIPTION);

        restPartenairesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPartenaires.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPartenaires))
            )
            .andExpect(status().isOk());

        // Validate the Partenaires in the database
        List<Partenaires> partenairesList = partenairesRepository.findAll();
        assertThat(partenairesList).hasSize(databaseSizeBeforeUpdate);
        Partenaires testPartenaires = partenairesList.get(partenairesList.size() - 1);
        assertThat(testPartenaires.getNomPartenaire()).isEqualTo(UPDATED_NOM_PARTENAIRE);
        assertThat(testPartenaires.getEmailPartenaire()).isEqualTo(UPDATED_EMAIL_PARTENAIRE);
        assertThat(testPartenaires.getTelPartenaire()).isEqualTo(UPDATED_TEL_PARTENAIRE);
        assertThat(testPartenaires.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingPartenaires() throws Exception {
        int databaseSizeBeforeUpdate = partenairesRepository.findAll().size();
        partenaires.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartenairesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, partenaires.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(partenaires))
            )
            .andExpect(status().isBadRequest());

        // Validate the Partenaires in the database
        List<Partenaires> partenairesList = partenairesRepository.findAll();
        assertThat(partenairesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPartenaires() throws Exception {
        int databaseSizeBeforeUpdate = partenairesRepository.findAll().size();
        partenaires.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartenairesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(partenaires))
            )
            .andExpect(status().isBadRequest());

        // Validate the Partenaires in the database
        List<Partenaires> partenairesList = partenairesRepository.findAll();
        assertThat(partenairesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPartenaires() throws Exception {
        int databaseSizeBeforeUpdate = partenairesRepository.findAll().size();
        partenaires.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartenairesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(partenaires)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Partenaires in the database
        List<Partenaires> partenairesList = partenairesRepository.findAll();
        assertThat(partenairesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePartenairesWithPatch() throws Exception {
        // Initialize the database
        partenairesRepository.saveAndFlush(partenaires);

        int databaseSizeBeforeUpdate = partenairesRepository.findAll().size();

        // Update the partenaires using partial update
        Partenaires partialUpdatedPartenaires = new Partenaires();
        partialUpdatedPartenaires.setId(partenaires.getId());

        partialUpdatedPartenaires.emailPartenaire(UPDATED_EMAIL_PARTENAIRE).telPartenaire(UPDATED_TEL_PARTENAIRE);

        restPartenairesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartenaires.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPartenaires))
            )
            .andExpect(status().isOk());

        // Validate the Partenaires in the database
        List<Partenaires> partenairesList = partenairesRepository.findAll();
        assertThat(partenairesList).hasSize(databaseSizeBeforeUpdate);
        Partenaires testPartenaires = partenairesList.get(partenairesList.size() - 1);
        assertThat(testPartenaires.getNomPartenaire()).isEqualTo(DEFAULT_NOM_PARTENAIRE);
        assertThat(testPartenaires.getEmailPartenaire()).isEqualTo(UPDATED_EMAIL_PARTENAIRE);
        assertThat(testPartenaires.getTelPartenaire()).isEqualTo(UPDATED_TEL_PARTENAIRE);
        assertThat(testPartenaires.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdatePartenairesWithPatch() throws Exception {
        // Initialize the database
        partenairesRepository.saveAndFlush(partenaires);

        int databaseSizeBeforeUpdate = partenairesRepository.findAll().size();

        // Update the partenaires using partial update
        Partenaires partialUpdatedPartenaires = new Partenaires();
        partialUpdatedPartenaires.setId(partenaires.getId());

        partialUpdatedPartenaires
            .nomPartenaire(UPDATED_NOM_PARTENAIRE)
            .emailPartenaire(UPDATED_EMAIL_PARTENAIRE)
            .telPartenaire(UPDATED_TEL_PARTENAIRE)
            .description(UPDATED_DESCRIPTION);

        restPartenairesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPartenaires.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPartenaires))
            )
            .andExpect(status().isOk());

        // Validate the Partenaires in the database
        List<Partenaires> partenairesList = partenairesRepository.findAll();
        assertThat(partenairesList).hasSize(databaseSizeBeforeUpdate);
        Partenaires testPartenaires = partenairesList.get(partenairesList.size() - 1);
        assertThat(testPartenaires.getNomPartenaire()).isEqualTo(UPDATED_NOM_PARTENAIRE);
        assertThat(testPartenaires.getEmailPartenaire()).isEqualTo(UPDATED_EMAIL_PARTENAIRE);
        assertThat(testPartenaires.getTelPartenaire()).isEqualTo(UPDATED_TEL_PARTENAIRE);
        assertThat(testPartenaires.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingPartenaires() throws Exception {
        int databaseSizeBeforeUpdate = partenairesRepository.findAll().size();
        partenaires.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPartenairesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partenaires.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partenaires))
            )
            .andExpect(status().isBadRequest());

        // Validate the Partenaires in the database
        List<Partenaires> partenairesList = partenairesRepository.findAll();
        assertThat(partenairesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPartenaires() throws Exception {
        int databaseSizeBeforeUpdate = partenairesRepository.findAll().size();
        partenaires.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartenairesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partenaires))
            )
            .andExpect(status().isBadRequest());

        // Validate the Partenaires in the database
        List<Partenaires> partenairesList = partenairesRepository.findAll();
        assertThat(partenairesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPartenaires() throws Exception {
        int databaseSizeBeforeUpdate = partenairesRepository.findAll().size();
        partenaires.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPartenairesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(partenaires))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Partenaires in the database
        List<Partenaires> partenairesList = partenairesRepository.findAll();
        assertThat(partenairesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePartenaires() throws Exception {
        // Initialize the database
        partenairesRepository.saveAndFlush(partenaires);

        int databaseSizeBeforeDelete = partenairesRepository.findAll().size();

        // Delete the partenaires
        restPartenairesMockMvc
            .perform(delete(ENTITY_API_URL_ID, partenaires.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Partenaires> partenairesList = partenairesRepository.findAll();
        assertThat(partenairesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
