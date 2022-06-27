package agir.gouv.sn.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Evenement.
 */
@Entity
@Table(name = "evenement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Evenement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_evenement")
    private String nomEvenement;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "action")
    private String action;

    @Column(name = "decision")
    private String decision;

    @Column(name = "delai_instruction")
    private ZonedDateTime delaiInstruction;

    @Column(name = "delai_inscription")
    private ZonedDateTime delaiInscription;

    @Column(name = "delai_validation")
    private ZonedDateTime delaiValidation;

    @OneToOne
    @JoinColumn(unique = true)
    private Artiste artiste;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Evenement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomEvenement() {
        return this.nomEvenement;
    }

    public Evenement nomEvenement(String nomEvenement) {
        this.setNomEvenement(nomEvenement);
        return this;
    }

    public void setNomEvenement(String nomEvenement) {
        this.nomEvenement = nomEvenement;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public Evenement libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getAction() {
        return this.action;
    }

    public Evenement action(String action) {
        this.setAction(action);
        return this;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDecision() {
        return this.decision;
    }

    public Evenement decision(String decision) {
        this.setDecision(decision);
        return this;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public ZonedDateTime getDelaiInstruction() {
        return this.delaiInstruction;
    }

    public Evenement delaiInstruction(ZonedDateTime delaiInstruction) {
        this.setDelaiInstruction(delaiInstruction);
        return this;
    }

    public void setDelaiInstruction(ZonedDateTime delaiInstruction) {
        this.delaiInstruction = delaiInstruction;
    }

    public ZonedDateTime getDelaiInscription() {
        return this.delaiInscription;
    }

    public Evenement delaiInscription(ZonedDateTime delaiInscription) {
        this.setDelaiInscription(delaiInscription);
        return this;
    }

    public void setDelaiInscription(ZonedDateTime delaiInscription) {
        this.delaiInscription = delaiInscription;
    }

    public ZonedDateTime getDelaiValidation() {
        return this.delaiValidation;
    }

    public Evenement delaiValidation(ZonedDateTime delaiValidation) {
        this.setDelaiValidation(delaiValidation);
        return this;
    }

    public void setDelaiValidation(ZonedDateTime delaiValidation) {
        this.delaiValidation = delaiValidation;
    }

    public Artiste getArtiste() {
        return this.artiste;
    }

    public void setArtiste(Artiste artiste) {
        this.artiste = artiste;
    }

    public Evenement artiste(Artiste artiste) {
        this.setArtiste(artiste);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Evenement)) {
            return false;
        }
        return id != null && id.equals(((Evenement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Evenement{" +
            "id=" + getId() +
            ", nomEvenement='" + getNomEvenement() + "'" +
            ", libelle='" + getLibelle() + "'" +
            ", action='" + getAction() + "'" +
            ", decision='" + getDecision() + "'" +
            ", delaiInstruction='" + getDelaiInstruction() + "'" +
            ", delaiInscription='" + getDelaiInscription() + "'" +
            ", delaiValidation='" + getDelaiValidation() + "'" +
            "}";
    }
}
