package sn.projet.communal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.projet.communal.domain.enumeration.DisciplineClub;

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

    @JsonIgnoreProperties(value = { "equipe", "club" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private TypeSport typeSport;

    @ManyToOne
    @JsonIgnoreProperties(value = { "combattant", "clubs" }, allowSetters = true)
    private Combattant conmbattant;

    @OneToMany(mappedBy = "club")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "equipe", "club", "beneficiaire" }, allowSetters = true)
    private Set<Quartier> quartiers = new HashSet<>();

    @ManyToMany(mappedBy = "clubs")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "vainqueur", "clubs" }, allowSetters = true)
    private Set<Competition> competitions = new HashSet<>();

    @ManyToMany(mappedBy = "clubs")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "equipes", "clubs" }, allowSetters = true)
    private Set<ArchiveSport> archves = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Club id(Long id) {
        this.id = id;
        return this;
    }

    public String getNomClub() {
        return this.nomClub;
    }

    public Club nomClub(String nomClub) {
        this.nomClub = nomClub;
        return this;
    }

    public void setNomClub(String nomClub) {
        this.nomClub = nomClub;
    }

    public LocalDate getDateCreation() {
        return this.dateCreation;
    }

    public Club dateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public byte[] getLogo() {
        return this.logo;
    }

    public Club logo(byte[] logo) {
        this.logo = logo;
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
        this.discipline = discipline;
        return this;
    }

    public void setDiscipline(DisciplineClub discipline) {
        this.discipline = discipline;
    }

    public TypeSport getTypeSport() {
        return this.typeSport;
    }

    public Club typeSport(TypeSport typeSport) {
        this.setTypeSport(typeSport);
        return this;
    }

    public void setTypeSport(TypeSport typeSport) {
        this.typeSport = typeSport;
    }

    public Combattant getConmbattant() {
        return this.conmbattant;
    }

    public Club conmbattant(Combattant combattant) {
        this.setConmbattant(combattant);
        return this;
    }

    public void setConmbattant(Combattant combattant) {
        this.conmbattant = combattant;
    }

    public Set<Quartier> getQuartiers() {
        return this.quartiers;
    }

    public Club quartiers(Set<Quartier> quartiers) {
        this.setQuartiers(quartiers);
        return this;
    }

    public Club addQuartier(Quartier quartier) {
        this.quartiers.add(quartier);
        quartier.setClub(this);
        return this;
    }

    public Club removeQuartier(Quartier quartier) {
        this.quartiers.remove(quartier);
        quartier.setClub(null);
        return this;
    }

    public void setQuartiers(Set<Quartier> quartiers) {
        if (this.quartiers != null) {
            this.quartiers.forEach(i -> i.setClub(null));
        }
        if (quartiers != null) {
            quartiers.forEach(i -> i.setClub(this));
        }
        this.quartiers = quartiers;
    }

    public Set<Competition> getCompetitions() {
        return this.competitions;
    }

    public Club competitions(Set<Competition> competitions) {
        this.setCompetitions(competitions);
        return this;
    }

    public Club addCompetition(Competition competition) {
        this.competitions.add(competition);
        competition.getClubs().add(this);
        return this;
    }

    public Club removeCompetition(Competition competition) {
        this.competitions.remove(competition);
        competition.getClubs().remove(this);
        return this;
    }

    public void setCompetitions(Set<Competition> competitions) {
        if (this.competitions != null) {
            this.competitions.forEach(i -> i.removeClub(this));
        }
        if (competitions != null) {
            competitions.forEach(i -> i.addClub(this));
        }
        this.competitions = competitions;
    }

    public Set<ArchiveSport> getArchves() {
        return this.archves;
    }

    public Club archves(Set<ArchiveSport> archiveSports) {
        this.setArchves(archiveSports);
        return this;
    }

    public Club addArchve(ArchiveSport archiveSport) {
        this.archves.add(archiveSport);
        archiveSport.getClubs().add(this);
        return this;
    }

    public Club removeArchve(ArchiveSport archiveSport) {
        this.archves.remove(archiveSport);
        archiveSport.getClubs().remove(this);
        return this;
    }

    public void setArchves(Set<ArchiveSport> archiveSports) {
        if (this.archves != null) {
            this.archves.forEach(i -> i.removeClub(this));
        }
        if (archiveSports != null) {
            archiveSports.forEach(i -> i.addClub(this));
        }
        this.archves = archiveSports;
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
