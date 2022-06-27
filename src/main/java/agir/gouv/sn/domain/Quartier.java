package agir.gouv.sn.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Quartier.
 */
@Entity
@Table(name = "quartier")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Quartier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_quartier")
    private String nomQuartier;

    @OneToOne
    @JoinColumn(unique = true)
    private Artiste artiste;

    @OneToMany(mappedBy = "quartier")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "typeSport", "joueurs", "matches", "archves", "quartier" }, allowSetters = true)
    private Set<Equipe> equipes = new HashSet<>();

    @OneToMany(mappedBy = "quartier")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "typeSport", "combattants", "competitions", "archves", "quartier" }, allowSetters = true)
    private Set<Club> clubs = new HashSet<>();

    @OneToMany(mappedBy = "quartier")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "annonces", "quartier" }, allowSetters = true)
    private Set<Beneficiaire> beneficiaires = new HashSet<>();

    @OneToMany(mappedBy = "quartier")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "quartier" }, allowSetters = true)
    private Set<CollecteurOdeur> collecteurOdeurs = new HashSet<>();

    @OneToMany(mappedBy = "quartier")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "quartier" }, allowSetters = true)
    private Set<Vidange> vidanges = new HashSet<>();

    @OneToMany(mappedBy = "quartier")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "quartier" }, allowSetters = true)
    private Set<RecuperationRecyclable> recuperationRecyclables = new HashSet<>();

    @OneToMany(mappedBy = "quartier")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ensegnants", "resultatExamen", "quartier" }, allowSetters = true)
    private Set<Etablissement> etablissements = new HashSet<>();

    @OneToMany(mappedBy = "quartier")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "quartier" }, allowSetters = true)
    private Set<Eleveur> eleveurs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Quartier id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomQuartier() {
        return this.nomQuartier;
    }

    public Quartier nomQuartier(String nomQuartier) {
        this.setNomQuartier(nomQuartier);
        return this;
    }

    public void setNomQuartier(String nomQuartier) {
        this.nomQuartier = nomQuartier;
    }

    public Artiste getArtiste() {
        return this.artiste;
    }

    public void setArtiste(Artiste artiste) {
        this.artiste = artiste;
    }

    public Quartier artiste(Artiste artiste) {
        this.setArtiste(artiste);
        return this;
    }

    public Set<Equipe> getEquipes() {
        return this.equipes;
    }

    public void setEquipes(Set<Equipe> equipes) {
        if (this.equipes != null) {
            this.equipes.forEach(i -> i.setQuartier(null));
        }
        if (equipes != null) {
            equipes.forEach(i -> i.setQuartier(this));
        }
        this.equipes = equipes;
    }

    public Quartier equipes(Set<Equipe> equipes) {
        this.setEquipes(equipes);
        return this;
    }

    public Quartier addEquipe(Equipe equipe) {
        this.equipes.add(equipe);
        equipe.setQuartier(this);
        return this;
    }

    public Quartier removeEquipe(Equipe equipe) {
        this.equipes.remove(equipe);
        equipe.setQuartier(null);
        return this;
    }

    public Set<Club> getClubs() {
        return this.clubs;
    }

    public void setClubs(Set<Club> clubs) {
        if (this.clubs != null) {
            this.clubs.forEach(i -> i.setQuartier(null));
        }
        if (clubs != null) {
            clubs.forEach(i -> i.setQuartier(this));
        }
        this.clubs = clubs;
    }

    public Quartier clubs(Set<Club> clubs) {
        this.setClubs(clubs);
        return this;
    }

    public Quartier addClub(Club club) {
        this.clubs.add(club);
        club.setQuartier(this);
        return this;
    }

    public Quartier removeClub(Club club) {
        this.clubs.remove(club);
        club.setQuartier(null);
        return this;
    }

    public Set<Beneficiaire> getBeneficiaires() {
        return this.beneficiaires;
    }

    public void setBeneficiaires(Set<Beneficiaire> beneficiaires) {
        if (this.beneficiaires != null) {
            this.beneficiaires.forEach(i -> i.setQuartier(null));
        }
        if (beneficiaires != null) {
            beneficiaires.forEach(i -> i.setQuartier(this));
        }
        this.beneficiaires = beneficiaires;
    }

    public Quartier beneficiaires(Set<Beneficiaire> beneficiaires) {
        this.setBeneficiaires(beneficiaires);
        return this;
    }

    public Quartier addBeneficiaire(Beneficiaire beneficiaire) {
        this.beneficiaires.add(beneficiaire);
        beneficiaire.setQuartier(this);
        return this;
    }

    public Quartier removeBeneficiaire(Beneficiaire beneficiaire) {
        this.beneficiaires.remove(beneficiaire);
        beneficiaire.setQuartier(null);
        return this;
    }

    public Set<CollecteurOdeur> getCollecteurOdeurs() {
        return this.collecteurOdeurs;
    }

    public void setCollecteurOdeurs(Set<CollecteurOdeur> collecteurOdeurs) {
        if (this.collecteurOdeurs != null) {
            this.collecteurOdeurs.forEach(i -> i.setQuartier(null));
        }
        if (collecteurOdeurs != null) {
            collecteurOdeurs.forEach(i -> i.setQuartier(this));
        }
        this.collecteurOdeurs = collecteurOdeurs;
    }

    public Quartier collecteurOdeurs(Set<CollecteurOdeur> collecteurOdeurs) {
        this.setCollecteurOdeurs(collecteurOdeurs);
        return this;
    }

    public Quartier addCollecteurOdeur(CollecteurOdeur collecteurOdeur) {
        this.collecteurOdeurs.add(collecteurOdeur);
        collecteurOdeur.setQuartier(this);
        return this;
    }

    public Quartier removeCollecteurOdeur(CollecteurOdeur collecteurOdeur) {
        this.collecteurOdeurs.remove(collecteurOdeur);
        collecteurOdeur.setQuartier(null);
        return this;
    }

    public Set<Vidange> getVidanges() {
        return this.vidanges;
    }

    public void setVidanges(Set<Vidange> vidanges) {
        if (this.vidanges != null) {
            this.vidanges.forEach(i -> i.setQuartier(null));
        }
        if (vidanges != null) {
            vidanges.forEach(i -> i.setQuartier(this));
        }
        this.vidanges = vidanges;
    }

    public Quartier vidanges(Set<Vidange> vidanges) {
        this.setVidanges(vidanges);
        return this;
    }

    public Quartier addVidange(Vidange vidange) {
        this.vidanges.add(vidange);
        vidange.setQuartier(this);
        return this;
    }

    public Quartier removeVidange(Vidange vidange) {
        this.vidanges.remove(vidange);
        vidange.setQuartier(null);
        return this;
    }

    public Set<RecuperationRecyclable> getRecuperationRecyclables() {
        return this.recuperationRecyclables;
    }

    public void setRecuperationRecyclables(Set<RecuperationRecyclable> recuperationRecyclables) {
        if (this.recuperationRecyclables != null) {
            this.recuperationRecyclables.forEach(i -> i.setQuartier(null));
        }
        if (recuperationRecyclables != null) {
            recuperationRecyclables.forEach(i -> i.setQuartier(this));
        }
        this.recuperationRecyclables = recuperationRecyclables;
    }

    public Quartier recuperationRecyclables(Set<RecuperationRecyclable> recuperationRecyclables) {
        this.setRecuperationRecyclables(recuperationRecyclables);
        return this;
    }

    public Quartier addRecuperationRecyclable(RecuperationRecyclable recuperationRecyclable) {
        this.recuperationRecyclables.add(recuperationRecyclable);
        recuperationRecyclable.setQuartier(this);
        return this;
    }

    public Quartier removeRecuperationRecyclable(RecuperationRecyclable recuperationRecyclable) {
        this.recuperationRecyclables.remove(recuperationRecyclable);
        recuperationRecyclable.setQuartier(null);
        return this;
    }

    public Set<Etablissement> getEtablissements() {
        return this.etablissements;
    }

    public void setEtablissements(Set<Etablissement> etablissements) {
        if (this.etablissements != null) {
            this.etablissements.forEach(i -> i.setQuartier(null));
        }
        if (etablissements != null) {
            etablissements.forEach(i -> i.setQuartier(this));
        }
        this.etablissements = etablissements;
    }

    public Quartier etablissements(Set<Etablissement> etablissements) {
        this.setEtablissements(etablissements);
        return this;
    }

    public Quartier addEtablissement(Etablissement etablissement) {
        this.etablissements.add(etablissement);
        etablissement.setQuartier(this);
        return this;
    }

    public Quartier removeEtablissement(Etablissement etablissement) {
        this.etablissements.remove(etablissement);
        etablissement.setQuartier(null);
        return this;
    }

    public Set<Eleveur> getEleveurs() {
        return this.eleveurs;
    }

    public void setEleveurs(Set<Eleveur> eleveurs) {
        if (this.eleveurs != null) {
            this.eleveurs.forEach(i -> i.setQuartier(null));
        }
        if (eleveurs != null) {
            eleveurs.forEach(i -> i.setQuartier(this));
        }
        this.eleveurs = eleveurs;
    }

    public Quartier eleveurs(Set<Eleveur> eleveurs) {
        this.setEleveurs(eleveurs);
        return this;
    }

    public Quartier addEleveur(Eleveur eleveur) {
        this.eleveurs.add(eleveur);
        eleveur.setQuartier(this);
        return this;
    }

    public Quartier removeEleveur(Eleveur eleveur) {
        this.eleveurs.remove(eleveur);
        eleveur.setQuartier(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Quartier)) {
            return false;
        }
        return id != null && id.equals(((Quartier) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Quartier{" +
            "id=" + getId() +
            ", nomQuartier='" + getNomQuartier() + "'" +
            "}";
    }
}
