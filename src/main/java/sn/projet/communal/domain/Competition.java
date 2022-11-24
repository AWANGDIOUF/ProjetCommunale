package sn.projet.communal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.projet.communal.domain.enumeration.DisciplineClub;

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
    private Long id;

    @Column(name = "date")
    private Instant date;

    @Column(name = "lieu")
    private String lieu;

    @Enumerated(EnumType.STRING)
    @Column(name = "discipline")
    private DisciplineClub discipline;

    @ManyToOne
    @JsonIgnoreProperties(value = { "competitions", "combattants" }, allowSetters = true)
    private Vainqueur vainqueur;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_competition__club",
        joinColumns = @JoinColumn(name = "competition_id"),
        inverseJoinColumns = @JoinColumn(name = "club_id")
    )
    @JsonIgnoreProperties(value = { "typeSport", "conmbattant", "quartiers", "competitions", "archves" }, allowSetters = true)
    private Set<Club> clubs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Competition id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getDate() {
        return this.date;
    }

    public Competition date(Instant date) {
        this.date = date;
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getLieu() {
        return this.lieu;
    }

    public Competition lieu(String lieu) {
        this.lieu = lieu;
        return this;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public DisciplineClub getDiscipline() {
        return this.discipline;
    }

    public Competition discipline(DisciplineClub discipline) {
        this.discipline = discipline;
        return this;
    }

    public void setDiscipline(DisciplineClub discipline) {
        this.discipline = discipline;
    }

    public Vainqueur getVainqueur() {
        return this.vainqueur;
    }

    public Competition vainqueur(Vainqueur vainqueur) {
        this.setVainqueur(vainqueur);
        return this;
    }

    public void setVainqueur(Vainqueur vainqueur) {
        this.vainqueur = vainqueur;
    }

    public Set<Club> getClubs() {
        return this.clubs;
    }

    public Competition clubs(Set<Club> clubs) {
        this.setClubs(clubs);
        return this;
    }

    public Competition addClub(Club club) {
        this.clubs.add(club);
        club.getCompetitions().add(this);
        return this;
    }

    public Competition removeClub(Club club) {
        this.clubs.remove(club);
        club.getCompetitions().remove(this);
        return this;
    }

    public void setClubs(Set<Club> clubs) {
        this.clubs = clubs;
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
            ", date='" + getDate() + "'" +
            ", lieu='" + getLieu() + "'" +
            ", discipline='" + getDiscipline() + "'" +
            "}";
    }
}
