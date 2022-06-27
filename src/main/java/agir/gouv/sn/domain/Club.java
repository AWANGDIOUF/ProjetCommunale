package agir.gouv.sn.domain;

import agir.gouv.sn.domain.enumeration.DisciplineClub;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Club.
 */
@Entity
@Table(name = "club")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Club implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_club")
    private String nomClub;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @Lob
    @Column(name = "logo")
    private byte[] logo;

    @Column(name = "logo_content_type")
    private String logoContentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "discipline")
    private DisciplineClub discipline;

    @OneToOne
    @JoinColumn(unique = true)
    private TypeSport typeSport;

    @OneToMany(mappedBy = "club")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "vainqueurs", "club" }, allowSetters = true)
    private Set<Combattant> combattants = new HashSet<>();

    @OneToMany(mappedBy = "club")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "vainqueurs", "club" }, allowSetters = true)
    private Set<Competition> competitions = new HashSet<>();

    @OneToMany(mappedBy = "club")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "equipe", "club" }, allowSetters = true)
    private Set<ArchiveSport> archves = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "artiste",
            "equipes",
            "clubs",
            "beneficiaires",
            "collecteurOdeurs",
            "vidanges",
            "recuperationRecyclables",
            "etablissements",
            "eleveurs",
        },
        allowSetters = true
    )
    private Quartier quartier;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Club id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomClub() {
        return this.nomClub;
    }

    public Club nomClub(String nomClub) {
        this.setNomClub(nomClub);
        return this;
    }

    public void setNomClub(String nomClub) {
        this.nomClub = nomClub;
    }

    public LocalDate getDateCreation() {
        return this.dateCreation;
    }

    public Club dateCreation(LocalDate dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public byte[] getLogo() {
        return this.logo;
    }

    public Club logo(byte[] logo) {
        this.setLogo(logo);
        return this;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return this.logoContentType;
    }

    public Club logoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
        return this;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public DisciplineClub getDiscipline() {
        return this.discipline;
    }

    public Club discipline(DisciplineClub discipline) {
        this.setDiscipline(discipline);
        return this;
    }

    public void setDiscipline(DisciplineClub discipline) {
        this.discipline = discipline;
    }

    public TypeSport getTypeSport() {
        return this.typeSport;
    }

    public void setTypeSport(TypeSport typeSport) {
        this.typeSport = typeSport;
    }

    public Club typeSport(TypeSport typeSport) {
        this.setTypeSport(typeSport);
        return this;
    }

    public Set<Combattant> getCombattants() {
        return this.combattants;
    }

    public void setCombattants(Set<Combattant> combattants) {
        if (this.combattants != null) {
            this.combattants.forEach(i -> i.setClub(null));
        }
        if (combattants != null) {
            combattants.forEach(i -> i.setClub(this));
        }
        this.combattants = combattants;
    }

    public Club combattants(Set<Combattant> combattants) {
        this.setCombattants(combattants);
        return this;
    }

    public Club addCombattant(Combattant combattant) {
        this.combattants.add(combattant);
        combattant.setClub(this);
        return this;
    }

    public Club removeCombattant(Combattant combattant) {
        this.combattants.remove(combattant);
        combattant.setClub(null);
        return this;
    }

    public Set<Competition> getCompetitions() {
        return this.competitions;
    }

    public void setCompetitions(Set<Competition> competitions) {
        if (this.competitions != null) {
            this.competitions.forEach(i -> i.setClub(null));
        }
        if (competitions != null) {
            competitions.forEach(i -> i.setClub(this));
        }
        this.competitions = competitions;
    }

    public Club competitions(Set<Competition> competitions) {
        this.setCompetitions(competitions);
        return this;
    }

    public Club addCompetition(Competition competition) {
        this.competitions.add(competition);
        competition.setClub(this);
        return this;
    }

    public Club removeCompetition(Competition competition) {
        this.competitions.remove(competition);
        competition.setClub(null);
        return this;
    }

    public Set<ArchiveSport> getArchves() {
        return this.archves;
    }

    public void setArchves(Set<ArchiveSport> archiveSports) {
        if (this.archves != null) {
            this.archves.forEach(i -> i.setClub(null));
        }
        if (archiveSports != null) {
            archiveSports.forEach(i -> i.setClub(this));
        }
        this.archves = archiveSports;
    }

    public Club archves(Set<ArchiveSport> archiveSports) {
        this.setArchves(archiveSports);
        return this;
    }

    public Club addArchve(ArchiveSport archiveSport) {
        this.archves.add(archiveSport);
        archiveSport.setClub(this);
        return this;
    }

    public Club removeArchve(ArchiveSport archiveSport) {
        this.archves.remove(archiveSport);
        archiveSport.setClub(null);
        return this;
    }

    public Quartier getQuartier() {
        return this.quartier;
    }

    public void setQuartier(Quartier quartier) {
        this.quartier = quartier;
    }

    public Club quartier(Quartier quartier) {
        this.setQuartier(quartier);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Club)) {
            return false;
        }
        return id != null && id.equals(((Club) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Club{" +
            "id=" + getId() +
            ", nomClub='" + getNomClub() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", logo='" + getLogo() + "'" +
            ", logoContentType='" + getLogoContentType() + "'" +
            ", discipline='" + getDiscipline() + "'" +
            "}";
    }
}
