package sn.projet.communal.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.projet.communal.web.rest.TestUtil;

class BeneficiaireTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Beneficiaire.class);
        Beneficiaire beneficiaire1 = new Beneficiaire();
        beneficiaire1.setId(1L);
        Beneficiaire beneficiaire2 = new Beneficiaire();
        beneficiaire2.setId(beneficiaire1.getId());
        assertThat(beneficiaire1).isEqualTo(beneficiaire2);
        beneficiaire2.setId(2L);
        assertThat(beneficiaire1).isNotEqualTo(beneficiaire2);
        beneficiaire1.setId(null);
        assertThat(beneficiaire1).isNotEqualTo(beneficiaire2);
    }
}
