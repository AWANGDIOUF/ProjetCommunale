package sn.projet.communal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.projet.communal.domain.enumeration.Poste;

/**
 * A Joueur.
 */
@Entity
@Table(name = "joueur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Joueur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "date_nais")
    private LocalDate dateNais;

    @Column(name = "lieu_nais")
    private String lieuNais;

    @Enumerated(EnumType.STRING)
    @Column(name = "poste")
    private Poste poste;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "photo_content_type")
    private String photoContentType;

    @OneToMany(mappedBy = "joueur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "typeSport", "joueur", "quartiers", "matches", "archves" }, allowSetters = true)
    private Set<Equipe> equipes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Joueur id(Long id) {
        this.id = id;
        return this;
    }

    public String getNom() {
        return this.nom;
    }

    public Joueur nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Joueur prenom(String prenom) {
        this.prenom = prenom;
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public LocalDate getDateNais() {
        return this.dateNais;
    }

    public Joueur dateNais(LocalDate dateNais) {
        this.dateNais = dateNais;
        return this;
    }

    public void setDateNais(LocalDate dateNais) {
        this.dateNais = dateNais;
    }

    public String getLieuNais() {
        return this.lieuNais;
    }

    public Joueur lieuNais(String lieuNais) {
        this.lieuNais = lieuNais;
        return this;
    }

    public void setLieuNais(String lieuNais) {
        this.lieuNais = lieuNais;
    }

    public Poste getPoste() {
        return this.poste;
    }

    public Joueur poste(Poste poste) {
        this.poste = poste;
        return this;
    }

    public void setPoste(Poste poste) {
        this.poste = poste;
    }

    public byte[] getPhoto() {
        return this.photo;
    }

    public Joueur photo(byte[] photo) {
        this.photo = photo;
        return this;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return this.photoContentType;
    }

    public Joueur photoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
        return this;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public Set<Equipe> getEquipes() {
        return this.equipes;
    }

    public Joueur equipes(Set<Equipe> equipes) {
        this.setEquipes(equipes);
        return this;
    }

    public Joueur addEquipe(Equipe equipe) {
        this.equipes.add(equipe);
        equipe.setJoueur(this);
        return this;
    }

    public Joueur removeEquipe(Equipe equipe) {
        this.equipes.remove(equipe);
        equipe.setJoueur(null);
        return this;
    }

    public void setEquipes(Set<Equipe> equipes) {
        if (this.equipes != null) {
            this.equipes.forEach(i -> i.setJoueur(null));
        }
        if (equipes != null) {
            equipes.forEach(i -> i.setJoueur(this));
        }
        this.equipes = equipes;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Joueur)) {
            return false;
        }
        return id != null && id.equals(((Joueur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Joueur{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", dateNais='" + getDateNais() + "'" +
            ", lieuNais='" + getLieuNais() + "'" +
            ", poste='" + getPoste() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", photoContentType='" + getPhotoContentType() + "'" +
            "}";
    }
}
