package agir.gouv.sn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import agir.gouv.sn.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PropositionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Proposition.class);
        Proposition proposition1 = new Proposition();
        proposition1.setId(1L);
        Proposition proposition2 = new Proposition();
        proposition2.setId(proposition1.getId());
        assertThat(proposition1).isEqualTo(proposition2);
        proposition2.setId(2L);
        assertThat(proposition1).isNotEqualTo(proposition2);
        proposition1.setId(null);
        assertThat(proposition1).isNotEqualTo(proposition2);
    }
}
