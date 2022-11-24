package sn.projet.communal.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.projet.communal.web.rest.TestUtil;

class DonneurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Donneur.class);
        Donneur donneur1 = new Donneur();
        donneur1.setId(1L);
        Donneur donneur2 = new Donneur();
        donneur2.setId(donneur1.getId());
        assertThat(donneur1).isEqualTo(donneur2);
        donneur2.setId(2L);
        assertThat(donneur1).isNotEqualTo(donneur2);
        donneur1.setId(null);
        assertThat(donneur1).isNotEqualTo(donneur2);
    }
}
