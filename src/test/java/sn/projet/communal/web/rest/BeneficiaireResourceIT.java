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
import sn.projet.communal.domain.Beneficiaire;
import sn.projet.communal.domain.enumeration.TypeBeneficiaire;
import sn.projet.communal.domain.enumeration.TypeMoral;
import sn.projet.communal.repository.BeneficiaireRepository;

/**
 * Integration tests for the {@link BeneficiaireResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BeneficiaireResourceIT {

    private static final TypeBeneficiaire DEFAULT_TYPE_BENEFIAIRE = TypeBeneficiaire.PersonneMoral;
    private static final TypeBeneficiaire UPDATED_TYPE_BENEFIAIRE = TypeBeneficiaire.PersonnePhysique;

    private static final TypeMoral DEFAULT_TYPE_PERSO_MORAL = TypeMoral.Groupement;
    private static final TypeMoral UPDATED_TYPE_PERSO_MORAL = TypeMoral.Association;

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_CIN = "AAAAAAAAAA";
    private static final String UPDATED_CIN = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String DEFAULT_TEL_1 = "AAAAAAAAAA";
    private static final String UPDATED_TEL_1 = "BBBBBBBBBB";

    private static final String DEFAULT_AUTRETEL_1 = "AAAAAAAAAA";
    private static final String UPDATED_AUTRETEL_1 = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL_ASSOCIATION = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL_ASSOCIATION = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_PRESIDENT = "AAAAAAAAAA";
    private static final String UPDATED_NOM_PRESIDENT = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/beneficiaires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BeneficiaireRepository beneficiaireRepository;

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
            .typeBenefiaire(DEFAULT_TYPE_BENEFIAIRE)
            .typePersoMoral(DEFAULT_TYPE_PERSO_MORAL)
            .prenom(DEFAULT_PRENOM)
            .nom(DEFAULT_NOM)
            .cin(DEFAULT_CIN)
            .adresse(DEFAULT_ADRESSE)
            .tel1(DEFAULT_TEL_1)
            .autretel1(DEFAULT_AUTRETEL_1)
            .emailAssociation(DEFAULT_EMAIL_ASSOCIATION)
            .nomPresident(DEFAULT_NOM_PRESIDENT)
            .description(DEFAULT_DESCRIPTION);
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
            .typeBenefiaire(UPDATED_TYPE_BENEFIAIRE)
            .typePersoMoral(UPDATED_TYPE_PERSO_MORAL)
            .prenom(UPDATED_PRENOM)
            .nom(UPDATED_NOM)
            .cin(UPDATED_CIN)
            .adresse(UPDATED_ADRESSE)
            .tel1(UPDATED_TEL_1)
            .autretel1(UPDATED_AUTRETEL_1)
            .emailAssociation(UPDATED_EMAIL_ASSOCIATION)
            .nomPresident(UPDATED_NOM_PRESIDENT)
            .description(UPDATED_DESCRIPTION);
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
        assertThat(testBeneficiaire.getTypeBenefiaire()).isEqualTo(DEFAULT_TYPE_BENEFIAIRE);
        assertThat(testBeneficiaire.getTypePersoMoral()).isEqualTo(DEFAULT_TYPE_PERSO_MORAL);
        assertThat(testBeneficiaire.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testBeneficiaire.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testBeneficiaire.getCin()).isEqualTo(DEFAULT_CIN);
        assertThat(testBeneficiaire.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testBeneficiaire.getTel1()).isEqualTo(DEFAULT_TEL_1);
        assertThat(testBeneficiaire.getAutretel1()).isEqualTo(DEFAULT_AUTRETEL_1);
        assertThat(testBeneficiaire.getEmailAssociation()).isEqualTo(DEFAULT_EMAIL_ASSOCIATION);
        assertThat(testBeneficiaire.getNomPresident()).isEqualTo(DEFAULT_NOM_PRESIDENT);
        assertThat(testBeneficiaire.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
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
    void checkTel1IsRequired() throws Exception {
        int databaseSizeBeforeTest = beneficiaireRepository.findAll().size();
        // set the field null
        beneficiaire.setTel1(null);

        // Create the Beneficiaire, which fails.

        restBeneficiaireMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(beneficiaire)))
            .andExpect(status().isBadRequest());

        List<Beneficiaire> beneficiaireList = beneficiaireRepository.findAll();
        assertThat(beneficiaireList).hasSize(databaseSizeBeforeTest);
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
            .andExpect(jsonPath("$.[*].typeBenefiaire").value(hasItem(DEFAULT_TYPE_BENEFIAIRE.toString())))
            .andExpect(jsonPath("$.[*].typePersoMoral").value(hasItem(DEFAULT_TYPE_PERSO_MORAL.toString())))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].cin").value(hasItem(DEFAULT_CIN)))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE)))
            .andExpect(jsonPath("$.[*].tel1").value(hasItem(DEFAULT_TEL_1)))
            .andExpect(jsonPath("$.[*].autretel1").value(hasItem(DEFAULT_AUTRETEL_1)))
            .andExpect(jsonPath("$.[*].emailAssociation").value(hasItem(DEFAULT_EMAIL_ASSOCIATION)))
            .andExpect(jsonPath("$.[*].nomPresident").value(hasItem(DEFAULT_NOM_PRESIDENT)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
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
            .andExpect(jsonPath("$.typeBenefiaire").value(DEFAULT_TYPE_BENEFIAIRE.toString()))
            .andExpect(jsonPath("$.typePersoMoral").value(DEFAULT_TYPE_PERSO_MORAL.toString()))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.cin").value(DEFAULT_CIN))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE))
            .andExpect(jsonPath("$.tel1").value(DEFAULT_TEL_1))
            .andExpect(jsonPath("$.autretel1").value(DEFAULT_AUTRETEL_1))
            .andExpect(jsonPath("$.emailAssociation").value(DEFAULT_EMAIL_ASSOCIATION))
            .andExpect(jsonPath("$.nomPresident").value(DEFAULT_NOM_PRESIDENT))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
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
            .typeBenefiaire(UPDATED_TYPE_BENEFIAIRE)
            .typePersoMoral(UPDATED_TYPE_PERSO_MORAL)
            .prenom(UPDATED_PRENOM)
            .nom(UPDATED_NOM)
            .cin(UPDATED_CIN)
            .adresse(UPDATED_ADRESSE)
            .tel1(UPDATED_TEL_1)
            .autretel1(UPDATED_AUTRETEL_1)
            .emailAssociation(UPDATED_EMAIL_ASSOCIATION)
            .nomPresident(UPDATED_NOM_PRESIDENT)
            .description(UPDATED_DESCRIPTION);

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
        assertThat(testBeneficiaire.getTypeBenefiaire()).isEqualTo(UPDATED_TYPE_BENEFIAIRE);
        assertThat(testBeneficiaire.getTypePersoMoral()).isEqualTo(UPDATED_TYPE_PERSO_MORAL);
        assertThat(testBeneficiaire.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testBeneficiaire.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testBeneficiaire.getCin()).isEqualTo(UPDATED_CIN);
        assertThat(testBeneficiaire.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testBeneficiaire.getTel1()).isEqualTo(UPDATED_TEL_1);
        assertThat(testBeneficiaire.getAutretel1()).isEqualTo(UPDATED_AUTRETEL_1);
        assertThat(testBeneficiaire.getEmailAssociation()).isEqualTo(UPDATED_EMAIL_ASSOCIATION);
        assertThat(testBeneficiaire.getNomPresident()).isEqualTo(UPDATED_NOM_PRESIDENT);
        assertThat(testBeneficiaire.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
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

        partialUpdatedBeneficiaire
            .typeBenefiaire(UPDATED_TYPE_BENEFIAIRE)
            .cin(UPDATED_CIN)
            .tel1(UPDATED_TEL_1)
            .autretel1(UPDATED_AUTRETEL_1)
            .emailAssociation(UPDATED_EMAIL_ASSOCIATION)
            .nomPresident(UPDATED_NOM_PRESIDENT)
            .description(UPDATED_DESCRIPTION);

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
        assertThat(testBeneficiaire.getTypeBenefiaire()).isEqualTo(UPDATED_TYPE_BENEFIAIRE);
        assertThat(testBeneficiaire.getTypePersoMoral()).isEqualTo(DEFAULT_TYPE_PERSO_MORAL);
        assertThat(testBeneficiaire.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testBeneficiaire.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testBeneficiaire.getCin()).isEqualTo(UPDATED_CIN);
        assertThat(testBeneficiaire.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testBeneficiaire.getTel1()).isEqualTo(UPDATED_TEL_1);
        assertThat(testBeneficiaire.getAutretel1()).isEqualTo(UPDATED_AUTRETEL_1);
        assertThat(testBeneficiaire.getEmailAssociation()).isEqualTo(UPDATED_EMAIL_ASSOCIATION);
        assertThat(testBeneficiaire.getNomPresident()).isEqualTo(UPDATED_NOM_PRESIDENT);
        assertThat(testBeneficiaire.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
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
            .typeBenefiaire(UPDATED_TYPE_BENEFIAIRE)
            .typePersoMoral(UPDATED_TYPE_PERSO_MORAL)
            .prenom(UPDATED_PRENOM)
            .nom(UPDATED_NOM)
            .cin(UPDATED_CIN)
            .adresse(UPDATED_ADRESSE)
            .tel1(UPDATED_TEL_1)
            .autretel1(UPDATED_AUTRETEL_1)
            .emailAssociation(UPDATED_EMAIL_ASSOCIATION)
            .nomPresident(UPDATED_NOM_PRESIDENT)
            .description(UPDATED_DESCRIPTION);

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
        assertThat(testBeneficiaire.getTypeBenefiaire()).isEqualTo(UPDATED_TYPE_BENEFIAIRE);
        assertThat(testBeneficiaire.getTypePersoMoral()).isEqualTo(UPDATED_TYPE_PERSO_MORAL);
        assertThat(testBeneficiaire.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testBeneficiaire.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testBeneficiaire.getCin()).isEqualTo(UPDATED_CIN);
        assertThat(testBeneficiaire.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testBeneficiaire.getTel1()).isEqualTo(UPDATED_TEL_1);
        assertThat(testBeneficiaire.getAutretel1()).isEqualTo(UPDATED_AUTRETEL_1);
        assertThat(testBeneficiaire.getEmailAssociation()).isEqualTo(UPDATED_EMAIL_ASSOCIATION);
        assertThat(testBeneficiaire.getNomPresident()).isEqualTo(UPDATED_NOM_PRESIDENT);
        assertThat(testBeneficiaire.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
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
