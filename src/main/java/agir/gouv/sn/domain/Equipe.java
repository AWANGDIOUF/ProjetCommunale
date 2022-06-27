package agir.gouv.sn.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Equipe.
 */
@Entity
@Table(name = "equipe")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Equipe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_equipe")
    private String nomEquipe;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @Lob
    @Column(name = "logo")
    private byte[] logo;

    @Column(name = "logo_content_type")
    private String logoContentType;

    @OneToOne
    @JoinColumn(unique = true)
    private TypeSport typeSport;

    @OneToMany(mappedBy = "equipe")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "equipe" }, allowSetters = true)
    private Set<Joueur> joueurs = new HashSet<>();

    @OneToMany(mappedBy = "equipe")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "equipe" }, allowSetters = true)
    private Set<Match> matches = new HashSet<>();

    @OneToMany(mappedBy = "equipe")
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

    public Equipe id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomEquipe() {
        return this.nomEquipe;
    }

    public Equipe nomEquipe(String nomEquipe) {
        this.setNomEquipe(nomEquipe);
        return this;
    }

    public void setNomEquipe(String nomEquipe) {
        this.nomEquipe = nomEquipe;
    }

    public LocalDate getDateCreation() {
        return this.dateCreation;
    }

    public Equipe dateCreation(LocalDate dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public byte[] getLogo() {
        return this.logo;
    }

    public Equipe logo(byte[] logo) {
        this.setLogo(logo);
        return this;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return this.logoContentType;
    }

    public Equipe logoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
        return this;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public TypeSport getTypeSport() {
        return this.typeSport;
    }

    public void setTypeSport(TypeSport typeSport) {
        this.typeSport = typeSport;
    }

    public Equipe typeSport(TypeSport typeSport) {
        this.setTypeSport(typeSport);
        return this;
    }

    public Set<Joueur> getJoueurs() {
        return this.joueurs;
    }

    public void setJoueurs(Set<Joueur> joueurs) {
        if (this.joueurs != null) {
            this.joueurs.forEach(i -> i.setEquipe(null));
        }
        if (joueurs != null) {
            joueurs.forEach(i -> i.setEquipe(this));
        }
        this.joueurs = joueurs;
    }

    public Equipe joueurs(Set<Joueur> joueurs) {
        this.setJoueurs(joueurs);
        return this;
    }

    public Equipe addJoueur(Joueur joueur) {
        this.joueurs.add(joueur);
        joueur.setEquipe(this);
        return this;
    }

    public Equipe removeJoueur(Joueur joueur) {
        this.joueurs.remove(joueur);
        joueur.setEquipe(null);
        return this;
    }

    public Set<Match> getMatches() {
        return this.matches;
    }

    public void setMatches(Set<Match> matches) {
        if (this.matches != null) {
            this.matches.forEach(i -> i.setEquipe(null));
        }
        if (matches != null) {
            matches.forEach(i -> i.setEquipe(this));
        }
        this.matches = matches;
    }

    public Equipe matches(Set<Match> matches) {
        this.setMatches(matches);
        return this;
    }

    public Equipe addMatch(Match match) {
        this.matches.add(match);
        match.setEquipe(this);
        return this;
    }

    public Equipe removeMatch(Match match) {
        this.matches.remove(match);
        match.setEquipe(null);
        return this;
    }

    public Set<ArchiveSport> getArchves() {
        return this.archves;
    }

    public void setArchves(Set<ArchiveSport> archiveSports) {
        if (this.archves != null) {
            this.archves.forEach(i -> i.setEquipe(null));
        }
        if (archiveSports != null) {
            archiveSports.forEach(i -> i.setEquipe(this));
        }
        this.archves = archiveSports;
    }

    public Equipe archves(Set<ArchiveSport> archiveSports) {
        this.setArchves(archiveSports);
        return this;
    }

    public Equipe addArchve(ArchiveSport archiveSport) {
        this.archves.add(archiveSport);
        archiveSport.setEquipe(this);
        return this;
    }

    public Equipe removeArchve(ArchiveSport archiveSport) {
        this.archves.remove(archiveSport);
        archiveSport.setEquipe(null);
        return this;
    }

    public Quartier getQuartier() {
        return this.quartier;
    }

    public void setQuartier(Quartier quartier) {
        this.quartier = quartier;
    }

    public Equipe quartier(Quartier quartier) {
        this.setQuartier(quartier);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Equipe)) {
            return false;
        }
        return id != null && id.equals(((Equipe) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Equipe{" +
            "id=" + getId() +
            ", nomEquipe='" + getNomEquipe() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", logo='" + getLogo() + "'" +
            ", logoContentType='" + getLogoContentType() + "'" +
            "}";
    }
}
