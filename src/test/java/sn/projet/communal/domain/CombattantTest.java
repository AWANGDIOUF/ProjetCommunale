package sn.projet.communal.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.projet.communal.web.rest.TestUtil;

class CombattantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Combattant.class);
        Combattant combattant1 = new Combattant();
        combattant1.setId(1L);
        Combattant combattant2 = new Combattant();
        combattant2.setId(combattant1.getId());
        assertThat(combattant1).isEqualTo(combattant2);
        combattant2.setId(2L);
        assertThat(combattant1).isNotEqualTo(combattant2);
        combattant1.setId(null);
        assertThat(combattant1).isNotEqualTo(combattant2);
    }
}
