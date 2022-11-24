package sn.projet.communal.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.projet.communal.web.rest.TestUtil;

class VaccinationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vaccination.class);
        Vaccination vaccination1 = new Vaccination();
        vaccination1.setId(1L);
        Vaccination vaccination2 = new Vaccination();
        vaccination2.setId(vaccination1.getId());
        assertThat(vaccination1).isEqualTo(vaccination2);
        vaccination2.setId(2L);
        assertThat(vaccination1).isNotEqualTo(vaccination2);
        vaccination1.setId(null);
        assertThat(vaccination1).isNotEqualTo(vaccination2);
    }
}
