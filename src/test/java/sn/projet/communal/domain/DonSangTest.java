package sn.projet.communal.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.projet.communal.web.rest.TestUtil;

class DonSangTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DonSang.class);
        DonSang donSang1 = new DonSang();
        donSang1.setId(1L);
        DonSang donSang2 = new DonSang();
        donSang2.setId(donSang1.getId());
        assertThat(donSang1).isEqualTo(donSang2);
        donSang2.setId(2L);
        assertThat(donSang1).isNotEqualTo(donSang2);
        donSang1.setId(null);
        assertThat(donSang1).isNotEqualTo(donSang2);
    }
}
