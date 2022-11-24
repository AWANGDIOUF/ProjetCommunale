package sn.projet.communal.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.projet.communal.web.rest.TestUtil;

class DonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Don.class);
        Don don1 = new Don();
        don1.setId(1L);
        Don don2 = new Don();
        don2.setId(don1.getId());
        assertThat(don1).isEqualTo(don2);
        don2.setId(2L);
        assertThat(don1).isNotEqualTo(don2);
        don1.setId(null);
        assertThat(don1).isNotEqualTo(don2);
    }
}
