package sn.projet.communal.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import sn.projet.communal.IntegrationTest;
import sn.projet.communal.domain.Vainqueur;
import sn.projet.communal.repository.VainqueurRepository;

/**
 * Integration tests for the {@link VainqueurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VainqueurResourceIT {

    private static final Double DEFAULT_PRIX = 1D;
    private static final Double UPDATED_PRIX = 2D;

    private static final String ENTITY_API_URL = "/api/vainqueurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VainqueurRepository vainqueurRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVainqueurMockMvc;

    private Vainqueur vainqueur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vainqueur createEntity(EntityManager em) {
        Vainqueur vainqueur = new Vainqueur().prix(DEFAULT_PRIX);
        return vainqueur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vainqueur createUpdatedEntity(EntityManager em) {
        Vainqueur vainqueur = new Vainqueur().prix(UPDATED_PRIX);
        return vainqueur;
    }

    @BeforeEach
    public void initTest() {
        vainqueur = createEntity(em);
    }

    @Test
    @Transactional
    void createVainqueur() throws Exception {
        int databaseSizeBeforeCreate = vainqueurRepository.findAll().size();
        // Create the Vainqueur
        restVainqueurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vainqueur)))
            .andExpect(status().isCreated());

        // Validate the Vainqueur in the database
        List<Vainqueur> vainqueurList = vainqueurRepository.findAll();
        assertThat(vainqueurList).hasSize(databaseSizeBeforeCreate + 1);
        Vainqueur testVainqueur = vainqueurList.get(vainqueurList.size() - 1);
        assertThat(testVainqueur.getPrix()).isEqualTo(DEFAULT_PRIX);
    }

    @Test
    @Transactional
    void createVainqueurWithExistingId() throws Exception {
        // Create the Vainqueur with an existing ID
        vainqueur.setId(1L);

        int databaseSizeBeforeCreate = vainqueurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVainqueurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vainqueur)))
            .andExpect(status().isBadRequest());

        // Validate the Vainqueur in the database
        List<Vainqueur> vainqueurList = vainqueurRepository.findAll();
        assertThat(vainqueurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVainqueurs() throws Exception {
        // Initialize the database
        vainqueurRepository.saveAndFlush(vainqueur);

        // Get all the vainqueurList
        restVainqueurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vainqueur.getId().intValue())))
            .andExpect(jsonPath("$.[*].prix").value(hasItem(DEFAULT_PRIX.doubleValue())));
    }

    @Test
    @Transactional
    void getVainqueur() throws Exception {
        // Initialize the database
        vainqueurRepository.saveAndFlush(vainqueur);

        // Get the vainqueur
        restVainqueurMockMvc
            .perform(get(ENTITY_API_URL_ID, vainqueur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vainqueur.getId().intValue()))
            .andExpect(jsonPath("$.prix").value(DEFAULT_PRIX.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingVainqueur() throws Exception {
        // Get the vainqueur
        restVainqueurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVainqueur() throws Exception {
        // Initialize the database
        vainqueurRepository.saveAndFlush(vainqueur);

        int databaseSizeBeforeUpdate = vainqueurRepository.findAll().size();

        // Update the vainqueur
        Vainqueur updatedVainqueur = vainqueurRepository.findById(vainqueur.getId()).get();
        // Disconnect from session so that the updates on updatedVainqueur are not directly saved in db
        em.detach(updatedVainqueur);
        updatedVainqueur.prix(UPDATED_PRIX);

        restVainqueurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVainqueur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVainqueur))
            )
            .andExpect(status().isOk());

        // Validate the Vainqueur in the database
        List<Vainqueur> vainqueurList = vainqueurRepository.findAll();
        assertThat(vainqueurList).hasSize(databaseSizeBeforeUpdate);
        Vainqueur testVainqueur = vainqueurList.get(vainqueurList.size() - 1);
        assertThat(testVainqueur.getPrix()).isEqualTo(UPDATED_PRIX);
    }

    @Test
    @Transactional
    void putNonExistingVainqueur() throws Exception {
        int databaseSizeBeforeUpdate = vainqueurRepository.findAll().size();
        vainqueur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVainqueurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vainqueur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vainqueur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vainqueur in the database
        List<Vainqueur> vainqueurList = vainqueurRepository.findAll();
        assertThat(vainqueurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVainqueur() throws Exception {
        int databaseSizeBeforeUpdate = vainqueurRepository.findAll().size();
        vainqueur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVainqueurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vainqueur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vainqueur in the database
        List<Vainqueur> vainqueurList = vainqueurRepository.findAll();
        assertThat(vainqueurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVainqueur() throws Exception {
        int databaseSizeBeforeUpdate = vainqueurRepository.findAll().size();
        vainqueur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVainqueurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vainqueur)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vainqueur in the database
        List<Vainqueur> vainqueurList = vainqueurRepository.findAll();
        assertThat(vainqueurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVainqueurWithPatch() throws Exception {
        // Initialize the database
        vainqueurRepository.saveAndFlush(vainqueur);

        int databaseSizeBeforeUpdate = vainqueurRepository.findAll().size();

        // Update the vainqueur using partial update
        Vainqueur partialUpdatedVainqueur = new Vainqueur();
        partialUpdatedVainqueur.setId(vainqueur.getId());

        restVainqueurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVainqueur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVainqueur))
            )
            .andExpect(status().isOk());

        // Validate the Vainqueur in the database
        List<Vainqueur> vainqueurList = vainqueurRepository.findAll();
        assertThat(vainqueurList).hasSize(databaseSizeBeforeUpdate);
        Vainqueur testVainqueur = vainqueurList.get(vainqueurList.size() - 1);
        assertThat(testVainqueur.getPrix()).isEqualTo(DEFAULT_PRIX);
    }

    @Test
    @Transactional
    void fullUpdateVainqueurWithPatch() throws Exception {
        // Initialize the database
        vainqueurRepository.saveAndFlush(vainqueur);

        int databaseSizeBeforeUpdate = vainqueurRepository.findAll().size();

        // Update the vainqueur using partial update
        Vainqueur partialUpdatedVainqueur = new Vainqueur();
        partialUpdatedVainqueur.setId(vainqueur.getId());

        partialUpdatedVainqueur.prix(UPDATED_PRIX);

        restVainqueurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVainqueur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVainqueur))
            )
            .andExpect(status().isOk());

        // Validate the Vainqueur in the database
        List<Vainqueur> vainqueurList = vainqueurRepository.findAll();
        assertThat(vainqueurList).hasSize(databaseSizeBeforeUpdate);
        Vainqueur testVainqueur = vainqueurList.get(vainqueurList.size() - 1);
        assertThat(testVainqueur.getPrix()).isEqualTo(UPDATED_PRIX);
    }

    @Test
    @Transactional
    void patchNonExistingVainqueur() throws Exception {
        int databaseSizeBeforeUpdate = vainqueurRepository.findAll().size();
        vainqueur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVainqueurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vainqueur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vainqueur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vainqueur in the database
        List<Vainqueur> vainqueurList = vainqueurRepository.findAll();
        assertThat(vainqueurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVainqueur() throws Exception {
        int databaseSizeBeforeUpdate = vainqueurRepository.findAll().size();
        vainqueur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVainqueurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vainqueur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vainqueur in the database
        List<Vainqueur> vainqueurList = vainqueurRepository.findAll();
        assertThat(vainqueurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVainqueur() throws Exception {
        int databaseSizeBeforeUpdate = vainqueurRepository.findAll().size();
        vainqueur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVainqueurMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vainqueur))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vainqueur in the database
        List<Vainqueur> vainqueurList = vainqueurRepository.findAll();
        assertThat(vainqueurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVainqueur() throws Exception {
        // Initialize the database
        vainqueurRepository.saveAndFlush(vainqueur);

        int databaseSizeBeforeDelete = vainqueurRepository.findAll().size();

        // Delete the vainqueur
        restVainqueurMockMvc
            .perform(delete(ENTITY_API_URL_ID, vainqueur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vainqueur> vainqueurList = vainqueurRepository.findAll();
        assertThat(vainqueurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
