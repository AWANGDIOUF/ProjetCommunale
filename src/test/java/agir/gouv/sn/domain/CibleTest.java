package agir.gouv.sn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import agir.gouv.sn.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CibleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cible.class);
        Cible cible1 = new Cible();
        cible1.setId(1L);
        Cible cible2 = new Cible();
        cible2.setId(cible1.getId());
        assertThat(cible1).isEqualTo(cible2);
        cible2.setId(2L);
        assertThat(cible1).isNotEqualTo(cible2);
        cible1.setId(null);
        assertThat(cible1).isNotEqualTo(cible2);
    }
}
