package agir.gouv.sn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import agir.gouv.sn.IntegrationTest;
import agir.gouv.sn.domain.Artiste;
import agir.gouv.sn.domain.enumeration.Domaine;
import agir.gouv.sn.repository.ArtisteRepository;
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

/**
 * Integration tests for the {@link ArtisteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ArtisteResourceIT {

    private static final String DEFAULT_NOM_ARTISTE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_ARTISTE = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM_ARTISTE = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM_ARTISTE = "BBBBBBBBBB";

    private static final Domaine DEFAULT_DOMAINE = Domaine.Chanteurs;
    private static final Domaine UPDATED_DOMAINE = Domaine.Danseurs;

    private static final String DEFAULT_AUTRE_DOMAINE = "AAAAAAAAAA";
    private static final String UPDATED_AUTRE_DOMAINE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/artistes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ArtisteRepository artisteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArtisteMockMvc;

    private Artiste artiste;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Artiste createEntity(EntityManager em) {
        Artiste artiste = new Artiste()
            .nomArtiste(DEFAULT_NOM_ARTISTE)
            .prenomArtiste(DEFAULT_PRENOM_ARTISTE)
            .domaine(DEFAULT_DOMAINE)
            .autreDomaine(DEFAULT_AUTRE_DOMAINE);
        return artiste;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Artiste createUpdatedEntity(EntityManager em) {
        Artiste artiste = new Artiste()
            .nomArtiste(UPDATED_NOM_ARTISTE)
            .prenomArtiste(UPDATED_PRENOM_ARTISTE)
            .domaine(UPDATED_DOMAINE)
            .autreDomaine(UPDATED_AUTRE_DOMAINE);
        return artiste;
    }

    @BeforeEach
    public void initTest() {
        artiste = createEntity(em);
    }

    @Test
    @Transactional
    void createArtiste() throws Exception {
        int databaseSizeBeforeCreate = artisteRepository.findAll().size();
        // Create the Artiste
        restArtisteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(artiste)))
            .andExpect(status().isCreated());

        // Validate the Artiste in the database
        List<Artiste> artisteList = artisteRepository.findAll();
        assertThat(artisteList).hasSize(databaseSizeBeforeCreate + 1);
        Artiste testArtiste = artisteList.get(artisteList.size() - 1);
        assertThat(testArtiste.getNomArtiste()).isEqualTo(DEFAULT_NOM_ARTISTE);
        assertThat(testArtiste.getPrenomArtiste()).isEqualTo(DEFAULT_PRENOM_ARTISTE);
        assertThat(testArtiste.getDomaine()).isEqualTo(DEFAULT_DOMAINE);
        assertThat(testArtiste.getAutreDomaine()).isEqualTo(DEFAULT_AUTRE_DOMAINE);
    }

    @Test
    @Transactional
    void createArtisteWithExistingId() throws Exception {
        // Create the Artiste with an existing ID
        artiste.setId(1L);

        int databaseSizeBeforeCreate = artisteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArtisteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(artiste)))
            .andExpect(status().isBadRequest());

        // Validate the Artiste in the database
        List<Artiste> artisteList = artisteRepository.findAll();
        assertThat(artisteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllArtistes() throws Exception {
        // Initialize the database
        artisteRepository.saveAndFlush(artiste);

        // Get all the artisteList
        restArtisteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(artiste.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomArtiste").value(hasItem(DEFAULT_NOM_ARTISTE)))
            .andExpect(jsonPath("$.[*].prenomArtiste").value(hasItem(DEFAULT_PRENOM_ARTISTE)))
            .andExpect(jsonPath("$.[*].domaine").value(hasItem(DEFAULT_DOMAINE.toString())))
            .andExpect(jsonPath("$.[*].autreDomaine").value(hasItem(DEFAULT_AUTRE_DOMAINE)));
    }

    @Test
    @Transactional
    void getArtiste() throws Exception {
        // Initialize the database
        artisteRepository.saveAndFlush(artiste);

        // Get the artiste
        restArtisteMockMvc
            .perform(get(ENTITY_API_URL_ID, artiste.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(artiste.getId().intValue()))
            .andExpect(jsonPath("$.nomArtiste").value(DEFAULT_NOM_ARTISTE))
            .andExpect(jsonPath("$.prenomArtiste").value(DEFAULT_PRENOM_ARTISTE))
            .andExpect(jsonPath("$.domaine").value(DEFAULT_DOMAINE.toString()))
            .andExpect(jsonPath("$.autreDomaine").value(DEFAULT_AUTRE_DOMAINE));
    }

    @Test
    @Transactional
    void getNonExistingArtiste() throws Exception {
        // Get the artiste
        restArtisteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewArtiste() throws Exception {
        // Initialize the database
        artisteRepository.saveAndFlush(artiste);

        int databaseSizeBeforeUpdate = artisteRepository.findAll().size();

        // Update the artiste
        Artiste updatedArtiste = artisteRepository.findById(artiste.getId()).get();
        // Disconnect from session so that the updates on updatedArtiste are not directly saved in db
        em.detach(updatedArtiste);
        updatedArtiste
            .nomArtiste(UPDATED_NOM_ARTISTE)
            .prenomArtiste(UPDATED_PRENOM_ARTISTE)
            .domaine(UPDATED_DOMAINE)
            .autreDomaine(UPDATED_AUTRE_DOMAINE);

        restArtisteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedArtiste.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedArtiste))
            )
            .andExpect(status().isOk());

        // Validate the Artiste in the database
        List<Artiste> artisteList = artisteRepository.findAll();
        assertThat(artisteList).hasSize(databaseSizeBeforeUpdate);
        Artiste testArtiste = artisteList.get(artisteList.size() - 1);
        assertThat(testArtiste.getNomArtiste()).isEqualTo(UPDATED_NOM_ARTISTE);
        assertThat(testArtiste.getPrenomArtiste()).isEqualTo(UPDATED_PRENOM_ARTISTE);
        assertThat(testArtiste.getDomaine()).isEqualTo(UPDATED_DOMAINE);
        assertThat(testArtiste.getAutreDomaine()).isEqualTo(UPDATED_AUTRE_DOMAINE);
    }

    @Test
    @Transactional
    void putNonExistingArtiste() throws Exception {
        int databaseSizeBeforeUpdate = artisteRepository.findAll().size();
        artiste.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArtisteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, artiste.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(artiste))
            )
            .andExpect(status().isBadRequest());

        // Validate the Artiste in the database
        List<Artiste> artisteList = artisteRepository.findAll();
        assertThat(artisteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchArtiste() throws Exception {
        int databaseSizeBeforeUpdate = artisteRepository.findAll().size();
        artiste.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArtisteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(artiste))
            )
            .andExpect(status().isBadRequest());

        // Validate the Artiste in the database
        List<Artiste> artisteList = artisteRepository.findAll();
        assertThat(artisteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArtiste() throws Exception {
        int databaseSizeBeforeUpdate = artisteRepository.findAll().size();
        artiste.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArtisteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(artiste)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Artiste in the database
        List<Artiste> artisteList = artisteRepository.findAll();
        assertThat(artisteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateArtisteWithPatch() throws Exception {
        // Initialize the database
        artisteRepository.saveAndFlush(artiste);

        int databaseSizeBeforeUpdate = artisteRepository.findAll().size();

        // Update the artiste using partial update
        Artiste partialUpdatedArtiste = new Artiste();
        partialUpdatedArtiste.setId(artiste.getId());

        partialUpdatedArtiste.prenomArtiste(UPDATED_PRENOM_ARTISTE).domaine(UPDATED_DOMAINE);

        restArtisteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArtiste.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArtiste))
            )
            .andExpect(status().isOk());

        // Validate the Artiste in the database
        List<Artiste> artisteList = artisteRepository.findAll();
        assertThat(artisteList).hasSize(databaseSizeBeforeUpdate);
        Artiste testArtiste = artisteList.get(artisteList.size() - 1);
        assertThat(testArtiste.getNomArtiste()).isEqualTo(DEFAULT_NOM_ARTISTE);
        assertThat(testArtiste.getPrenomArtiste()).isEqualTo(UPDATED_PRENOM_ARTISTE);
        assertThat(testArtiste.getDomaine()).isEqualTo(UPDATED_DOMAINE);
        assertThat(testArtiste.getAutreDomaine()).isEqualTo(DEFAULT_AUTRE_DOMAINE);
    }

    @Test
    @Transactional
    void fullUpdateArtisteWithPatch() throws Exception {
        // Initialize the database
        artisteRepository.saveAndFlush(artiste);

        int databaseSizeBeforeUpdate = artisteRepository.findAll().size();

        // Update the artiste using partial update
        Artiste partialUpdatedArtiste = new Artiste();
        partialUpdatedArtiste.setId(artiste.getId());

        partialUpdatedArtiste
            .nomArtiste(UPDATED_NOM_ARTISTE)
            .prenomArtiste(UPDATED_PRENOM_ARTISTE)
            .domaine(UPDATED_DOMAINE)
            .autreDomaine(UPDATED_AUTRE_DOMAINE);

        restArtisteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArtiste.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedArtiste))
            )
            .andExpect(status().isOk());

        // Validate the Artiste in the database
        List<Artiste> artisteList = artisteRepository.findAll();
        assertThat(artisteList).hasSize(databaseSizeBeforeUpdate);
        Artiste testArtiste = artisteList.get(artisteList.size() - 1);
        assertThat(testArtiste.getNomArtiste()).isEqualTo(UPDATED_NOM_ARTISTE);
        assertThat(testArtiste.getPrenomArtiste()).isEqualTo(UPDATED_PRENOM_ARTISTE);
        assertThat(testArtiste.getDomaine()).isEqualTo(UPDATED_DOMAINE);
        assertThat(testArtiste.getAutreDomaine()).isEqualTo(UPDATED_AUTRE_DOMAINE);
    }

    @Test
    @Transactional
    void patchNonExistingArtiste() throws Exception {
        int databaseSizeBeforeUpdate = artisteRepository.findAll().size();
        artiste.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArtisteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, artiste.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(artiste))
            )
            .andExpect(status().isBadRequest());

        // Validate the Artiste in the database
        List<Artiste> artisteList = artisteRepository.findAll();
        assertThat(artisteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArtiste() throws Exception {
        int databaseSizeBeforeUpdate = artisteRepository.findAll().size();
        artiste.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArtisteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(artiste))
            )
            .andExpect(status().isBadRequest());

        // Validate the Artiste in the database
        List<Artiste> artisteList = artisteRepository.findAll();
        assertThat(artisteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArtiste() throws Exception {
        int databaseSizeBeforeUpdate = artisteRepository.findAll().size();
        artiste.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArtisteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(artiste)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Artiste in the database
        List<Artiste> artisteList = artisteRepository.findAll();
        assertThat(artisteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteArtiste() throws Exception {
        // Initialize the database
        artisteRepository.saveAndFlush(artiste);

        int databaseSizeBeforeDelete = artisteRepository.findAll().size();

        // Delete the artiste
        restArtisteMockMvc
            .perform(delete(ENTITY_API_URL_ID, artiste.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Artiste> artisteList = artisteRepository.findAll();
        assertThat(artisteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
