package agir.gouv.sn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import agir.gouv.sn.IntegrationTest;
import agir.gouv.sn.domain.DonSang;
import agir.gouv.sn.repository.DonSangRepository;
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
 * Integration tests for the {@link DonSangResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DonSangResourceIT {

    private static final String DEFAULT_ORGANISATEUR = "AAAAAAAAAA";
    private static final String UPDATED_ORGANISATEUR = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/don-sangs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DonSangRepository donSangRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDonSangMockMvc;

    private DonSang donSang;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DonSang createEntity(EntityManager em) {
        DonSang donSang = new DonSang().organisateur(DEFAULT_ORGANISATEUR).description(DEFAULT_DESCRIPTION);
        return donSang;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DonSang createUpdatedEntity(EntityManager em) {
        DonSang donSang = new DonSang().organisateur(UPDATED_ORGANISATEUR).description(UPDATED_DESCRIPTION);
        return donSang;
    }

    @BeforeEach
    public void initTest() {
        donSang = createEntity(em);
    }

    @Test
    @Transactional
    void createDonSang() throws Exception {
        int databaseSizeBeforeCreate = donSangRepository.findAll().size();
        // Create the DonSang
        restDonSangMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(donSang)))
            .andExpect(status().isCreated());

        // Validate the DonSang in the database
        List<DonSang> donSangList = donSangRepository.findAll();
        assertThat(donSangList).hasSize(databaseSizeBeforeCreate + 1);
        DonSang testDonSang = donSangList.get(donSangList.size() - 1);
        assertThat(testDonSang.getOrganisateur()).isEqualTo(DEFAULT_ORGANISATEUR);
        assertThat(testDonSang.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createDonSangWithExistingId() throws Exception {
        // Create the DonSang with an existing ID
        donSang.setId(1L);

        int databaseSizeBeforeCreate = donSangRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDonSangMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(donSang)))
            .andExpect(status().isBadRequest());

        // Validate the DonSang in the database
        List<DonSang> donSangList = donSangRepository.findAll();
        assertThat(donSangList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDonSangs() throws Exception {
        // Initialize the database
        donSangRepository.saveAndFlush(donSang);

        // Get all the donSangList
        restDonSangMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(donSang.getId().intValue())))
            .andExpect(jsonPath("$.[*].organisateur").value(hasItem(DEFAULT_ORGANISATEUR)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getDonSang() throws Exception {
        // Initialize the database
        donSangRepository.saveAndFlush(donSang);

        // Get the donSang
        restDonSangMockMvc
            .perform(get(ENTITY_API_URL_ID, donSang.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(donSang.getId().intValue()))
            .andExpect(jsonPath("$.organisateur").value(DEFAULT_ORGANISATEUR))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDonSang() throws Exception {
        // Get the donSang
        restDonSangMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDonSang() throws Exception {
        // Initialize the database
        donSangRepository.saveAndFlush(donSang);

        int databaseSizeBeforeUpdate = donSangRepository.findAll().size();

        // Update the donSang
        DonSang updatedDonSang = donSangRepository.findById(donSang.getId()).get();
        // Disconnect from session so that the updates on updatedDonSang are not directly saved in db
        em.detach(updatedDonSang);
        updatedDonSang.organisateur(UPDATED_ORGANISATEUR).description(UPDATED_DESCRIPTION);

        restDonSangMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDonSang.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDonSang))
            )
            .andExpect(status().isOk());

        // Validate the DonSang in the database
        List<DonSang> donSangList = donSangRepository.findAll();
        assertThat(donSangList).hasSize(databaseSizeBeforeUpdate);
        DonSang testDonSang = donSangList.get(donSangList.size() - 1);
        assertThat(testDonSang.getOrganisateur()).isEqualTo(UPDATED_ORGANISATEUR);
        assertThat(testDonSang.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingDonSang() throws Exception {
        int databaseSizeBeforeUpdate = donSangRepository.findAll().size();
        donSang.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDonSangMockMvc
            .perform(
                put(ENTITY_API_URL_ID, donSang.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(donSang))
            )
            .andExpect(status().isBadRequest());

        // Validate the DonSang in the database
        List<DonSang> donSangList = donSangRepository.findAll();
        assertThat(donSangList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDonSang() throws Exception {
        int databaseSizeBeforeUpdate = donSangRepository.findAll().size();
        donSang.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonSangMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(donSang))
            )
            .andExpect(status().isBadRequest());

        // Validate the DonSang in the database
        List<DonSang> donSangList = donSangRepository.findAll();
        assertThat(donSangList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDonSang() throws Exception {
        int databaseSizeBeforeUpdate = donSangRepository.findAll().size();
        donSang.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonSangMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(donSang)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DonSang in the database
        List<DonSang> donSangList = donSangRepository.findAll();
        assertThat(donSangList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDonSangWithPatch() throws Exception {
        // Initialize the database
        donSangRepository.saveAndFlush(donSang);

        int databaseSizeBeforeUpdate = donSangRepository.findAll().size();

        // Update the donSang using partial update
        DonSang partialUpdatedDonSang = new DonSang();
        partialUpdatedDonSang.setId(donSang.getId());

        partialUpdatedDonSang.organisateur(UPDATED_ORGANISATEUR);

        restDonSangMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDonSang.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDonSang))
            )
            .andExpect(status().isOk());

        // Validate the DonSang in the database
        List<DonSang> donSangList = donSangRepository.findAll();
        assertThat(donSangList).hasSize(databaseSizeBeforeUpdate);
        DonSang testDonSang = donSangList.get(donSangList.size() - 1);
        assertThat(testDonSang.getOrganisateur()).isEqualTo(UPDATED_ORGANISATEUR);
        assertThat(testDonSang.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateDonSangWithPatch() throws Exception {
        // Initialize the database
        donSangRepository.saveAndFlush(donSang);

        int databaseSizeBeforeUpdate = donSangRepository.findAll().size();

        // Update the donSang using partial update
        DonSang partialUpdatedDonSang = new DonSang();
        partialUpdatedDonSang.setId(donSang.getId());

        partialUpdatedDonSang.organisateur(UPDATED_ORGANISATEUR).description(UPDATED_DESCRIPTION);

        restDonSangMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDonSang.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDonSang))
            )
            .andExpect(status().isOk());

        // Validate the DonSang in the database
        List<DonSang> donSangList = donSangRepository.findAll();
        assertThat(donSangList).hasSize(databaseSizeBeforeUpdate);
        DonSang testDonSang = donSangList.get(donSangList.size() - 1);
        assertThat(testDonSang.getOrganisateur()).isEqualTo(UPDATED_ORGANISATEUR);
        assertThat(testDonSang.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingDonSang() throws Exception {
        int databaseSizeBeforeUpdate = donSangRepository.findAll().size();
        donSang.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDonSangMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, donSang.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(donSang))
            )
            .andExpect(status().isBadRequest());

        // Validate the DonSang in the database
        List<DonSang> donSangList = donSangRepository.findAll();
        assertThat(donSangList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDonSang() throws Exception {
        int databaseSizeBeforeUpdate = donSangRepository.findAll().size();
        donSang.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonSangMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(donSang))
            )
            .andExpect(status().isBadRequest());

        // Validate the DonSang in the database
        List<DonSang> donSangList = donSangRepository.findAll();
        assertThat(donSangList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDonSang() throws Exception {
        int databaseSizeBeforeUpdate = donSangRepository.findAll().size();
        donSang.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonSangMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(donSang)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DonSang in the database
        List<DonSang> donSangList = donSangRepository.findAll();
        assertThat(donSangList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDonSang() throws Exception {
        // Initialize the database
        donSangRepository.saveAndFlush(donSang);

        int databaseSizeBeforeDelete = donSangRepository.findAll().size();

        // Delete the donSang
        restDonSangMockMvc
            .perform(delete(ENTITY_API_URL_ID, donSang.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DonSang> donSangList = donSangRepository.findAll();
        assertThat(donSangList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
