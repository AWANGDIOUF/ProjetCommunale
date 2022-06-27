package agir.gouv.sn.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Match.
 */
@Entity
@Table(name = "jhi_match")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Match implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date_match")
    private Instant dateMatch;

    @Column(name = "lieu_match")
    private String lieuMatch;

    @Column(name = "score_match")
    private Integer scoreMatch;

    @ManyToOne
    @JsonIgnoreProperties(value = { "typeSport", "joueurs", "matches", "archves", "quartier" }, allowSetters = true)
    private Equipe equipe;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Match id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateMatch() {
        return this.dateMatch;
    }

    public Match dateMatch(Instant dateMatch) {
        this.setDateMatch(dateMatch);
        return this;
    }

    public void setDateMatch(Instant dateMatch) {
        this.dateMatch = dateMatch;
    }

    public String getLieuMatch() {
        return this.lieuMatch;
    }

    public Match lieuMatch(String lieuMatch) {
        this.setLieuMatch(lieuMatch);
        return this;
    }

    public void setLieuMatch(String lieuMatch) {
        this.lieuMatch = lieuMatch;
    }

    public Integer getScoreMatch() {
        return this.scoreMatch;
    }

    public Match scoreMatch(Integer scoreMatch) {
        this.setScoreMatch(scoreMatch);
        return this;
    }

    public void setScoreMatch(Integer scoreMatch) {
        this.scoreMatch = scoreMatch;
    }

    public Equipe getEquipe() {
        return this.equipe;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }

    public Match equipe(Equipe equipe) {
        this.setEquipe(equipe);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Match)) {
            return false;
        }
        return id != null && id.equals(((Match) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Match{" +
            "id=" + getId() +
            ", dateMatch='" + getDateMatch() + "'" +
            ", lieuMatch='" + getLieuMatch() + "'" +
            ", scoreMatch=" + getScoreMatch() +
            "}";
    }
}
