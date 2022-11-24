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
 * A Equipe.
 */
@Entity
@Table(name = "equipe")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Equipe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @JsonIgnoreProperties(value = { "equipe", "club" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private TypeSport typeSport;

    @ManyToOne
    @JsonIgnoreProperties(value = { "equipes" }, allowSetters = true)
    private Joueur joueur;

    @OneToMany(mappedBy = "equipe")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "equipe", "club", "beneficiaire" }, allowSetters = true)
    private Set<Quartier> quartiers = new HashSet<>();

    @ManyToMany(mappedBy = "equipes")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "equipes" }, allowSetters = true)
    private Set<Match> matches = new HashSet<>();

    @ManyToMany(mappedBy = "equipes")
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

    public Equipe id(Long id) {
        this.id = id;
        return this;
    }

    public String getNomEquipe() {
        return this.nomEquipe;
    }

    public Equipe nomEquipe(String nomEquipe) {
        this.nomEquipe = nomEquipe;
        return this;
    }

    public void setNomEquipe(String nomEquipe) {
        this.nomEquipe = nomEquipe;
    }

    public LocalDate getDateCreation() {
        return this.dateCreation;
    }

    public Equipe dateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
        return this;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public byte[] getLogo() {
        return this.logo;
    }

    public Equipe logo(byte[] logo) {
        this.logo = logo;
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

    public Equipe typeSport(TypeSport typeSport) {
        this.setTypeSport(typeSport);
        return this;
    }

    public void setTypeSport(TypeSport typeSport) {
        this.typeSport = typeSport;
    }

    public Joueur getJoueur() {
        return this.joueur;
    }

    public Equipe joueur(Joueur joueur) {
        this.setJoueur(joueur);
        return this;
    }

    public void setJoueur(Joueur joueur) {
        this.joueur = joueur;
    }

    public Set<Quartier> getQuartiers() {
        return this.quartiers;
    }

    public Equipe quartiers(Set<Quartier> quartiers) {
        this.setQuartiers(quartiers);
        return this;
    }

    public Equipe addQuartier(Quartier quartier) {
        this.quartiers.add(quartier);
        quartier.setEquipe(this);
        return this;
    }

    public Equipe removeQuartier(Quartier quartier) {
        this.quartiers.remove(quartier);
        quartier.setEquipe(null);
        return this;
    }

    public void setQuartiers(Set<Quartier> quartiers) {
        if (this.quartiers != null) {
            this.quartiers.forEach(i -> i.setEquipe(null));
        }
        if (quartiers != null) {
            quartiers.forEach(i -> i.setEquipe(this));
        }
        this.quartiers = quartiers;
    }

    public Set<Match> getMatches() {
        return this.matches;
    }

    public Equipe matches(Set<Match> matches) {
        this.setMatches(matches);
        return this;
    }

    public Equipe addMatch(Match match) {
        this.matches.add(match);
        match.getEquipes().add(this);
        return this;
    }

    public Equipe removeMatch(Match match) {
        this.matches.remove(match);
        match.getEquipes().remove(this);
        return this;
    }

    public void setMatches(Set<Match> matches) {
        if (this.matches != null) {
            this.matches.forEach(i -> i.removeEquipe(this));
        }
        if (matches != null) {
            matches.forEach(i -> i.addEquipe(this));
        }
        this.matches = matches;
    }

    public Set<ArchiveSport> getArchves() {
        return this.archves;
    }

    public Equipe archves(Set<ArchiveSport> archiveSports) {
        this.setArchves(archiveSports);
        return this;
    }

    public Equipe addArchve(ArchiveSport archiveSport) {
        this.archves.add(archiveSport);
        archiveSport.getEquipes().add(this);
        return this;
    }

    public Equipe removeArchve(ArchiveSport archiveSport) {
        this.archves.remove(archiveSport);
        archiveSport.getEquipes().remove(this);
        return this;
    }

    public void setArchves(Set<ArchiveSport> archiveSports) {
        if (this.archves != null) {
            this.archves.forEach(i -> i.removeEquipe(this));
        }
        if (archiveSports != null) {
            archiveSports.forEach(i -> i.addEquipe(this));
        }
        this.archves = archiveSports;
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
