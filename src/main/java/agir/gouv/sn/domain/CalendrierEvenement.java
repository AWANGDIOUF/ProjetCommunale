package agir.gouv.sn.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CalendrierEvenement.
 */
@Entity
@Table(name = "calendrier_evenement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CalendrierEvenement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_eve")
    private String nomEve;

    @Lob
    @Column(name = "but")
    private String but;

    @Lob
    @Column(name = "objectif")
    private String objectif;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "lieu")
    private String lieu;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CalendrierEvenement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomEve() {
        return this.nomEve;
    }

    public CalendrierEvenement nomEve(String nomEve) {
        this.setNomEve(nomEve);
        return this;
    }

    public void setNomEve(String nomEve) {
        this.nomEve = nomEve;
    }

    public String getBut() {
        return this.but;
    }

    public CalendrierEvenement but(String but) {
        this.setBut(but);
        return this;
    }

    public void setBut(String but) {
        this.but = but;
    }

    public String getObjectif() {
        return this.objectif;
    }

    public CalendrierEvenement objectif(String objectif) {
        this.setObjectif(objectif);
        return this;
    }

    public void setObjectif(String objectif) {
        this.objectif = objectif;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public CalendrierEvenement date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getLieu() {
        return this.lieu;
    }

    public CalendrierEvenement lieu(String lieu) {
        this.setLieu(lieu);
        return this;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CalendrierEvenement)) {
            return false;
        }
        return id != null && id.equals(((CalendrierEvenement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CalendrierEvenement{" +
            "id=" + getId() +
            ", nomEve='" + getNomEve() + "'" +
            ", but='" + getBut() + "'" +
            ", objectif='" + getObjectif() + "'" +
            ", date='" + getDate() + "'" +
            ", lieu='" + getLieu() + "'" +
            "}";
    }
}
