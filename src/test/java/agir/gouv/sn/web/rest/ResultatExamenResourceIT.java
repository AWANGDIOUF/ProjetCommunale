package agir.gouv.sn.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import agir.gouv.sn.IntegrationTest;
import agir.gouv.sn.domain.ResultatExamen;
import agir.gouv.sn.domain.enumeration.Examen;
import agir.gouv.sn.repository.ResultatExamenRepository;
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

/**
 * Integration tests for the {@link ResultatExamenResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ResultatExamenResourceIT {

    private static final Examen DEFAULT_TYPE_EXAMENT = Examen.CFEE;
    private static final Examen UPDATED_TYPE_EXAMENT = Examen.BFEM;

    private static final String DEFAULT_AUTRE_EXAMEN = "AAAAAAAAAA";
    private static final String UPDATED_AUTRE_EXAMEN = "BBBBBBBBBB";

    private static final Double DEFAULT_TAUX_REUISSITE = 1D;
    private static final Double UPDATED_TAUX_REUISSITE = 2D;

    private static final LocalDate DEFAULT_ANNEE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ANNEE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/resultat-examen";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ResultatExamenRepository resultatExamenRepository;

    @Mock
    private ResultatExamenRepository resultatExamenRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restResultatExamenMockMvc;

    private ResultatExamen resultatExamen;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResultatExamen createEntity(EntityManager em) {
        ResultatExamen resultatExamen = new ResultatExamen()
            .typeExament(DEFAULT_TYPE_EXAMENT)
            .autreExamen(DEFAULT_AUTRE_EXAMEN)
            .tauxReuissite(DEFAULT_TAUX_REUISSITE)
            .annee(DEFAULT_ANNEE);
        return resultatExamen;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResultatExamen createUpdatedEntity(EntityManager em) {
        ResultatExamen resultatExamen = new ResultatExamen()
            .typeExament(UPDATED_TYPE_EXAMENT)
            .autreExamen(UPDATED_AUTRE_EXAMEN)
            .tauxReuissite(UPDATED_TAUX_REUISSITE)
            .annee(UPDATED_ANNEE);
        return resultatExamen;
    }

    @BeforeEach
    public void initTest() {
        resultatExamen = createEntity(em);
    }

    @Test
    @Transactional
    void createResultatExamen() throws Exception {
        int databaseSizeBeforeCreate = resultatExamenRepository.findAll().size();
        // Create the ResultatExamen
        restResultatExamenMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resultatExamen))
            )
            .andExpect(status().isCreated());

        // Validate the ResultatExamen in the database
        List<ResultatExamen> resultatExamenList = resultatExamenRepository.findAll();
        assertThat(resultatExamenList).hasSize(databaseSizeBeforeCreate + 1);
        ResultatExamen testResultatExamen = resultatExamenList.get(resultatExamenList.size() - 1);
        assertThat(testResultatExamen.getTypeExament()).isEqualTo(DEFAULT_TYPE_EXAMENT);
        assertThat(testResultatExamen.getAutreExamen()).isEqualTo(DEFAULT_AUTRE_EXAMEN);
        assertThat(testResultatExamen.getTauxReuissite()).isEqualTo(DEFAULT_TAUX_REUISSITE);
        assertThat(testResultatExamen.getAnnee()).isEqualTo(DEFAULT_ANNEE);
    }

    @Test
    @Transactional
    void createResultatExamenWithExistingId() throws Exception {
        // Create the ResultatExamen with an existing ID
        resultatExamen.setId(1L);

        int databaseSizeBeforeCreate = resultatExamenRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restResultatExamenMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resultatExamen))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResultatExamen in the database
        List<ResultatExamen> resultatExamenList = resultatExamenRepository.findAll();
        assertThat(resultatExamenList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllResultatExamen() throws Exception {
        // Initialize the database
        resultatExamenRepository.saveAndFlush(resultatExamen);

        // Get all the resultatExamenList
        restResultatExamenMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resultatExamen.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeExament").value(hasItem(DEFAULT_TYPE_EXAMENT.toString())))
            .andExpect(jsonPath("$.[*].autreExamen").value(hasItem(DEFAULT_AUTRE_EXAMEN)))
            .andExpect(jsonPath("$.[*].tauxReuissite").value(hasItem(DEFAULT_TAUX_REUISSITE.doubleValue())))
            .andExpect(jsonPath("$.[*].annee").value(hasItem(DEFAULT_ANNEE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllResultatExamenWithEagerRelationshipsIsEnabled() throws Exception {
        when(resultatExamenRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restResultatExamenMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(resultatExamenRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllResultatExamenWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(resultatExamenRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restResultatExamenMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(resultatExamenRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getResultatExamen() throws Exception {
        // Initialize the database
        resultatExamenRepository.saveAndFlush(resultatExamen);

        // Get the resultatExamen
        restResultatExamenMockMvc
            .perform(get(ENTITY_API_URL_ID, resultatExamen.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(resultatExamen.getId().intValue()))
            .andExpect(jsonPath("$.typeExament").value(DEFAULT_TYPE_EXAMENT.toString()))
            .andExpect(jsonPath("$.autreExamen").value(DEFAULT_AUTRE_EXAMEN))
            .andExpect(jsonPath("$.tauxReuissite").value(DEFAULT_TAUX_REUISSITE.doubleValue()))
            .andExpect(jsonPath("$.annee").value(DEFAULT_ANNEE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingResultatExamen() throws Exception {
        // Get the resultatExamen
        restResultatExamenMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewResultatExamen() throws Exception {
        // Initialize the database
        resultatExamenRepository.saveAndFlush(resultatExamen);

        int databaseSizeBeforeUpdate = resultatExamenRepository.findAll().size();

        // Update the resultatExamen
        ResultatExamen updatedResultatExamen = resultatExamenRepository.findById(resultatExamen.getId()).get();
        // Disconnect from session so that the updates on updatedResultatExamen are not directly saved in db
        em.detach(updatedResultatExamen);
        updatedResultatExamen
            .typeExament(UPDATED_TYPE_EXAMENT)
            .autreExamen(UPDATED_AUTRE_EXAMEN)
            .tauxReuissite(UPDATED_TAUX_REUISSITE)
            .annee(UPDATED_ANNEE);

        restResultatExamenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedResultatExamen.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedResultatExamen))
            )
            .andExpect(status().isOk());

        // Validate the ResultatExamen in the database
        List<ResultatExamen> resultatExamenList = resultatExamenRepository.findAll();
        assertThat(resultatExamenList).hasSize(databaseSizeBeforeUpdate);
        ResultatExamen testResultatExamen = resultatExamenList.get(resultatExamenList.size() - 1);
        assertThat(testResultatExamen.getTypeExament()).isEqualTo(UPDATED_TYPE_EXAMENT);
        assertThat(testResultatExamen.getAutreExamen()).isEqualTo(UPDATED_AUTRE_EXAMEN);
        assertThat(testResultatExamen.getTauxReuissite()).isEqualTo(UPDATED_TAUX_REUISSITE);
        assertThat(testResultatExamen.getAnnee()).isEqualTo(UPDATED_ANNEE);
    }

    @Test
    @Transactional
    void putNonExistingResultatExamen() throws Exception {
        int databaseSizeBeforeUpdate = resultatExamenRepository.findAll().size();
        resultatExamen.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResultatExamenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, resultatExamen.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resultatExamen))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResultatExamen in the database
        List<ResultatExamen> resultatExamenList = resultatExamenRepository.findAll();
        assertThat(resultatExamenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchResultatExamen() throws Exception {
        int databaseSizeBeforeUpdate = resultatExamenRepository.findAll().size();
        resultatExamen.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResultatExamenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(resultatExamen))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResultatExamen in the database
        List<ResultatExamen> resultatExamenList = resultatExamenRepository.findAll();
        assertThat(resultatExamenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamResultatExamen() throws Exception {
        int databaseSizeBeforeUpdate = resultatExamenRepository.findAll().size();
        resultatExamen.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResultatExamenMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(resultatExamen)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ResultatExamen in the database
        List<ResultatExamen> resultatExamenList = resultatExamenRepository.findAll();
        assertThat(resultatExamenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateResultatExamenWithPatch() throws Exception {
        // Initialize the database
        resultatExamenRepository.saveAndFlush(resultatExamen);

        int databaseSizeBeforeUpdate = resultatExamenRepository.findAll().size();

        // Update the resultatExamen using partial update
        ResultatExamen partialUpdatedResultatExamen = new ResultatExamen();
        partialUpdatedResultatExamen.setId(resultatExamen.getId());

        partialUpdatedResultatExamen.autreExamen(UPDATED_AUTRE_EXAMEN).annee(UPDATED_ANNEE);

        restResultatExamenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResultatExamen.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResultatExamen))
            )
            .andExpect(status().isOk());

        // Validate the ResultatExamen in the database
        List<ResultatExamen> resultatExamenList = resultatExamenRepository.findAll();
        assertThat(resultatExamenList).hasSize(databaseSizeBeforeUpdate);
        ResultatExamen testResultatExamen = resultatExamenList.get(resultatExamenList.size() - 1);
        assertThat(testResultatExamen.getTypeExament()).isEqualTo(DEFAULT_TYPE_EXAMENT);
        assertThat(testResultatExamen.getAutreExamen()).isEqualTo(UPDATED_AUTRE_EXAMEN);
        assertThat(testResultatExamen.getTauxReuissite()).isEqualTo(DEFAULT_TAUX_REUISSITE);
        assertThat(testResultatExamen.getAnnee()).isEqualTo(UPDATED_ANNEE);
    }

    @Test
    @Transactional
    void fullUpdateResultatExamenWithPatch() throws Exception {
        // Initialize the database
        resultatExamenRepository.saveAndFlush(resultatExamen);

        int databaseSizeBeforeUpdate = resultatExamenRepository.findAll().size();

        // Update the resultatExamen using partial update
        ResultatExamen partialUpdatedResultatExamen = new ResultatExamen();
        partialUpdatedResultatExamen.setId(resultatExamen.getId());

        partialUpdatedResultatExamen
            .typeExament(UPDATED_TYPE_EXAMENT)
            .autreExamen(UPDATED_AUTRE_EXAMEN)
            .tauxReuissite(UPDATED_TAUX_REUISSITE)
            .annee(UPDATED_ANNEE);

        restResultatExamenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedResultatExamen.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedResultatExamen))
            )
            .andExpect(status().isOk());

        // Validate the ResultatExamen in the database
        List<ResultatExamen> resultatExamenList = resultatExamenRepository.findAll();
        assertThat(resultatExamenList).hasSize(databaseSizeBeforeUpdate);
        ResultatExamen testResultatExamen = resultatExamenList.get(resultatExamenList.size() - 1);
        assertThat(testResultatExamen.getTypeExament()).isEqualTo(UPDATED_TYPE_EXAMENT);
        assertThat(testResultatExamen.getAutreExamen()).isEqualTo(UPDATED_AUTRE_EXAMEN);
        assertThat(testResultatExamen.getTauxReuissite()).isEqualTo(UPDATED_TAUX_REUISSITE);
        assertThat(testResultatExamen.getAnnee()).isEqualTo(UPDATED_ANNEE);
    }

    @Test
    @Transactional
    void patchNonExistingResultatExamen() throws Exception {
        int databaseSizeBeforeUpdate = resultatExamenRepository.findAll().size();
        resultatExamen.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResultatExamenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, resultatExamen.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resultatExamen))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResultatExamen in the database
        List<ResultatExamen> resultatExamenList = resultatExamenRepository.findAll();
        assertThat(resultatExamenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchResultatExamen() throws Exception {
        int databaseSizeBeforeUpdate = resultatExamenRepository.findAll().size();
        resultatExamen.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResultatExamenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(resultatExamen))
            )
            .andExpect(status().isBadRequest());

        // Validate the ResultatExamen in the database
        List<ResultatExamen> resultatExamenList = resultatExamenRepository.findAll();
        assertThat(resultatExamenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamResultatExamen() throws Exception {
        int databaseSizeBeforeUpdate = resultatExamenRepository.findAll().size();
        resultatExamen.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restResultatExamenMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(resultatExamen))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ResultatExamen in the database
        List<ResultatExamen> resultatExamenList = resultatExamenRepository.findAll();
        assertThat(resultatExamenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteResultatExamen() throws Exception {
        // Initialize the database
        resultatExamenRepository.saveAndFlush(resultatExamen);

        int databaseSizeBeforeDelete = resultatExamenRepository.findAll().size();

        // Delete the resultatExamen
        restResultatExamenMockMvc
            .perform(delete(ENTITY_API_URL_ID, resultatExamen.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ResultatExamen> resultatExamenList = resultatExamenRepository.findAll();
        assertThat(resultatExamenList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
