package agir.gouv.sn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import agir.gouv.sn.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CalendrierEvenementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CalendrierEvenement.class);
        CalendrierEvenement calendrierEvenement1 = new CalendrierEvenement();
        calendrierEvenement1.setId(1L);
        CalendrierEvenement calendrierEvenement2 = new CalendrierEvenement();
        calendrierEvenement2.setId(calendrierEvenement1.getId());
        assertThat(calendrierEvenement1).isEqualTo(calendrierEvenement2);
        calendrierEvenement2.setId(2L);
        assertThat(calendrierEvenement1).isNotEqualTo(calendrierEvenement2);
        calendrierEvenement1.setId(null);
        assertThat(calendrierEvenement1).isNotEqualTo(calendrierEvenement2);
    }
}
