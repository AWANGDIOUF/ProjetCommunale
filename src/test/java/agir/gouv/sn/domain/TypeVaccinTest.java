package agir.gouv.sn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import agir.gouv.sn.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypeVaccinTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeVaccin.class);
        TypeVaccin typeVaccin1 = new TypeVaccin();
        typeVaccin1.setId(1L);
        TypeVaccin typeVaccin2 = new TypeVaccin();
        typeVaccin2.setId(typeVaccin1.getId());
        assertThat(typeVaccin1).isEqualTo(typeVaccin2);
        typeVaccin2.setId(2L);
        assertThat(typeVaccin1).isNotEqualTo(typeVaccin2);
        typeVaccin1.setId(null);
        assertThat(typeVaccin1).isNotEqualTo(typeVaccin2);
    }
}
