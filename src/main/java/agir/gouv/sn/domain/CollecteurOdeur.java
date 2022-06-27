package agir.gouv.sn.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CollecteurOdeur.
 */
@Entity
@Table(name = "collecteur_odeur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CollecteurOdeur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_collecteur")
    private String nomCollecteur;

    @Column(name = "prenom_collecteur")
    private String prenomCollecteur;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "tel_1")
    private String tel1;

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

    public CollecteurOdeur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomCollecteur() {
        return this.nomCollecteur;
    }

    public CollecteurOdeur nomCollecteur(String nomCollecteur) {
        this.setNomCollecteur(nomCollecteur);
        return this;
    }

    public void setNomCollecteur(String nomCollecteur) {
        this.nomCollecteur = nomCollecteur;
    }

    public String getPrenomCollecteur() {
        return this.prenomCollecteur;
    }

    public CollecteurOdeur prenomCollecteur(String prenomCollecteur) {
        this.setPrenomCollecteur(prenomCollecteur);
        return this;
    }

    public void setPrenomCollecteur(String prenomCollecteur) {
        this.prenomCollecteur = prenomCollecteur;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public CollecteurOdeur date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTel1() {
        return this.tel1;
    }

    public CollecteurOdeur tel1(String tel1) {
        this.setTel1(tel1);
        return this;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    public Quartier getQuartier() {
        return this.quartier;
    }

    public void setQuartier(Quartier quartier) {
        this.quartier = quartier;
    }

    public CollecteurOdeur quartier(Quartier quartier) {
        this.setQuartier(quartier);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CollecteurOdeur)) {
            return false;
        }
        return id != null && id.equals(((CollecteurOdeur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CollecteurOdeur{" +
            "id=" + getId() +
            ", nomCollecteur='" + getNomCollecteur() + "'" +
            ", prenomCollecteur='" + getPrenomCollecteur() + "'" +
            ", date='" + getDate() + "'" +
            ", tel1='" + getTel1() + "'" +
            "}";
    }
}
