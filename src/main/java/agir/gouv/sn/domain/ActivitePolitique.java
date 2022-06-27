package agir.gouv.sn.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ActivitePolitique.
 */
@Entity
@Table(name = "activite_politique")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ActivitePolitique implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "titre_activite")
    private String titreActivite;

    @Lob
    @Column(name = "description_activite")
    private String descriptionActivite;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ActivitePolitique id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitreActivite() {
        return this.titreActivite;
    }

    public ActivitePolitique titreActivite(String titreActivite) {
        this.setTitreActivite(titreActivite);
        return this;
    }

    public void setTitreActivite(String titreActivite) {
        this.titreActivite = titreActivite;
    }

    public String getDescriptionActivite() {
        return this.descriptionActivite;
    }

    public ActivitePolitique descriptionActivite(String descriptionActivite) {
        this.setDescriptionActivite(descriptionActivite);
        return this;
    }

    public void setDescriptionActivite(String descriptionActivite) {
        this.descriptionActivite = descriptionActivite;
    }

    public LocalDate getDateDebut() {
        return this.dateDebut;
    }

    public ActivitePolitique dateDebut(LocalDate dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return this.dateFin;
    }

    public ActivitePolitique dateFin(LocalDate dateFin) {
        this.setDateFin(dateFin);
        return this;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ActivitePolitique)) {
            return false;
        }
        return id != null && id.equals(((ActivitePolitique) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ActivitePolitique{" +
            "id=" + getId() +
            ", titreActivite='" + getTitreActivite() + "'" +
            ", descriptionActivite='" + getDescriptionActivite() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            "}";
    }
}
