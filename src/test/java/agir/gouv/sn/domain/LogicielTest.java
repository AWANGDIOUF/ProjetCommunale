package agir.gouv.sn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import agir.gouv.sn.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LogicielTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Logiciel.class);
        Logiciel logiciel1 = new Logiciel();
        logiciel1.setId(1L);
        Logiciel logiciel2 = new Logiciel();
        logiciel2.setId(logiciel1.getId());
        assertThat(logiciel1).isEqualTo(logiciel2);
        logiciel2.setId(2L);
        assertThat(logiciel1).isNotEqualTo(logiciel2);
        logiciel1.setId(null);
        assertThat(logiciel1).isNotEqualTo(logiciel2);
    }
}
