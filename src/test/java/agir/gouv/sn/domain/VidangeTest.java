package agir.gouv.sn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import agir.gouv.sn.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VidangeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Vidange.class);
        Vidange vidange1 = new Vidange();
        vidange1.setId(1L);
        Vidange vidange2 = new Vidange();
        vidange2.setId(vidange1.getId());
        assertThat(vidange1).isEqualTo(vidange2);
        vidange2.setId(2L);
        assertThat(vidange1).isNotEqualTo(vidange2);
        vidange1.setId(null);
        assertThat(vidange1).isNotEqualTo(vidange2);
    }
}
