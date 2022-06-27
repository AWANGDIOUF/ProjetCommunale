package agir.gouv.sn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import agir.gouv.sn.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DemandeInterviewTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DemandeInterview.class);
        DemandeInterview demandeInterview1 = new DemandeInterview();
        demandeInterview1.setId(1L);
        DemandeInterview demandeInterview2 = new DemandeInterview();
        demandeInterview2.setId(demandeInterview1.getId());
        assertThat(demandeInterview1).isEqualTo(demandeInterview2);
        demandeInterview2.setId(2L);
        assertThat(demandeInterview1).isNotEqualTo(demandeInterview2);
        demandeInterview1.setId(null);
        assertThat(demandeInterview1).isNotEqualTo(demandeInterview2);
    }
}
