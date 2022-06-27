package agir.gouv.sn.domain;

import agir.gouv.sn.domain.enumeration.TypeElevage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Eleveur.
 */
@Entity
@Table(name = "eleveur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Eleveur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_eleveur")
    private String nomEleveur;

    @Column(name = "prenom_eleveur")
    private String prenomEleveur;

    @Column(name = "tel_eleveur")
    private String telEleveur;

    @Column(name = "tel_1_eleveur")
    private String tel1Eleveur;

    @Column(name = "adresse")
    private String adresse;

    @Enumerated(EnumType.STRING)
    @Column(name = "nom_elevage")
    private TypeElevage nomElevage;

    @Column(name = "description_activite")
    private String descriptionActivite;

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

    public Eleveur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomEleveur() {
        return this.nomEleveur;
    }

    public Eleveur nomEleveur(String nomEleveur) {
        this.setNomEleveur(nomEleveur);
        return this;
    }

    public void setNomEleveur(String nomEleveur) {
        this.nomEleveur = nomEleveur;
    }

    public String getPrenomEleveur() {
        return this.prenomEleveur;
    }

    public Eleveur prenomEleveur(String prenomEleveur) {
        this.setPrenomEleveur(prenomEleveur);
        return this;
    }

    public void setPrenomEleveur(String prenomEleveur) {
        this.prenomEleveur = prenomEleveur;
    }

    public String getTelEleveur() {
        return this.telEleveur;
    }

    public Eleveur telEleveur(String telEleveur) {
        this.setTelEleveur(telEleveur);
        return this;
    }

    public void setTelEleveur(String telEleveur) {
        this.telEleveur = telEleveur;
    }

    public String getTel1Eleveur() {
        return this.tel1Eleveur;
    }

    public Eleveur tel1Eleveur(String tel1Eleveur) {
        this.setTel1Eleveur(tel1Eleveur);
        return this;
    }

    public void setTel1Eleveur(String tel1Eleveur) {
        this.tel1Eleveur = tel1Eleveur;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Eleveur adresse(String adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public TypeElevage getNomElevage() {
        return this.nomElevage;
    }

    public Eleveur nomElevage(TypeElevage nomElevage) {
        this.setNomElevage(nomElevage);
        return this;
    }

    public void setNomElevage(TypeElevage nomElevage) {
        this.nomElevage = nomElevage;
    }

    public String getDescriptionActivite() {
        return this.descriptionActivite;
    }

    public Eleveur descriptionActivite(String descriptionActivite) {
        this.setDescriptionActivite(descriptionActivite);
        return this;
    }

    public void setDescriptionActivite(String descriptionActivite) {
        this.descriptionActivite = descriptionActivite;
    }

    public Quartier getQuartier() {
        return this.quartier;
    }

    public void setQuartier(Quartier quartier) {
        this.quartier = quartier;
    }

    public Eleveur quartier(Quartier quartier) {
        this.setQuartier(quartier);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Eleveur)) {
            return false;
        }
        return id != null && id.equals(((Eleveur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Eleveur{" +
            "id=" + getId() +
            ", nomEleveur='" + getNomEleveur() + "'" +
            ", prenomEleveur='" + getPrenomEleveur() + "'" +
            ", telEleveur='" + getTelEleveur() + "'" +
            ", tel1Eleveur='" + getTel1Eleveur() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", nomElevage='" + getNomElevage() + "'" +
            ", descriptionActivite='" + getDescriptionActivite() + "'" +
            "}";
    }
}
