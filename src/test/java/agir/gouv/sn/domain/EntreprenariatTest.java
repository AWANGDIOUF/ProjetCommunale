package agir.gouv.sn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import agir.gouv.sn.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EntreprenariatTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Entreprenariat.class);
        Entreprenariat entreprenariat1 = new Entreprenariat();
        entreprenariat1.setId(1L);
        Entreprenariat entreprenariat2 = new Entreprenariat();
        entreprenariat2.setId(entreprenariat1.getId());
        assertThat(entreprenariat1).isEqualTo(entreprenariat2);
        entreprenariat2.setId(2L);
        assertThat(entreprenariat1).isNotEqualTo(entreprenariat2);
        entreprenariat1.setId(null);
        assertThat(entreprenariat1).isNotEqualTo(entreprenariat2);
    }
}
