package agir.gouv.sn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import agir.gouv.sn.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LienTutorielTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LienTutoriel.class);
        LienTutoriel lienTutoriel1 = new LienTutoriel();
        lienTutoriel1.setId(1L);
        LienTutoriel lienTutoriel2 = new LienTutoriel();
        lienTutoriel2.setId(lienTutoriel1.getId());
        assertThat(lienTutoriel1).isEqualTo(lienTutoriel2);
        lienTutoriel2.setId(2L);
        assertThat(lienTutoriel1).isNotEqualTo(lienTutoriel2);
        lienTutoriel1.setId(null);
        assertThat(lienTutoriel1).isNotEqualTo(lienTutoriel2);
    }
}
