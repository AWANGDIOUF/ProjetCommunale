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
import sn.projet.communal.domain.Don;
import sn.projet.communal.domain.enumeration.TypeDon;
import sn.projet.communal.repository.DonRepository;

/**
 * Integration tests for the {@link DonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DonResourceIT {

    private static final TypeDon DEFAULT_TYPE_DON = TypeDon.Ravitament;
    private static final TypeDon UPDATED_TYPE_DON = TypeDon.Fourniture;

    private static final Long DEFAULT_MONTANT = 1L;
    private static final Long UPDATED_MONTANT = 2L;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/dons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DonRepository donRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDonMockMvc;

    private Don don;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Don createEntity(EntityManager em) {
        Don don = new Don().typeDon(DEFAULT_TYPE_DON).montant(DEFAULT_MONTANT).description(DEFAULT_DESCRIPTION);
        return don;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Don createUpdatedEntity(EntityManager em) {
        Don don = new Don().typeDon(UPDATED_TYPE_DON).montant(UPDATED_MONTANT).description(UPDATED_DESCRIPTION);
        return don;
    }

    @BeforeEach
    public void initTest() {
        don = createEntity(em);
    }

    @Test
    @Transactional
    void createDon() throws Exception {
        int databaseSizeBeforeCreate = donRepository.findAll().size();
        // Create the Don
        restDonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(don)))
            .andExpect(status().isCreated());

        // Validate the Don in the database
        List<Don> donList = donRepository.findAll();
        assertThat(donList).hasSize(databaseSizeBeforeCreate + 1);
        Don testDon = donList.get(donList.size() - 1);
        assertThat(testDon.getTypeDon()).isEqualTo(DEFAULT_TYPE_DON);
        assertThat(testDon.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testDon.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createDonWithExistingId() throws Exception {
        // Create the Don with an existing ID
        don.setId(1L);

        int databaseSizeBeforeCreate = donRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(don)))
            .andExpect(status().isBadRequest());

        // Validate the Don in the database
        List<Don> donList = donRepository.findAll();
        assertThat(donList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDons() throws Exception {
        // Initialize the database
        donRepository.saveAndFlush(don);

        // Get all the donList
        restDonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(don.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeDon").value(hasItem(DEFAULT_TYPE_DON.toString())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getDon() throws Exception {
        // Initialize the database
        donRepository.saveAndFlush(don);

        // Get the don
        restDonMockMvc
            .perform(get(ENTITY_API_URL_ID, don.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(don.getId().intValue()))
            .andExpect(jsonPath("$.typeDon").value(DEFAULT_TYPE_DON.toString()))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingDon() throws Exception {
        // Get the don
        restDonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDon() throws Exception {
        // Initialize the database
        donRepository.saveAndFlush(don);

        int databaseSizeBeforeUpdate = donRepository.findAll().size();

        // Update the don
        Don updatedDon = donRepository.findById(don.getId()).get();
        // Disconnect from session so that the updates on updatedDon are not directly saved in db
        em.detach(updatedDon);
        updatedDon.typeDon(UPDATED_TYPE_DON).montant(UPDATED_MONTANT).description(UPDATED_DESCRIPTION);

        restDonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDon.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDon))
            )
            .andExpect(status().isOk());

        // Validate the Don in the database
        List<Don> donList = donRepository.findAll();
        assertThat(donList).hasSize(databaseSizeBeforeUpdate);
        Don testDon = donList.get(donList.size() - 1);
        assertThat(testDon.getTypeDon()).isEqualTo(UPDATED_TYPE_DON);
        assertThat(testDon.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testDon.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingDon() throws Exception {
        int databaseSizeBeforeUpdate = donRepository.findAll().size();
        don.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, don.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(don))
            )
            .andExpect(status().isBadRequest());

        // Validate the Don in the database
        List<Don> donList = donRepository.findAll();
        assertThat(donList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDon() throws Exception {
        int databaseSizeBeforeUpdate = donRepository.findAll().size();
        don.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(don))
            )
            .andExpect(status().isBadRequest());

        // Validate the Don in the database
        List<Don> donList = donRepository.findAll();
        assertThat(donList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDon() throws Exception {
        int databaseSizeBeforeUpdate = donRepository.findAll().size();
        don.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(don)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Don in the database
        List<Don> donList = donRepository.findAll();
        assertThat(donList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDonWithPatch() throws Exception {
        // Initialize the database
        donRepository.saveAndFlush(don);

        int databaseSizeBeforeUpdate = donRepository.findAll().size();

        // Update the don using partial update
        Don partialUpdatedDon = new Don();
        partialUpdatedDon.setId(don.getId());

        partialUpdatedDon.typeDon(UPDATED_TYPE_DON).montant(UPDATED_MONTANT).description(UPDATED_DESCRIPTION);

        restDonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDon.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDon))
            )
            .andExpect(status().isOk());

        // Validate the Don in the database
        List<Don> donList = donRepository.findAll();
        assertThat(donList).hasSize(databaseSizeBeforeUpdate);
        Don testDon = donList.get(donList.size() - 1);
        assertThat(testDon.getTypeDon()).isEqualTo(UPDATED_TYPE_DON);
        assertThat(testDon.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testDon.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateDonWithPatch() throws Exception {
        // Initialize the database
        donRepository.saveAndFlush(don);

        int databaseSizeBeforeUpdate = donRepository.findAll().size();

        // Update the don using partial update
        Don partialUpdatedDon = new Don();
        partialUpdatedDon.setId(don.getId());

        partialUpdatedDon.typeDon(UPDATED_TYPE_DON).montant(UPDATED_MONTANT).description(UPDATED_DESCRIPTION);

        restDonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDon.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDon))
            )
            .andExpect(status().isOk());

        // Validate the Don in the database
        List<Don> donList = donRepository.findAll();
        assertThat(donList).hasSize(databaseSizeBeforeUpdate);
        Don testDon = donList.get(donList.size() - 1);
        assertThat(testDon.getTypeDon()).isEqualTo(UPDATED_TYPE_DON);
        assertThat(testDon.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testDon.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingDon() throws Exception {
        int databaseSizeBeforeUpdate = donRepository.findAll().size();
        don.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, don.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(don))
            )
            .andExpect(status().isBadRequest());

        // Validate the Don in the database
        List<Don> donList = donRepository.findAll();
        assertThat(donList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDon() throws Exception {
        int databaseSizeBeforeUpdate = donRepository.findAll().size();
        don.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(don))
            )
            .andExpect(status().isBadRequest());

        // Validate the Don in the database
        List<Don> donList = donRepository.findAll();
        assertThat(donList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDon() throws Exception {
        int databaseSizeBeforeUpdate = donRepository.findAll().size();
        don.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(don)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Don in the database
        List<Don> donList = donRepository.findAll();
        assertThat(donList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDon() throws Exception {
        // Initialize the database
        donRepository.saveAndFlush(don);

        int databaseSizeBeforeDelete = donRepository.findAll().size();

        // Delete the don
        restDonMockMvc.perform(delete(ENTITY_API_URL_ID, don.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Don> donList = donRepository.findAll();
        assertThat(donList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
