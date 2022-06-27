package agir.gouv.sn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import agir.gouv.sn.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CollecteurOdeurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CollecteurOdeur.class);
        CollecteurOdeur collecteurOdeur1 = new CollecteurOdeur();
        collecteurOdeur1.setId(1L);
        CollecteurOdeur collecteurOdeur2 = new CollecteurOdeur();
        collecteurOdeur2.setId(collecteurOdeur1.getId());
        assertThat(collecteurOdeur1).isEqualTo(collecteurOdeur2);
        collecteurOdeur2.setId(2L);
        assertThat(collecteurOdeur1).isNotEqualTo(collecteurOdeur2);
        collecteurOdeur1.setId(null);
        assertThat(collecteurOdeur1).isNotEqualTo(collecteurOdeur2);
    }
}
