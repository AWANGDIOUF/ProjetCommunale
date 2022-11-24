package sn.projet.communal.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.projet.communal.web.rest.TestUtil;

class AnnonceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Annonce.class);
        Annonce annonce1 = new Annonce();
        annonce1.setId(1L);
        Annonce annonce2 = new Annonce();
        annonce2.setId(annonce1.getId());
        assertThat(annonce1).isEqualTo(annonce2);
        annonce2.setId(2L);
        assertThat(annonce1).isNotEqualTo(annonce2);
        annonce1.setId(null);
        assertThat(annonce1).isNotEqualTo(annonce2);
    }
}
