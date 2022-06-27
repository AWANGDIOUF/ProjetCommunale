package agir.gouv.sn.domain;

import agir.gouv.sn.domain.enumeration.DisciplineClub;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Competition.
 */
@Entity
@Table(name = "competition")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Competition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "date_competition")
    private Instant dateCompetition;

    @Column(name = "lieu_competition")
    private String lieuCompetition;

    @Enumerated(EnumType.STRING)
    @Column(name = "discipline")
    private DisciplineClub discipline;

    @OneToMany(mappedBy = "competition")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "competition", "combattant" }, allowSetters = true)
    private Set<Vainqueur> vainqueurs = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "typeSport", "combattants", "competitions", "archves", "quartier" }, allowSetters = true)
    private Club club;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Competition id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateCompetition() {
        return this.dateCompetition;
    }

    public Competition dateCompetition(Instant dateCompetition) {
        this.setDateCompetition(dateCompetition);
        return this;
    }

    public void setDateCompetition(Instant dateCompetition) {
        this.dateCompetition = dateCompetition;
    }

    public String getLieuCompetition() {
        return this.lieuCompetition;
    }

    public Competition lieuCompetition(String lieuCompetition) {
        this.setLieuCompetition(lieuCompetition);
        return this;
    }

    public void setLieuCompetition(String lieuCompetition) {
        this.lieuCompetition = lieuCompetition;
    }

    public DisciplineClub getDiscipline() {
        return this.discipline;
    }

    public Competition discipline(DisciplineClub discipline) {
        this.setDiscipline(discipline);
        return this;
    }

    public void setDiscipline(DisciplineClub discipline) {
        this.discipline = discipline;
    }

    public Set<Vainqueur> getVainqueurs() {
        return this.vainqueurs;
    }

    public void setVainqueurs(Set<Vainqueur> vainqueurs) {
        if (this.vainqueurs != null) {
            this.vainqueurs.forEach(i -> i.setCompetition(null));
        }
        if (vainqueurs != null) {
            vainqueurs.forEach(i -> i.setCompetition(this));
        }
        this.vainqueurs = vainqueurs;
    }

    public Competition vainqueurs(Set<Vainqueur> vainqueurs) {
        this.setVainqueurs(vainqueurs);
        return this;
    }

    public Competition addVainqueur(Vainqueur vainqueur) {
        this.vainqueurs.add(vainqueur);
        vainqueur.setCompetition(this);
        return this;
    }

    public Competition removeVainqueur(Vainqueur vainqueur) {
        this.vainqueurs.remove(vainqueur);
        vainqueur.setCompetition(null);
        return this;
    }

    public Club getClub() {
        return this.club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public Competition club(Club club) {
        this.setClub(club);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Competition)) {
            return false;
        }
        return id != null && id.equals(((Competition) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Competition{" +
            "id=" + getId() +
            ", dateCompetition='" + getDateCompetition() + "'" +
            ", lieuCompetition='" + getLieuCompetition() + "'" +
            ", discipline='" + getDiscipline() + "'" +
            "}";
    }
}
