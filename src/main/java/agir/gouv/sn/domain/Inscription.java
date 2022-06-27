package agir.gouv.sn.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Inscription.
 */
@Entity
@Table(name = "inscription")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Inscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_pers")
    private String nomPers;

    @Column(name = "prenom_pers")
    private String prenomPers;

    @Column(name = "email_pers", unique = true)
    private String emailPers;

    @NotNull
    @Column(name = "tel_pers", nullable = false, unique = true)
    private String telPers;

    @Column(name = "tel_1_pers", unique = true)
    private String tel1Pers;

    @Column(name = "etat_inscription")
    private Boolean etatInscription;

    @JsonIgnoreProperties(value = { "artiste" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Evenement evenement;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Inscription id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomPers() {
        return this.nomPers;
    }

    public Inscription nomPers(String nomPers) {
        this.setNomPers(nomPers);
        return this;
    }

    public void setNomPers(String nomPers) {
        this.nomPers = nomPers;
    }

    public String getPrenomPers() {
        return this.prenomPers;
    }

    public Inscription prenomPers(String prenomPers) {
        this.setPrenomPers(prenomPers);
        return this;
    }

    public void setPrenomPers(String prenomPers) {
        this.prenomPers = prenomPers;
    }

    public String getEmailPers() {
        return this.emailPers;
    }

    public Inscription emailPers(String emailPers) {
        this.setEmailPers(emailPers);
        return this;
    }

    public void setEmailPers(String emailPers) {
        this.emailPers = emailPers;
    }

    public String getTelPers() {
        return this.telPers;
    }

    public Inscription telPers(String telPers) {
        this.setTelPers(telPers);
        return this;
    }

    public void setTelPers(String telPers) {
        this.telPers = telPers;
    }

    public String getTel1Pers() {
        return this.tel1Pers;
    }

    public Inscription tel1Pers(String tel1Pers) {
        this.setTel1Pers(tel1Pers);
        return this;
    }

    public void setTel1Pers(String tel1Pers) {
        this.tel1Pers = tel1Pers;
    }

    public Boolean getEtatInscription() {
        return this.etatInscription;
    }

    public Inscription etatInscription(Boolean etatInscription) {
        this.setEtatInscription(etatInscription);
        return this;
    }

    public void setEtatInscription(Boolean etatInscription) {
        this.etatInscription = etatInscription;
    }

    public Evenement getEvenement() {
        return this.evenement;
    }

    public void setEvenement(Evenement evenement) {
        this.evenement = evenement;
    }

    public Inscription evenement(Evenement evenement) {
        this.setEvenement(evenement);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Inscription)) {
            return false;
        }
        return id != null && id.equals(((Inscription) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Inscription{" +
            "id=" + getId() +
            ", nomPers='" + getNomPers() + "'" +
            ", prenomPers='" + getPrenomPers() + "'" +
            ", emailPers='" + getEmailPers() + "'" +
            ", telPers='" + getTelPers() + "'" +
            ", tel1Pers='" + getTel1Pers() + "'" +
            ", etatInscription='" + getEtatInscription() + "'" +
            "}";
    }
}
