package agir.gouv.sn.domain;

import agir.gouv.sn.domain.enumeration.Poste;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_joueur")
    private String nomJoueur;

    @Column(name = "prenom_joueur")
    private String prenomJoueur;

    @Column(name = "date_nais_joueur")
    private LocalDate dateNaisJoueur;

    @Column(name = "lieu_nais_joueur")
    private String lieuNaisJoueur;

    @Enumerated(EnumType.STRING)
    @Column(name = "poste_joueur")
    private Poste posteJoueur;

    @Lob
    @Column(name = "photo_joueur")
    private byte[] photoJoueur;

    @Column(name = "photo_joueur_content_type")
    private String photoJoueurContentType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "typeSport", "joueurs", "matches", "archves", "quartier" }, allowSetters = true)
    private Equipe equipe;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Joueur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomJoueur() {
        return this.nomJoueur;
    }

    public Joueur nomJoueur(String nomJoueur) {
        this.setNomJoueur(nomJoueur);
        return this;
    }

    public void setNomJoueur(String nomJoueur) {
        this.nomJoueur = nomJoueur;
    }

    public String getPrenomJoueur() {
        return this.prenomJoueur;
    }

    public Joueur prenomJoueur(String prenomJoueur) {
        this.setPrenomJoueur(prenomJoueur);
        return this;
    }

    public void setPrenomJoueur(String prenomJoueur) {
        this.prenomJoueur = prenomJoueur;
    }

    public LocalDate getDateNaisJoueur() {
        return this.dateNaisJoueur;
    }

    public Joueur dateNaisJoueur(LocalDate dateNaisJoueur) {
        this.setDateNaisJoueur(dateNaisJoueur);
        return this;
    }

    public void setDateNaisJoueur(LocalDate dateNaisJoueur) {
        this.dateNaisJoueur = dateNaisJoueur;
    }

    public String getLieuNaisJoueur() {
        return this.lieuNaisJoueur;
    }

    public Joueur lieuNaisJoueur(String lieuNaisJoueur) {
        this.setLieuNaisJoueur(lieuNaisJoueur);
        return this;
    }

    public void setLieuNaisJoueur(String lieuNaisJoueur) {
        this.lieuNaisJoueur = lieuNaisJoueur;
    }

    public Poste getPosteJoueur() {
        return this.posteJoueur;
    }

    public Joueur posteJoueur(Poste posteJoueur) {
        this.setPosteJoueur(posteJoueur);
        return this;
    }

    public void setPosteJoueur(Poste posteJoueur) {
        this.posteJoueur = posteJoueur;
    }

    public byte[] getPhotoJoueur() {
        return this.photoJoueur;
    }

    public Joueur photoJoueur(byte[] photoJoueur) {
        this.setPhotoJoueur(photoJoueur);
        return this;
    }

    public void setPhotoJoueur(byte[] photoJoueur) {
        this.photoJoueur = photoJoueur;
    }

    public String getPhotoJoueurContentType() {
        return this.photoJoueurContentType;
    }

    public Joueur photoJoueurContentType(String photoJoueurContentType) {
        this.photoJoueurContentType = photoJoueurContentType;
        return this;
    }

    public void setPhotoJoueurContentType(String photoJoueurContentType) {
        this.photoJoueurContentType = photoJoueurContentType;
    }

    public Equipe getEquipe() {
        return this.equipe;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }

    public Joueur equipe(Equipe equipe) {
        this.setEquipe(equipe);
        return this;
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
            ", nomJoueur='" + getNomJoueur() + "'" +
            ", prenomJoueur='" + getPrenomJoueur() + "'" +
            ", dateNaisJoueur='" + getDateNaisJoueur() + "'" +
            ", lieuNaisJoueur='" + getLieuNaisJoueur() + "'" +
            ", posteJoueur='" + getPosteJoueur() + "'" +
            ", photoJoueur='" + getPhotoJoueur() + "'" +
            ", photoJoueurContentType='" + getPhotoJoueurContentType() + "'" +
            "}";
    }
}
