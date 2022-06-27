package agir.gouv.sn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import agir.gouv.sn.IntegrationTest;
import agir.gouv.sn.domain.ActivitePolitique;
import agir.gouv.sn.repository.ActivitePolitiqueRepository;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ActivitePolitiqueResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ActivitePolitiqueResourceIT {

    private static final String DEFAULT_TITRE_ACTIVITE = "AAAAAAAAAA";
    private static final String UPDATED_TITRE_ACTIVITE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION_ACTIVITE = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION_ACTIVITE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DEBUT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DEBUT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_FIN = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FIN = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/activite-politiques";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ActivitePolitiqueRepository activitePolitiqueRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restActivitePolitiqueMockMvc;

    private ActivitePolitique activitePolitique;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ActivitePolitique createEntity(EntityManager em) {
        ActivitePolitique activitePolitique = new ActivitePolitique()
            .titreActivite(DEFAULT_TITRE_ACTIVITE)
            .descriptionActivite(DEFAULT_DESCRIPTION_ACTIVITE)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN);
        return activitePolitique;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ActivitePolitique createUpdatedEntity(EntityManager em) {
        ActivitePolitique activitePolitique = new ActivitePolitique()
            .titreActivite(UPDATED_TITRE_ACTIVITE)
            .descriptionActivite(UPDATED_DESCRIPTION_ACTIVITE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN);
        return activitePolitique;
    }

    @BeforeEach
    public void initTest() {
        activitePolitique = createEntity(em);
    }

    @Test
    @Transactional
    void createActivitePolitique() throws Exception {
        int databaseSizeBeforeCreate = activitePolitiqueRepository.findAll().size();
        // Create the ActivitePolitique
        restActivitePolitiqueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(activitePolitique))
            )
            .andExpect(status().isCreated());

        // Validate the ActivitePolitique in the database
        List<ActivitePolitique> activitePolitiqueList = activitePolitiqueRepository.findAll();
        assertThat(activitePolitiqueList).hasSize(databaseSizeBeforeCreate + 1);
        ActivitePolitique testActivitePolitique = activitePolitiqueList.get(activitePolitiqueList.size() - 1);
        assertThat(testActivitePolitique.getTitreActivite()).isEqualTo(DEFAULT_TITRE_ACTIVITE);
        assertThat(testActivitePolitique.getDescriptionActivite()).isEqualTo(DEFAULT_DESCRIPTION_ACTIVITE);
        assertThat(testActivitePolitique.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testActivitePolitique.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
    }

    @Test
    @Transactional
    void createActivitePolitiqueWithExistingId() throws Exception {
        // Create the ActivitePolitique with an existing ID
        activitePolitique.setId(1L);

        int databaseSizeBeforeCreate = activitePolitiqueRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restActivitePolitiqueMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(activitePolitique))
            )
            .andExpect(status().isBadRequest());

        // Validate the ActivitePolitique in the database
        List<ActivitePolitique> activitePolitiqueList = activitePolitiqueRepository.findAll();
        assertThat(activitePolitiqueList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllActivitePolitiques() throws Exception {
        // Initialize the database
        activitePolitiqueRepository.saveAndFlush(activitePolitique);

        // Get all the activitePolitiqueList
        restActivitePolitiqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(activitePolitique.getId().intValue())))
            .andExpect(jsonPath("$.[*].titreActivite").value(hasItem(DEFAULT_TITRE_ACTIVITE)))
            .andExpect(jsonPath("$.[*].descriptionActivite").value(hasItem(DEFAULT_DESCRIPTION_ACTIVITE.toString())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())));
    }

    @Test
    @Transactional
    void getActivitePolitique() throws Exception {
        // Initialize the database
        activitePolitiqueRepository.saveAndFlush(activitePolitique);

        // Get the activitePolitique
        restActivitePolitiqueMockMvc
            .perform(get(ENTITY_API_URL_ID, activitePolitique.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(activitePolitique.getId().intValue()))
            .andExpect(jsonPath("$.titreActivite").value(DEFAULT_TITRE_ACTIVITE))
            .andExpect(jsonPath("$.descriptionActivite").value(DEFAULT_DESCRIPTION_ACTIVITE.toString()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()));
    }

    @Test
    @Transactional
    void getNonExistingActivitePolitique() throws Exception {
        // Get the activitePolitique
        restActivitePolitiqueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewActivitePolitique() throws Exception {
        // Initialize the database
        activitePolitiqueRepository.saveAndFlush(activitePolitique);

        int databaseSizeBeforeUpdate = activitePolitiqueRepository.findAll().size();

        // Update the activitePolitique
        ActivitePolitique updatedActivitePolitique = activitePolitiqueRepository.findById(activitePolitique.getId()).get();
        // Disconnect from session so that the updates on updatedActivitePolitique are not directly saved in db
        em.detach(updatedActivitePolitique);
        updatedActivitePolitique
            .titreActivite(UPDATED_TITRE_ACTIVITE)
            .descriptionActivite(UPDATED_DESCRIPTION_ACTIVITE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN);

        restActivitePolitiqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedActivitePolitique.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedActivitePolitique))
            )
            .andExpect(status().isOk());

        // Validate the ActivitePolitique in the database
        List<ActivitePolitique> activitePolitiqueList = activitePolitiqueRepository.findAll();
        assertThat(activitePolitiqueList).hasSize(databaseSizeBeforeUpdate);
        ActivitePolitique testActivitePolitique = activitePolitiqueList.get(activitePolitiqueList.size() - 1);
        assertThat(testActivitePolitique.getTitreActivite()).isEqualTo(UPDATED_TITRE_ACTIVITE);
        assertThat(testActivitePolitique.getDescriptionActivite()).isEqualTo(UPDATED_DESCRIPTION_ACTIVITE);
        assertThat(testActivitePolitique.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testActivitePolitique.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void putNonExistingActivitePolitique() throws Exception {
        int databaseSizeBeforeUpdate = activitePolitiqueRepository.findAll().size();
        activitePolitique.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActivitePolitiqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, activitePolitique.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(activitePolitique))
            )
            .andExpect(status().isBadRequest());

        // Validate the ActivitePolitique in the database
        List<ActivitePolitique> activitePolitiqueList = activitePolitiqueRepository.findAll();
        assertThat(activitePolitiqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchActivitePolitique() throws Exception {
        int databaseSizeBeforeUpdate = activitePolitiqueRepository.findAll().size();
        activitePolitique.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivitePolitiqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(activitePolitique))
            )
            .andExpect(status().isBadRequest());

        // Validate the ActivitePolitique in the database
        List<ActivitePolitique> activitePolitiqueList = activitePolitiqueRepository.findAll();
        assertThat(activitePolitiqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamActivitePolitique() throws Exception {
        int databaseSizeBeforeUpdate = activitePolitiqueRepository.findAll().size();
        activitePolitique.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivitePolitiqueMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(activitePolitique))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ActivitePolitique in the database
        List<ActivitePolitique> activitePolitiqueList = activitePolitiqueRepository.findAll();
        assertThat(activitePolitiqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateActivitePolitiqueWithPatch() throws Exception {
        // Initialize the database
        activitePolitiqueRepository.saveAndFlush(activitePolitique);

        int databaseSizeBeforeUpdate = activitePolitiqueRepository.findAll().size();

        // Update the activitePolitique using partial update
        ActivitePolitique partialUpdatedActivitePolitique = new ActivitePolitique();
        partialUpdatedActivitePolitique.setId(activitePolitique.getId());

        partialUpdatedActivitePolitique.descriptionActivite(UPDATED_DESCRIPTION_ACTIVITE);

        restActivitePolitiqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActivitePolitique.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedActivitePolitique))
            )
            .andExpect(status().isOk());

        // Validate the ActivitePolitique in the database
        List<ActivitePolitique> activitePolitiqueList = activitePolitiqueRepository.findAll();
        assertThat(activitePolitiqueList).hasSize(databaseSizeBeforeUpdate);
        ActivitePolitique testActivitePolitique = activitePolitiqueList.get(activitePolitiqueList.size() - 1);
        assertThat(testActivitePolitique.getTitreActivite()).isEqualTo(DEFAULT_TITRE_ACTIVITE);
        assertThat(testActivitePolitique.getDescriptionActivite()).isEqualTo(UPDATED_DESCRIPTION_ACTIVITE);
        assertThat(testActivitePolitique.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testActivitePolitique.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
    }

    @Test
    @Transactional
    void fullUpdateActivitePolitiqueWithPatch() throws Exception {
        // Initialize the database
        activitePolitiqueRepository.saveAndFlush(activitePolitique);

        int databaseSizeBeforeUpdate = activitePolitiqueRepository.findAll().size();

        // Update the activitePolitique using partial update
        ActivitePolitique partialUpdatedActivitePolitique = new ActivitePolitique();
        partialUpdatedActivitePolitique.setId(activitePolitique.getId());

        partialUpdatedActivitePolitique
            .titreActivite(UPDATED_TITRE_ACTIVITE)
            .descriptionActivite(UPDATED_DESCRIPTION_ACTIVITE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN);

        restActivitePolitiqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActivitePolitique.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedActivitePolitique))
            )
            .andExpect(status().isOk());

        // Validate the ActivitePolitique in the database
        List<ActivitePolitique> activitePolitiqueList = activitePolitiqueRepository.findAll();
        assertThat(activitePolitiqueList).hasSize(databaseSizeBeforeUpdate);
        ActivitePolitique testActivitePolitique = activitePolitiqueList.get(activitePolitiqueList.size() - 1);
        assertThat(testActivitePolitique.getTitreActivite()).isEqualTo(UPDATED_TITRE_ACTIVITE);
        assertThat(testActivitePolitique.getDescriptionActivite()).isEqualTo(UPDATED_DESCRIPTION_ACTIVITE);
        assertThat(testActivitePolitique.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testActivitePolitique.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    void patchNonExistingActivitePolitique() throws Exception {
        int databaseSizeBeforeUpdate = activitePolitiqueRepository.findAll().size();
        activitePolitique.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActivitePolitiqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, activitePolitique.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(activitePolitique))
            )
            .andExpect(status().isBadRequest());

        // Validate the ActivitePolitique in the database
        List<ActivitePolitique> activitePolitiqueList = activitePolitiqueRepository.findAll();
        assertThat(activitePolitiqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchActivitePolitique() throws Exception {
        int databaseSizeBeforeUpdate = activitePolitiqueRepository.findAll().size();
        activitePolitique.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivitePolitiqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(activitePolitique))
            )
            .andExpect(status().isBadRequest());

        // Validate the ActivitePolitique in the database
        List<ActivitePolitique> activitePolitiqueList = activitePolitiqueRepository.findAll();
        assertThat(activitePolitiqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamActivitePolitique() throws Exception {
        int databaseSizeBeforeUpdate = activitePolitiqueRepository.findAll().size();
        activitePolitique.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActivitePolitiqueMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(activitePolitique))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ActivitePolitique in the database
        List<ActivitePolitique> activitePolitiqueList = activitePolitiqueRepository.findAll();
        assertThat(activitePolitiqueList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteActivitePolitique() throws Exception {
        // Initialize the database
        activitePolitiqueRepository.saveAndFlush(activitePolitique);

        int databaseSizeBeforeDelete = activitePolitiqueRepository.findAll().size();

        // Delete the activitePolitique
        restActivitePolitiqueMockMvc
            .perform(delete(ENTITY_API_URL_ID, activitePolitique.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ActivitePolitique> activitePolitiqueList = activitePolitiqueRepository.findAll();
        assertThat(activitePolitiqueList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
