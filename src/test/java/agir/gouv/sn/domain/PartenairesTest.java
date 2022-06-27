package agir.gouv.sn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import agir.gouv.sn.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PartenairesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Partenaires.class);
        Partenaires partenaires1 = new Partenaires();
        partenaires1.setId(1L);
        Partenaires partenaires2 = new Partenaires();
        partenaires2.setId(partenaires1.getId());
        assertThat(partenaires1).isEqualTo(partenaires2);
        partenaires2.setId(2L);
        assertThat(partenaires1).isNotEqualTo(partenaires2);
        partenaires1.setId(null);
        assertThat(partenaires1).isNotEqualTo(partenaires2);
    }
}
