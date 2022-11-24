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
import org.springframework.util.Base64Utils;
import sn.projet.communal.IntegrationTest;
import sn.projet.communal.domain.Donneur;
import sn.projet.communal.domain.enumeration.TypeDonneur;
import sn.projet.communal.repository.DonneurRepository;

/**
 * Integration tests for the {@link DonneurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DonneurResourceIT {

    private static final TypeDonneur DEFAULT_TYPE_DONNEUR = TypeDonneur.ONG;
    private static final TypeDonneur UPDATED_TYPE_DONNEUR = TypeDonneur.ONG;

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String DEFAULT_TEL_1 = "AAAAAAAAAA";
    private static final String UPDATED_TEL_1 = "BBBBBBBBBB";

    private static final String DEFAULT_VILLE = "AAAAAAAAAA";
    private static final String UPDATED_VILLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/donneurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DonneurRepository donneurRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDonneurMockMvc;

    private Donneur donneur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Donneur createEntity(EntityManager em) {
        Donneur donneur = new Donneur()
            .typeDonneur(DEFAULT_TYPE_DONNEUR)
            .prenom(DEFAULT_PRENOM)
            .nom(DEFAULT_NOM)
            .email(DEFAULT_EMAIL)
            .adresse(DEFAULT_ADRESSE)
            .tel1(DEFAULT_TEL_1)
            .ville(DEFAULT_VILLE)
            .description(DEFAULT_DESCRIPTION);
        return donneur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Donneur createUpdatedEntity(EntityManager em) {
        Donneur donneur = new Donneur()
            .typeDonneur(UPDATED_TYPE_DONNEUR)
            .prenom(UPDATED_PRENOM)
            .nom(UPDATED_NOM)
            .email(UPDATED_EMAIL)
            .adresse(UPDATED_ADRESSE)
            .tel1(UPDATED_TEL_1)
            .ville(UPDATED_VILLE)
            .description(UPDATED_DESCRIPTION);
        return donneur;
    }

    @BeforeEach
    public void initTest() {
        donneur = createEntity(em);
    }

    @Test
    @Transactional
    void createDonneur() throws Exception {
        int databaseSizeBeforeCreate = donneurRepository.findAll().size();
        // Create the Donneur
        restDonneurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(donneur)))
            .andExpect(status().isCreated());

        // Validate the Donneur in the database
        List<Donneur> donneurList = donneurRepository.findAll();
        assertThat(donneurList).hasSize(databaseSizeBeforeCreate + 1);
        Donneur testDonneur = donneurList.get(donneurList.size() - 1);
        assertThat(testDonneur.getTypeDonneur()).isEqualTo(DEFAULT_TYPE_DONNEUR);
        assertThat(testDonneur.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testDonneur.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testDonneur.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testDonneur.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testDonneur.getTel1()).isEqualTo(DEFAULT_TEL_1);
        assertThat(testDonneur.getVille()).isEqualTo(DEFAULT_VILLE);
        assertThat(testDonneur.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createDonneurWithExistingId() throws Exception {
        // Create the Donneur with an existing ID
        donneur.setId(1L);

        int databaseSizeBeforeCreate = donneurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDonneurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(donneur)))
            .andExpect(status().isBadRequest());

        // Validate the Donneur in the database
        List<Donneur> donneurList = donneurRepository.findAll();
        assertThat(donneurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = donneurRepository.findAll().size();
        // set the field null
        donneur.setNom(null);

        // Create the Donneur, which fails.

        restDonneurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(donneur)))
            .andExpect(status().isBadRequest());

        List<Donneur> donneurList = donneurRepository.findAll();
        assertThat(donneurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDonneurs() throws Exception {
        // Initialize the database
        donneurRepository.saveAndFlush(donneur);

        // Get all the donneurList
        restDonneurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(donneur.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeDonneur").value(hasItem(DEFAULT_TYPE_DONNEUR.toString())))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].tel1").value(hasItem(DEFAULT_TEL_1)))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getDonneur() throws Exception {
        // Initialize the database
        donneurRepository.saveAndFlush(donneur);

        // Get the donneur
        restDonneurMockMvc
            .perform(get(ENTITY_API_URL_ID, donneur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(donneur.getId().intValue()))
            .andExpect(jsonPath("$.typeDonneur").value(DEFAULT_TYPE_DONNEUR.toString()))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.tel1").value(DEFAULT_TEL_1))
            .andExpect(jsonPath("$.ville").value(DEFAULT_VILLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDonneur() throws Exception {
        // Get the donneur
        restDonneurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDonneur() throws Exception {
        // Initialize the database
        donneurRepository.saveAndFlush(donneur);

        int databaseSizeBeforeUpdate = donneurRepository.findAll().size();

        // Update the donneur
        Donneur updatedDonneur = donneurRepository.findById(donneur.getId()).get();
        // Disconnect from session so that the updates on updatedDonneur are not directly saved in db
        em.detach(updatedDonneur);
        updatedDonneur
            .typeDonneur(UPDATED_TYPE_DONNEUR)
            .prenom(UPDATED_PRENOM)
            .nom(UPDATED_NOM)
            .email(UPDATED_EMAIL)
            .adresse(UPDATED_ADRESSE)
            .tel1(UPDATED_TEL_1)
            .ville(UPDATED_VILLE)
            .description(UPDATED_DESCRIPTION);

        restDonneurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDonneur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDonneur))
            )
            .andExpect(status().isOk());

        // Validate the Donneur in the database
        List<Donneur> donneurList = donneurRepository.findAll();
        assertThat(donneurList).hasSize(databaseSizeBeforeUpdate);
        Donneur testDonneur = donneurList.get(donneurList.size() - 1);
        assertThat(testDonneur.getTypeDonneur()).isEqualTo(UPDATED_TYPE_DONNEUR);
        assertThat(testDonneur.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testDonneur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testDonneur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testDonneur.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testDonneur.getTel1()).isEqualTo(UPDATED_TEL_1);
        assertThat(testDonneur.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testDonneur.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingDonneur() throws Exception {
        int databaseSizeBeforeUpdate = donneurRepository.findAll().size();
        donneur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDonneurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, donneur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(donneur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Donneur in the database
        List<Donneur> donneurList = donneurRepository.findAll();
        assertThat(donneurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDonneur() throws Exception {
        int databaseSizeBeforeUpdate = donneurRepository.findAll().size();
        donneur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonneurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(donneur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Donneur in the database
        List<Donneur> donneurList = donneurRepository.findAll();
        assertThat(donneurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDonneur() throws Exception {
        int databaseSizeBeforeUpdate = donneurRepository.findAll().size();
        donneur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonneurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(donneur)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Donneur in the database
        List<Donneur> donneurList = donneurRepository.findAll();
        assertThat(donneurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDonneurWithPatch() throws Exception {
        // Initialize the database
        donneurRepository.saveAndFlush(donneur);

        int databaseSizeBeforeUpdate = donneurRepository.findAll().size();

        // Update the donneur using partial update
        Donneur partialUpdatedDonneur = new Donneur();
        partialUpdatedDonneur.setId(donneur.getId());

        partialUpdatedDonneur.typeDonneur(UPDATED_TYPE_DONNEUR).nom(UPDATED_NOM).ville(UPDATED_VILLE).description(UPDATED_DESCRIPTION);

        restDonneurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDonneur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDonneur))
            )
            .andExpect(status().isOk());

        // Validate the Donneur in the database
        List<Donneur> donneurList = donneurRepository.findAll();
        assertThat(donneurList).hasSize(databaseSizeBeforeUpdate);
        Donneur testDonneur = donneurList.get(donneurList.size() - 1);
        assertThat(testDonneur.getTypeDonneur()).isEqualTo(UPDATED_TYPE_DONNEUR);
        assertThat(testDonneur.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testDonneur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testDonneur.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testDonneur.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testDonneur.getTel1()).isEqualTo(DEFAULT_TEL_1);
        assertThat(testDonneur.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testDonneur.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateDonneurWithPatch() throws Exception {
        // Initialize the database
        donneurRepository.saveAndFlush(donneur);

        int databaseSizeBeforeUpdate = donneurRepository.findAll().size();

        // Update the donneur using partial update
        Donneur partialUpdatedDonneur = new Donneur();
        partialUpdatedDonneur.setId(donneur.getId());

        partialUpdatedDonneur
            .typeDonneur(UPDATED_TYPE_DONNEUR)
            .prenom(UPDATED_PRENOM)
            .nom(UPDATED_NOM)
            .email(UPDATED_EMAIL)
            .adresse(UPDATED_ADRESSE)
            .tel1(UPDATED_TEL_1)
            .ville(UPDATED_VILLE)
            .description(UPDATED_DESCRIPTION);

        restDonneurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDonneur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDonneur))
            )
            .andExpect(status().isOk());

        // Validate the Donneur in the database
        List<Donneur> donneurList = donneurRepository.findAll();
        assertThat(donneurList).hasSize(databaseSizeBeforeUpdate);
        Donneur testDonneur = donneurList.get(donneurList.size() - 1);
        assertThat(testDonneur.getTypeDonneur()).isEqualTo(UPDATED_TYPE_DONNEUR);
        assertThat(testDonneur.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testDonneur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testDonneur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testDonneur.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testDonneur.getTel1()).isEqualTo(UPDATED_TEL_1);
        assertThat(testDonneur.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testDonneur.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingDonneur() throws Exception {
        int databaseSizeBeforeUpdate = donneurRepository.findAll().size();
        donneur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDonneurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, donneur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(donneur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Donneur in the database
        List<Donneur> donneurList = donneurRepository.findAll();
        assertThat(donneurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDonneur() throws Exception {
        int databaseSizeBeforeUpdate = donneurRepository.findAll().size();
        donneur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonneurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(donneur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Donneur in the database
        List<Donneur> donneurList = donneurRepository.findAll();
        assertThat(donneurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDonneur() throws Exception {
        int databaseSizeBeforeUpdate = donneurRepository.findAll().size();
        donneur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDonneurMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(donneur)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Donneur in the database
        List<Donneur> donneurList = donneurRepository.findAll();
        assertThat(donneurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDonneur() throws Exception {
        // Initialize the database
        donneurRepository.saveAndFlush(donneur);

        int databaseSizeBeforeDelete = donneurRepository.findAll().size();

        // Delete the donneur
        restDonneurMockMvc
            .perform(delete(ENTITY_API_URL_ID, donneur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Donneur> donneurList = donneurRepository.findAll();
        assertThat(donneurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
