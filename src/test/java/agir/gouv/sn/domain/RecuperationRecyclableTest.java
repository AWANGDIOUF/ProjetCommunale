package agir.gouv.sn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import agir.gouv.sn.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RecuperationRecyclableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RecuperationRecyclable.class);
        RecuperationRecyclable recuperationRecyclable1 = new RecuperationRecyclable();
        recuperationRecyclable1.setId(1L);
        RecuperationRecyclable recuperationRecyclable2 = new RecuperationRecyclable();
        recuperationRecyclable2.setId(recuperationRecyclable1.getId());
        assertThat(recuperationRecyclable1).isEqualTo(recuperationRecyclable2);
        recuperationRecyclable2.setId(2L);
        assertThat(recuperationRecyclable1).isNotEqualTo(recuperationRecyclable2);
        recuperationRecyclable1.setId(null);
        assertThat(recuperationRecyclable1).isNotEqualTo(recuperationRecyclable2);
    }
}
