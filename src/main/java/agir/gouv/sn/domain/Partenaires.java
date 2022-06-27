package agir.gouv.sn.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Partenaires.
 */
@Entity
@Table(name = "partenaires")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Partenaires implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_partenaire")
    private String nomPartenaire;

    @Column(name = "email_partenaire")
    private String emailPartenaire;

    @Column(name = "tel_partenaire")
    private String telPartenaire;

    @Column(name = "description")
    private String description;

    @JsonIgnoreProperties(value = { "entreprenariat", "domaineActivite" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Entrepreneur entrepreneur;

    @JsonIgnoreProperties(value = { "quartier" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Eleveur eleveur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Partenaires id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomPartenaire() {
        return this.nomPartenaire;
    }

    public Partenaires nomPartenaire(String nomPartenaire) {
        this.setNomPartenaire(nomPartenaire);
        return this;
    }

    public void setNomPartenaire(String nomPartenaire) {
        this.nomPartenaire = nomPartenaire;
    }

    public String getEmailPartenaire() {
        return this.emailPartenaire;
    }

    public Partenaires emailPartenaire(String emailPartenaire) {
        this.setEmailPartenaire(emailPartenaire);
        return this;
    }

    public void setEmailPartenaire(String emailPartenaire) {
        this.emailPartenaire = emailPartenaire;
    }

    public String getTelPartenaire() {
        return this.telPartenaire;
    }

    public Partenaires telPartenaire(String telPartenaire) {
        this.setTelPartenaire(telPartenaire);
        return this;
    }

    public void setTelPartenaire(String telPartenaire) {
        this.telPartenaire = telPartenaire;
    }

    public String getDescription() {
        return this.description;
    }

    public Partenaires description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Entrepreneur getEntrepreneur() {
        return this.entrepreneur;
    }

    public void setEntrepreneur(Entrepreneur entrepreneur) {
        this.entrepreneur = entrepreneur;
    }

    public Partenaires entrepreneur(Entrepreneur entrepreneur) {
        this.setEntrepreneur(entrepreneur);
        return this;
    }

    public Eleveur getEleveur() {
        return this.eleveur;
    }

    public void setEleveur(Eleveur eleveur) {
        this.eleveur = eleveur;
    }

    public Partenaires eleveur(Eleveur eleveur) {
        this.setEleveur(eleveur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Partenaires)) {
            return false;
        }
        return id != null && id.equals(((Partenaires) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Partenaires{" +
            "id=" + getId() +
            ", nomPartenaire='" + getNomPartenaire() + "'" +
            ", emailPartenaire='" + getEmailPartenaire() + "'" +
            ", telPartenaire='" + getTelPartenaire() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
