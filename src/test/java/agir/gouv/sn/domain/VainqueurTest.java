package agir.gouv.sn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import agir.gouv.sn.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VainqueurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vainqueur.class);
        Vainqueur vainqueur1 = new Vainqueur();
        vainqueur1.setId(1L);
        Vainqueur vainqueur2 = new Vainqueur();
        vainqueur2.setId(vainqueur1.getId());
        assertThat(vainqueur1).isEqualTo(vainqueur2);
        vainqueur2.setId(2L);
        assertThat(vainqueur1).isNotEqualTo(vainqueur2);
        vainqueur1.setId(null);
        assertThat(vainqueur1).isNotEqualTo(vainqueur2);
    }
}
