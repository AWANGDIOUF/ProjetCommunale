package sn.projet.communal.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.projet.communal.web.rest.TestUtil;

class TypeSportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeSport.class);
        TypeSport typeSport1 = new TypeSport();
        typeSport1.setId(1L);
        TypeSport typeSport2 = new TypeSport();
        typeSport2.setId(typeSport1.getId());
        assertThat(typeSport1).isEqualTo(typeSport2);
        typeSport2.setId(2L);
        assertThat(typeSport1).isNotEqualTo(typeSport2);
        typeSport1.setId(null);
        assertThat(typeSport1).isNotEqualTo(typeSport2);
    }
}
