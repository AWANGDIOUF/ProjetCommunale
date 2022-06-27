package agir.gouv.sn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import agir.gouv.sn.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResultatExamenTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResultatExamen.class);
        ResultatExamen resultatExamen1 = new ResultatExamen();
        resultatExamen1.setId(1L);
        ResultatExamen resultatExamen2 = new ResultatExamen();
        resultatExamen2.setId(resultatExamen1.getId());
        assertThat(resultatExamen1).isEqualTo(resultatExamen2);
        resultatExamen2.setId(2L);
        assertThat(resultatExamen1).isNotEqualTo(resultatExamen2);
        resultatExamen1.setId(null);
        assertThat(resultatExamen1).isNotEqualTo(resultatExamen2);
    }
}
