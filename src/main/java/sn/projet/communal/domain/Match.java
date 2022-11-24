package sn.projet.communal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
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
    private Long id;

    @Column(name = "date")
    private Instant date;

    @Column(name = "lieu")
    private String lieu;

    @Column(name = "score")
    private Integer score;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_jhi_match__equipe",
        joinColumns = @JoinColumn(name = "jhi_match_id"),
        inverseJoinColumns = @JoinColumn(name = "equipe_id")
    )
    @JsonIgnoreProperties(value = { "typeSport", "joueur", "quartiers", "matches", "archves" }, allowSetters = true)
    private Set<Equipe> equipes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Match id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getDate() {
        return this.date;
    }

    public Match date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getLieu() {
        return this.lieu;
    }

    public Match lieu(String lieu) {
        this.lieu = lieu;
        return this;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public Integer getScore() {
        return this.score;
    }

    public Match score(Integer score) {
        this.score = score;
        return this;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Set<Equipe> getEquipes() {
        return this.equipes;
    }

    public Match equipes(Set<Equipe> equipes) {
        this.setEquipes(equipes);
        return this;
    }

    public Match addEquipe(Equipe equipe) {
        this.equipes.add(equipe);
        equipe.getMatches().add(this);
        return this;
    }

    public Match removeEquipe(Equipe equipe) {
        this.equipes.remove(equipe);
        equipe.getMatches().remove(this);
        return this;
    }

    public void setEquipes(Set<Equipe> equipes) {
        this.equipes = equipes;
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
            ", date='" + getDate() + "'" +
            ", lieu='" + getLieu() + "'" +
            ", score=" + getScore() +
            "}";
    }
}
