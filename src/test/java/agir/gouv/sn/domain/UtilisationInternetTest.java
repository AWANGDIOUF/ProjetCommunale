package agir.gouv.sn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import agir.gouv.sn.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UtilisationInternetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UtilisationInternet.class);
        UtilisationInternet utilisationInternet1 = new UtilisationInternet();
        utilisationInternet1.setId(1L);
        UtilisationInternet utilisationInternet2 = new UtilisationInternet();
        utilisationInternet2.setId(utilisationInternet1.getId());
        assertThat(utilisationInternet1).isEqualTo(utilisationInternet2);
        utilisationInternet2.setId(2L);
        assertThat(utilisationInternet1).isNotEqualTo(utilisationInternet2);
        utilisationInternet1.setId(null);
        assertThat(utilisationInternet1).isNotEqualTo(utilisationInternet2);
    }
}
