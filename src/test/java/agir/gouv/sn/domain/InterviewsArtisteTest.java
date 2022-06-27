package agir.gouv.sn.domain;

import static org.assertj.core.api.Assertions.assertThat;

import agir.gouv.sn.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InterviewsArtisteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InterviewsArtiste.class);
        InterviewsArtiste interviewsArtiste1 = new InterviewsArtiste();
        interviewsArtiste1.setId(1L);
        InterviewsArtiste interviewsArtiste2 = new InterviewsArtiste();
        interviewsArtiste2.setId(interviewsArtiste1.getId());
        assertThat(interviewsArtiste1).isEqualTo(interviewsArtiste2);
        interviewsArtiste2.setId(2L);
        assertThat(interviewsArtiste1).isNotEqualTo(interviewsArtiste2);
        interviewsArtiste1.setId(null);
        assertThat(interviewsArtiste1).isNotEqualTo(interviewsArtiste2);
    }
}
