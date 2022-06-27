package agir.gouv.sn.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A RecuperationRecyclable.
 */
@Entity
@Table(name = "recuperation_recyclable")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class RecuperationRecyclable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "lieu")
    private String lieu;

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

    public RecuperationRecyclable id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public RecuperationRecyclable nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public RecuperationRecyclable date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getLieu() {
        return this.lieu;
    }

    public RecuperationRecyclable lieu(String lieu) {
        this.setLieu(lieu);
        return this;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public Quartier getQuartier() {
        return this.quartier;
    }

    public void setQuartier(Quartier quartier) {
        this.quartier = quartier;
    }

    public RecuperationRecyclable quartier(Quartier quartier) {
        this.setQuartier(quartier);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RecuperationRecyclable)) {
            return false;
        }
        return id != null && id.equals(((RecuperationRecyclable) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RecuperationRecyclable{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", date='" + getDate() + "'" +
            ", lieu='" + getLieu() + "'" +
            "}";
    }
}
