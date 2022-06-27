package agir.gouv.sn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import agir.gouv.sn.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SensibiisationInternetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SensibiisationInternet.class);
        SensibiisationInternet sensibiisationInternet1 = new SensibiisationInternet();
        sensibiisationInternet1.setId(1L);
        SensibiisationInternet sensibiisationInternet2 = new SensibiisationInternet();
        sensibiisationInternet2.setId(sensibiisationInternet1.getId());
        assertThat(sensibiisationInternet1).isEqualTo(sensibiisationInternet2);
        sensibiisationInternet2.setId(2L);
        assertThat(sensibiisationInternet1).isNotEqualTo(sensibiisationInternet2);
        sensibiisationInternet1.setId(null);
        assertThat(sensibiisationInternet1).isNotEqualTo(sensibiisationInternet2);
    }
}
