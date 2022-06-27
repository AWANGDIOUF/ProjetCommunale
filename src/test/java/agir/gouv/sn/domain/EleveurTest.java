package agir.gouv.sn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import agir.gouv.sn.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EleveurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Eleveur.class);
        Eleveur eleveur1 = new Eleveur();
        eleveur1.setId(1L);
        Eleveur eleveur2 = new Eleveur();
        eleveur2.setId(eleveur1.getId());
        assertThat(eleveur1).isEqualTo(eleveur2);
        eleveur2.setId(2L);
        assertThat(eleveur1).isNotEqualTo(eleveur2);
        eleveur1.setId(null);
        assertThat(eleveur1).isNotEqualTo(eleveur2);
    }
}
