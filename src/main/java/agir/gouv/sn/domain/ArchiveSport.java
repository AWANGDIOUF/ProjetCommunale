package agir.gouv.sn.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
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
    @Column(name = "id")
    private Long id;

    @Column(name = "annee")
    private LocalDate annee;

    @ManyToOne
    @JsonIgnoreProperties(value = { "typeSport", "joueurs", "matches", "archves", "quartier" }, allowSetters = true)
    private Equipe equipe;

    @ManyToOne
    @JsonIgnoreProperties(value = { "typeSport", "combattants", "competitions", "archves", "quartier" }, allowSetters = true)
    private Club club;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ArchiveSport id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getAnnee() {
        return this.annee;
    }

    public ArchiveSport annee(LocalDate annee) {
        this.setAnnee(annee);
        return this;
    }

    public void setAnnee(LocalDate annee) {
        this.annee = annee;
    }

    public Equipe getEquipe() {
        return this.equipe;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }

    public ArchiveSport equipe(Equipe equipe) {
        this.setEquipe(equipe);
        return this;
    }

    public Club getClub() {
        return this.club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public ArchiveSport club(Club club) {
        this.setClub(club);
        return this;
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
