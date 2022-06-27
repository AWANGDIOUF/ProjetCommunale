package agir.gouv.sn.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DemandeInterview.
 */
@Entity
@Table(name = "demande_interview")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DemandeInterview implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_journaliste")
    private String nomJournaliste;

    @Column(name = "prenom_journaliste")
    private String prenomJournaliste;

    @NotNull
    @Column(name = "nom_societe", nullable = false)
    private String nomSociete;

    @NotNull
    @Column(name = "email_journalite", nullable = false, unique = true)
    private String emailJournalite;

    @Column(name = "date_interview")
    private LocalDate dateInterview;

    @Column(name = "etat_demande")
    private Boolean etatDemande;

    @JsonIgnoreProperties(value = { "entreprenariat", "domaineActivite" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Entrepreneur entrepreneur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DemandeInterview id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomJournaliste() {
        return this.nomJournaliste;
    }

    public DemandeInterview nomJournaliste(String nomJournaliste) {
        this.setNomJournaliste(nomJournaliste);
        return this;
    }

    public void setNomJournaliste(String nomJournaliste) {
        this.nomJournaliste = nomJournaliste;
    }

    public String getPrenomJournaliste() {
        return this.prenomJournaliste;
    }

    public DemandeInterview prenomJournaliste(String prenomJournaliste) {
        this.setPrenomJournaliste(prenomJournaliste);
        return this;
    }

    public void setPrenomJournaliste(String prenomJournaliste) {
        this.prenomJournaliste = prenomJournaliste;
    }

    public String getNomSociete() {
        return this.nomSociete;
    }

    public DemandeInterview nomSociete(String nomSociete) {
        this.setNomSociete(nomSociete);
        return this;
    }

    public void setNomSociete(String nomSociete) {
        this.nomSociete = nomSociete;
    }

    public String getEmailJournalite() {
        return this.emailJournalite;
    }

    public DemandeInterview emailJournalite(String emailJournalite) {
        this.setEmailJournalite(emailJournalite);
        return this;
    }

    public void setEmailJournalite(String emailJournalite) {
        this.emailJournalite = emailJournalite;
    }

    public LocalDate getDateInterview() {
        return this.dateInterview;
    }

    public DemandeInterview dateInterview(LocalDate dateInterview) {
        this.setDateInterview(dateInterview);
        return this;
    }

    public void setDateInterview(LocalDate dateInterview) {
        this.dateInterview = dateInterview;
    }

    public Boolean getEtatDemande() {
        return this.etatDemande;
    }

    public DemandeInterview etatDemande(Boolean etatDemande) {
        this.setEtatDemande(etatDemande);
        return this;
    }

    public void setEtatDemande(Boolean etatDemande) {
        this.etatDemande = etatDemande;
    }

    public Entrepreneur getEntrepreneur() {
        return this.entrepreneur;
    }

    public void setEntrepreneur(Entrepreneur entrepreneur) {
        this.entrepreneur = entrepreneur;
    }

    public DemandeInterview entrepreneur(Entrepreneur entrepreneur) {
        this.setEntrepreneur(entrepreneur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DemandeInterview)) {
            return false;
        }
        return id != null && id.equals(((DemandeInterview) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DemandeInterview{" +
            "id=" + getId() +
            ", nomJournaliste='" + getNomJournaliste() + "'" +
            ", prenomJournaliste='" + getPrenomJournaliste() + "'" +
            ", nomSociete='" + getNomSociete() + "'" +
            ", emailJournalite='" + getEmailJournalite() + "'" +
            ", dateInterview='" + getDateInterview() + "'" +
            ", etatDemande='" + getEtatDemande() + "'" +
            "}";
    }
}
