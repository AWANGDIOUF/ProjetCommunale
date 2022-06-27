package agir.gouv.sn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import agir.gouv.sn.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EnsegnantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ensegnant.class);
        Ensegnant ensegnant1 = new Ensegnant();
        ensegnant1.setId(1L);
        Ensegnant ensegnant2 = new Ensegnant();
        ensegnant2.setId(ensegnant1.getId());
        assertThat(ensegnant1).isEqualTo(ensegnant2);
        ensegnant2.setId(2L);
        assertThat(ensegnant1).isNotEqualTo(ensegnant2);
        ensegnant1.setId(null);
        assertThat(ensegnant1).isNotEqualTo(ensegnant2);
    }
}
