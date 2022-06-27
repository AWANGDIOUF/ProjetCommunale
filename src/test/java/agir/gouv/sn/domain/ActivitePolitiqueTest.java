package agir.gouv.sn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import agir.gouv.sn.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ActivitePolitiqueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActivitePolitique.class);
        ActivitePolitique activitePolitique1 = new ActivitePolitique();
        activitePolitique1.setId(1L);
        ActivitePolitique activitePolitique2 = new ActivitePolitique();
        activitePolitique2.setId(activitePolitique1.getId());
        assertThat(activitePolitique1).isEqualTo(activitePolitique2);
        activitePolitique2.setId(2L);
        assertThat(activitePolitique1).isNotEqualTo(activitePolitique2);
        activitePolitique1.setId(null);
        assertThat(activitePolitique1).isNotEqualTo(activitePolitique2);
    }
}
