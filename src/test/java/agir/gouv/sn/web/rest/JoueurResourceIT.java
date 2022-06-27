package agir.gouv.sn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import agir.gouv.sn.IntegrationTest;
import agir.gouv.sn.domain.Joueur;
import agir.gouv.sn.domain.enumeration.Poste;
import agir.gouv.sn.repository.JoueurRepository;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link JoueurResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class JoueurResourceIT {

    private static final String DEFAULT_NOM_JOUEUR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_JOUEUR = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM_JOUEUR = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM_JOUEUR = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_NAIS_JOUEUR = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_NAIS_JOUEUR = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_LIEU_NAIS_JOUEUR = "AAAAAAAAAA";
    private static final String UPDATED_LIEU_NAIS_JOUEUR = "BBBBBBBBBB";

    private static final Poste DEFAULT_POSTE_JOUEUR = Poste.ATTAQUANT;
    private static final Poste UPDATED_POSTE_JOUEUR = Poste.DEFENSEUR;

    private static final byte[] DEFAULT_PHOTO_JOUEUR = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO_JOUEUR = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PHOTO_JOUEUR_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_JOUEUR_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/joueurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private JoueurRepository joueurRepository;

    @Mock
    private JoueurRepository joueurRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJoueurMockMvc;

    private Joueur joueur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Joueur createEntity(EntityManager em) {
        Joueur joueur = new Joueur()
            .nomJoueur(DEFAULT_NOM_JOUEUR)
            .prenomJoueur(DEFAULT_PRENOM_JOUEUR)
            .dateNaisJoueur(DEFAULT_DATE_NAIS_JOUEUR)
            .lieuNaisJoueur(DEFAULT_LIEU_NAIS_JOUEUR)
            .posteJoueur(DEFAULT_POSTE_JOUEUR)
            .photoJoueur(DEFAULT_PHOTO_JOUEUR)
            .photoJoueurContentType(DEFAULT_PHOTO_JOUEUR_CONTENT_TYPE);
        return joueur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Joueur createUpdatedEntity(EntityManager em) {
        Joueur joueur = new Joueur()
            .nomJoueur(UPDATED_NOM_JOUEUR)
            .prenomJoueur(UPDATED_PRENOM_JOUEUR)
            .dateNaisJoueur(UPDATED_DATE_NAIS_JOUEUR)
            .lieuNaisJoueur(UPDATED_LIEU_NAIS_JOUEUR)
            .posteJoueur(UPDATED_POSTE_JOUEUR)
            .photoJoueur(UPDATED_PHOTO_JOUEUR)
            .photoJoueurContentType(UPDATED_PHOTO_JOUEUR_CONTENT_TYPE);
        return joueur;
    }

    @BeforeEach
    public void initTest() {
        joueur = createEntity(em);
    }

    @Test
    @Transactional
    void createJoueur() throws Exception {
        int databaseSizeBeforeCreate = joueurRepository.findAll().size();
        // Create the Joueur
        restJoueurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(joueur)))
            .andExpect(status().isCreated());

        // Validate the Joueur in the database
        List<Joueur> joueurList = joueurRepository.findAll();
        assertThat(joueurList).hasSize(databaseSizeBeforeCreate + 1);
        Joueur testJoueur = joueurList.get(joueurList.size() - 1);
        assertThat(testJoueur.getNomJoueur()).isEqualTo(DEFAULT_NOM_JOUEUR);
        assertThat(testJoueur.getPrenomJoueur()).isEqualTo(DEFAULT_PRENOM_JOUEUR);
        assertThat(testJoueur.getDateNaisJoueur()).isEqualTo(DEFAULT_DATE_NAIS_JOUEUR);
        assertThat(testJoueur.getLieuNaisJoueur()).isEqualTo(DEFAULT_LIEU_NAIS_JOUEUR);
        assertThat(testJoueur.getPosteJoueur()).isEqualTo(DEFAULT_POSTE_JOUEUR);
        assertThat(testJoueur.getPhotoJoueur()).isEqualTo(DEFAULT_PHOTO_JOUEUR);
        assertThat(testJoueur.getPhotoJoueurContentType()).isEqualTo(DEFAULT_PHOTO_JOUEUR_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createJoueurWithExistingId() throws Exception {
        // Create the Joueur with an existing ID
        joueur.setId(1L);

        int databaseSizeBeforeCreate = joueurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restJoueurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(joueur)))
            .andExpect(status().isBadRequest());

        // Validate the Joueur in the database
        List<Joueur> joueurList = joueurRepository.findAll();
        assertThat(joueurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllJoueurs() throws Exception {
        // Initialize the database
        joueurRepository.saveAndFlush(joueur);

        // Get all the joueurList
        restJoueurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(joueur.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomJoueur").value(hasItem(DEFAULT_NOM_JOUEUR)))
            .andExpect(jsonPath("$.[*].prenomJoueur").value(hasItem(DEFAULT_PRENOM_JOUEUR)))
            .andExpect(jsonPath("$.[*].dateNaisJoueur").value(hasItem(DEFAULT_DATE_NAIS_JOUEUR.toString())))
            .andExpect(jsonPath("$.[*].lieuNaisJoueur").value(hasItem(DEFAULT_LIEU_NAIS_JOUEUR)))
            .andExpect(jsonPath("$.[*].posteJoueur").value(hasItem(DEFAULT_POSTE_JOUEUR.toString())))
            .andExpect(jsonPath("$.[*].photoJoueurContentType").value(hasItem(DEFAULT_PHOTO_JOUEUR_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photoJoueur").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO_JOUEUR))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllJoueursWithEagerRelationshipsIsEnabled() throws Exception {
        when(joueurRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restJoueurMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(joueurRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllJoueursWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(joueurRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restJoueurMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(joueurRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getJoueur() throws Exception {
        // Initialize the database
        joueurRepository.saveAndFlush(joueur);

        // Get the joueur
        restJoueurMockMvc
            .perform(get(ENTITY_API_URL_ID, joueur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(joueur.getId().intValue()))
            .andExpect(jsonPath("$.nomJoueur").value(DEFAULT_NOM_JOUEUR))
            .andExpect(jsonPath("$.prenomJoueur").value(DEFAULT_PRENOM_JOUEUR))
            .andExpect(jsonPath("$.dateNaisJoueur").value(DEFAULT_DATE_NAIS_JOUEUR.toString()))
            .andExpect(jsonPath("$.lieuNaisJoueur").value(DEFAULT_LIEU_NAIS_JOUEUR))
            .andExpect(jsonPath("$.posteJoueur").value(DEFAULT_POSTE_JOUEUR.toString()))
            .andExpect(jsonPath("$.photoJoueurContentType").value(DEFAULT_PHOTO_JOUEUR_CONTENT_TYPE))
            .andExpect(jsonPath("$.photoJoueur").value(Base64Utils.encodeToString(DEFAULT_PHOTO_JOUEUR)));
    }

    @Test
    @Transactional
    void getNonExistingJoueur() throws Exception {
        // Get the joueur
        restJoueurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewJoueur() throws Exception {
        // Initialize the database
        joueurRepository.saveAndFlush(joueur);

        int databaseSizeBeforeUpdate = joueurRepository.findAll().size();

        // Update the joueur
        Joueur updatedJoueur = joueurRepository.findById(joueur.getId()).get();
        // Disconnect from session so that the updates on updatedJoueur are not directly saved in db
        em.detach(updatedJoueur);
        updatedJoueur
            .nomJoueur(UPDATED_NOM_JOUEUR)
            .prenomJoueur(UPDATED_PRENOM_JOUEUR)
            .dateNaisJoueur(UPDATED_DATE_NAIS_JOUEUR)
            .lieuNaisJoueur(UPDATED_LIEU_NAIS_JOUEUR)
            .posteJoueur(UPDATED_POSTE_JOUEUR)
            .photoJoueur(UPDATED_PHOTO_JOUEUR)
            .photoJoueurContentType(UPDATED_PHOTO_JOUEUR_CONTENT_TYPE);

        restJoueurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedJoueur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedJoueur))
            )
            .andExpect(status().isOk());

        // Validate the Joueur in the database
        List<Joueur> joueurList = joueurRepository.findAll();
        assertThat(joueurList).hasSize(databaseSizeBeforeUpdate);
        Joueur testJoueur = joueurList.get(joueurList.size() - 1);
        assertThat(testJoueur.getNomJoueur()).isEqualTo(UPDATED_NOM_JOUEUR);
        assertThat(testJoueur.getPrenomJoueur()).isEqualTo(UPDATED_PRENOM_JOUEUR);
        assertThat(testJoueur.getDateNaisJoueur()).isEqualTo(UPDATED_DATE_NAIS_JOUEUR);
        assertThat(testJoueur.getLieuNaisJoueur()).isEqualTo(UPDATED_LIEU_NAIS_JOUEUR);
        assertThat(testJoueur.getPosteJoueur()).isEqualTo(UPDATED_POSTE_JOUEUR);
        assertThat(testJoueur.getPhotoJoueur()).isEqualTo(UPDATED_PHOTO_JOUEUR);
        assertThat(testJoueur.getPhotoJoueurContentType()).isEqualTo(UPDATED_PHOTO_JOUEUR_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingJoueur() throws Exception {
        int databaseSizeBeforeUpdate = joueurRepository.findAll().size();
        joueur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJoueurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, joueur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(joueur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Joueur in the database
        List<Joueur> joueurList = joueurRepository.findAll();
        assertThat(joueurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchJoueur() throws Exception {
        int databaseSizeBeforeUpdate = joueurRepository.findAll().size();
        joueur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJoueurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(joueur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Joueur in the database
        List<Joueur> joueurList = joueurRepository.findAll();
        assertThat(joueurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamJoueur() throws Exception {
        int databaseSizeBeforeUpdate = joueurRepository.findAll().size();
        joueur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJoueurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(joueur)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Joueur in the database
        List<Joueur> joueurList = joueurRepository.findAll();
        assertThat(joueurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateJoueurWithPatch() throws Exception {
        // Initialize the database
        joueurRepository.saveAndFlush(joueur);

        int databaseSizeBeforeUpdate = joueurRepository.findAll().size();

        // Update the joueur using partial update
        Joueur partialUpdatedJoueur = new Joueur();
        partialUpdatedJoueur.setId(joueur.getId());

        partialUpdatedJoueur.nomJoueur(UPDATED_NOM_JOUEUR).prenomJoueur(UPDATED_PRENOM_JOUEUR).lieuNaisJoueur(UPDATED_LIEU_NAIS_JOUEUR);

        restJoueurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJoueur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJoueur))
            )
            .andExpect(status().isOk());

        // Validate the Joueur in the database
        List<Joueur> joueurList = joueurRepository.findAll();
        assertThat(joueurList).hasSize(databaseSizeBeforeUpdate);
        Joueur testJoueur = joueurList.get(joueurList.size() - 1);
        assertThat(testJoueur.getNomJoueur()).isEqualTo(UPDATED_NOM_JOUEUR);
        assertThat(testJoueur.getPrenomJoueur()).isEqualTo(UPDATED_PRENOM_JOUEUR);
        assertThat(testJoueur.getDateNaisJoueur()).isEqualTo(DEFAULT_DATE_NAIS_JOUEUR);
        assertThat(testJoueur.getLieuNaisJoueur()).isEqualTo(UPDATED_LIEU_NAIS_JOUEUR);
        assertThat(testJoueur.getPosteJoueur()).isEqualTo(DEFAULT_POSTE_JOUEUR);
        assertThat(testJoueur.getPhotoJoueur()).isEqualTo(DEFAULT_PHOTO_JOUEUR);
        assertThat(testJoueur.getPhotoJoueurContentType()).isEqualTo(DEFAULT_PHOTO_JOUEUR_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateJoueurWithPatch() throws Exception {
        // Initialize the database
        joueurRepository.saveAndFlush(joueur);

        int databaseSizeBeforeUpdate = joueurRepository.findAll().size();

        // Update the joueur using partial update
        Joueur partialUpdatedJoueur = new Joueur();
        partialUpdatedJoueur.setId(joueur.getId());

        partialUpdatedJoueur
            .nomJoueur(UPDATED_NOM_JOUEUR)
            .prenomJoueur(UPDATED_PRENOM_JOUEUR)
            .dateNaisJoueur(UPDATED_DATE_NAIS_JOUEUR)
            .lieuNaisJoueur(UPDATED_LIEU_NAIS_JOUEUR)
            .posteJoueur(UPDATED_POSTE_JOUEUR)
            .photoJoueur(UPDATED_PHOTO_JOUEUR)
            .photoJoueurContentType(UPDATED_PHOTO_JOUEUR_CONTENT_TYPE);

        restJoueurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJoueur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJoueur))
            )
            .andExpect(status().isOk());

        // Validate the Joueur in the database
        List<Joueur> joueurList = joueurRepository.findAll();
        assertThat(joueurList).hasSize(databaseSizeBeforeUpdate);
        Joueur testJoueur = joueurList.get(joueurList.size() - 1);
        assertThat(testJoueur.getNomJoueur()).isEqualTo(UPDATED_NOM_JOUEUR);
        assertThat(testJoueur.getPrenomJoueur()).isEqualTo(UPDATED_PRENOM_JOUEUR);
        assertThat(testJoueur.getDateNaisJoueur()).isEqualTo(UPDATED_DATE_NAIS_JOUEUR);
        assertThat(testJoueur.getLieuNaisJoueur()).isEqualTo(UPDATED_LIEU_NAIS_JOUEUR);
        assertThat(testJoueur.getPosteJoueur()).isEqualTo(UPDATED_POSTE_JOUEUR);
        assertThat(testJoueur.getPhotoJoueur()).isEqualTo(UPDATED_PHOTO_JOUEUR);
        assertThat(testJoueur.getPhotoJoueurContentType()).isEqualTo(UPDATED_PHOTO_JOUEUR_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingJoueur() throws Exception {
        int databaseSizeBeforeUpdate = joueurRepository.findAll().size();
        joueur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJoueurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, joueur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(joueur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Joueur in the database
        List<Joueur> joueurList = joueurRepository.findAll();
        assertThat(joueurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchJoueur() throws Exception {
        int databaseSizeBeforeUpdate = joueurRepository.findAll().size();
        joueur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJoueurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(joueur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Joueur in the database
        List<Joueur> joueurList = joueurRepository.findAll();
        assertThat(joueurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamJoueur() throws Exception {
        int databaseSizeBeforeUpdate = joueurRepository.findAll().size();
        joueur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJoueurMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(joueur)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Joueur in the database
        List<Joueur> joueurList = joueurRepository.findAll();
        assertThat(joueurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteJoueur() throws Exception {
        // Initialize the database
        joueurRepository.saveAndFlush(joueur);

        int databaseSizeBeforeDelete = joueurRepository.findAll().size();

        // Delete the joueur
        restJoueurMockMvc
            .perform(delete(ENTITY_API_URL_ID, joueur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Joueur> joueurList = joueurRepository.findAll();
        assertThat(joueurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
