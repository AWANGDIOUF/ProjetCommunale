package sn.projet.communal.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.projet.communal.web.rest.TestUtil;

class ArchiveSportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArchiveSport.class);
        ArchiveSport archiveSport1 = new ArchiveSport();
        archiveSport1.setId(1L);
        ArchiveSport archiveSport2 = new ArchiveSport();
        archiveSport2.setId(archiveSport1.getId());
        assertThat(archiveSport1).isEqualTo(archiveSport2);
        archiveSport2.setId(2L);
        assertThat(archiveSport1).isNotEqualTo(archiveSport2);
        archiveSport1.setId(null);
        assertThat(archiveSport1).isNotEqualTo(archiveSport2);
    }
}
