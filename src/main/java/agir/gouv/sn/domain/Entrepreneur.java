package agir.gouv.sn.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Entrepreneur.
 */
@Entity
@Table(name = "entrepreneur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Entrepreneur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nom_entrepreneur", nullable = false)
    private String nomEntrepreneur;

    @Column(name = "prenom_entrepreneur")
    private String prenomEntrepreneur;

    @Column(name = "email_entrepreneur", unique = true)
    private String emailEntrepreneur;

    @NotNull
    @Column(name = "tel_entrepreneur", nullable = false, unique = true)
    private String telEntrepreneur;

    @Column(name = "tel_1_entrepreneur", unique = true)
    private String tel1Entrepreneur;

    @OneToOne
    @JoinColumn(unique = true)
    private Entreprenariat entreprenariat;

    @OneToOne
    @JoinColumn(unique = true)
    private DomaineActivite domaineActivite;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Entrepreneur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomEntrepreneur() {
        return this.nomEntrepreneur;
    }

    public Entrepreneur nomEntrepreneur(String nomEntrepreneur) {
        this.setNomEntrepreneur(nomEntrepreneur);
        return this;
    }

    public void setNomEntrepreneur(String nomEntrepreneur) {
        this.nomEntrepreneur = nomEntrepreneur;
    }

    public String getPrenomEntrepreneur() {
        return this.prenomEntrepreneur;
    }

    public Entrepreneur prenomEntrepreneur(String prenomEntrepreneur) {
        this.setPrenomEntrepreneur(prenomEntrepreneur);
        return this;
    }

    public void setPrenomEntrepreneur(String prenomEntrepreneur) {
        this.prenomEntrepreneur = prenomEntrepreneur;
    }

    public String getEmailEntrepreneur() {
        return this.emailEntrepreneur;
    }

    public Entrepreneur emailEntrepreneur(String emailEntrepreneur) {
        this.setEmailEntrepreneur(emailEntrepreneur);
        return this;
    }

    public void setEmailEntrepreneur(String emailEntrepreneur) {
        this.emailEntrepreneur = emailEntrepreneur;
    }

    public String getTelEntrepreneur() {
        return this.telEntrepreneur;
    }

    public Entrepreneur telEntrepreneur(String telEntrepreneur) {
        this.setTelEntrepreneur(telEntrepreneur);
        return this;
    }

    public void setTelEntrepreneur(String telEntrepreneur) {
        this.telEntrepreneur = telEntrepreneur;
    }

    public String getTel1Entrepreneur() {
        return this.tel1Entrepreneur;
    }

    public Entrepreneur tel1Entrepreneur(String tel1Entrepreneur) {
        this.setTel1Entrepreneur(tel1Entrepreneur);
        return this;
    }

    public void setTel1Entrepreneur(String tel1Entrepreneur) {
        this.tel1Entrepreneur = tel1Entrepreneur;
    }

    public Entreprenariat getEntreprenariat() {
        return this.entreprenariat;
    }

    public void setEntreprenariat(Entreprenariat entreprenariat) {
        this.entreprenariat = entreprenariat;
    }

    public Entrepreneur entreprenariat(Entreprenariat entreprenariat) {
        this.setEntreprenariat(entreprenariat);
        return this;
    }

    public DomaineActivite getDomaineActivite() {
        return this.domaineActivite;
    }

    public void setDomaineActivite(DomaineActivite domaineActivite) {
        this.domaineActivite = domaineActivite;
    }

    public Entrepreneur domaineActivite(DomaineActivite domaineActivite) {
        this.setDomaineActivite(domaineActivite);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Entrepreneur)) {
            return false;
        }
        return id != null && id.equals(((Entrepreneur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Entrepreneur{" +
            "id=" + getId() +
            ", nomEntrepreneur='" + getNomEntrepreneur() + "'" +
            ", prenomEntrepreneur='" + getPrenomEntrepreneur() + "'" +
            ", emailEntrepreneur='" + getEmailEntrepreneur() + "'" +
            ", telEntrepreneur='" + getTelEntrepreneur() + "'" +
            ", tel1Entrepreneur='" + getTel1Entrepreneur() + "'" +
            "}";
    }
}
