package agir.gouv.sn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import agir.gouv.sn.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DomaineActiviteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DomaineActivite.class);
        DomaineActivite domaineActivite1 = new DomaineActivite();
        domaineActivite1.setId(1L);
        DomaineActivite domaineActivite2 = new DomaineActivite();
        domaineActivite2.setId(domaineActivite1.getId());
        assertThat(domaineActivite1).isEqualTo(domaineActivite2);
        domaineActivite2.setId(2L);
        assertThat(domaineActivite1).isNotEqualTo(domaineActivite2);
        domaineActivite1.setId(null);
        assertThat(domaineActivite1).isNotEqualTo(domaineActivite2);
    }
}
