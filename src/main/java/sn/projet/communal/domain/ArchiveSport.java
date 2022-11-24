package sn.projet.communal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ArchiveSport.
 */
@Entity
@Table(name = "archive_sport")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ArchiveSport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "annee")
    private LocalDate annee;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_archive_sport__equipe",
        joinColumns = @JoinColumn(name = "archive_sport_id"),
        inverseJoinColumns = @JoinColumn(name = "equipe_id")
    )
    @JsonIgnoreProperties(value = { "typeSport", "joueur", "quartiers", "matches", "archves" }, allowSetters = true)
    private Set<Equipe> equipes = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinTable(
        name = "rel_archive_sport__club",
        joinColumns = @JoinColumn(name = "archive_sport_id"),
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

    public ArchiveSport id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getAnnee() {
        return this.annee;
    }

    public ArchiveSport annee(LocalDate annee) {
        this.annee = annee;
        return this;
    }

    public void setAnnee(LocalDate annee) {
        this.annee = annee;
    }

    public Set<Equipe> getEquipes() {
        return this.equipes;
    }

    public ArchiveSport equipes(Set<Equipe> equipes) {
        this.setEquipes(equipes);
        return this;
    }

    public ArchiveSport addEquipe(Equipe equipe) {
        this.equipes.add(equipe);
        equipe.getArchves().add(this);
        return this;
    }

    public ArchiveSport removeEquipe(Equipe equipe) {
        this.equipes.remove(equipe);
        equipe.getArchves().remove(this);
        return this;
    }

    public void setEquipes(Set<Equipe> equipes) {
        this.equipes = equipes;
    }

    public Set<Club> getClubs() {
        return this.clubs;
    }

    public ArchiveSport clubs(Set<Club> clubs) {
        this.setClubs(clubs);
        return this;
    }

    public ArchiveSport addClub(Club club) {
        this.clubs.add(club);
        club.getArchves().add(this);
        return this;
    }

    public ArchiveSport removeClub(Club club) {
        this.clubs.remove(club);
        club.getArchves().remove(this);
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
        if (!(o instanceof ArchiveSport)) {
            return false;
        }
        return id != null && id.equals(((ArchiveSport) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArchiveSport{" +
            "id=" + getId() +
            ", annee='" + getAnnee() + "'" +
            "}";
    }
}
