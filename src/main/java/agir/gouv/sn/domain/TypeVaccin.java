package agir.gouv.sn.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TypeVaccin.
 */
@Entity
@Table(name = "type_vaccin")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TypeVaccin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "objectif")
    private LocalDate objectif;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TypeVaccin id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public TypeVaccin libelle(String libelle) {
        this.setLibelle(libelle);
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public LocalDate getObjectif() {
        return this.objectif;
    }

    public TypeVaccin objectif(LocalDate objectif) {
        this.setObjectif(objectif);
        return this;
    }

    public void setObjectif(LocalDate objectif) {
        this.objectif = objectif;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeVaccin)) {
            return false;
        }
        return id != null && id.equals(((TypeVaccin) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeVaccin{" +
            "id=" + getId() +
            ", libelle='" + getLibelle() + "'" +
            ", objectif='" + getObjectif() + "'" +
            "}";
    }
}
