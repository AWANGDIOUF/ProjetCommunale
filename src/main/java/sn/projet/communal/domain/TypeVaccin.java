package sn.projet.communal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
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
    private Long id;

    @Column(name = "libelle")
    private String libelle;

    @JsonIgnoreProperties(value = { "annonce", "typeVaccin", "cibles" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Vaccination vaccination;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeVaccin id(Long id) {
        this.id = id;
        return this;
    }

    public String getLibelle() {
        return this.libelle;
    }

    public TypeVaccin libelle(String libelle) {
        this.libelle = libelle;
        return this;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Vaccination getVaccination() {
        return this.vaccination;
    }

    public TypeVaccin vaccination(Vaccination vaccination) {
        this.setVaccination(vaccination);
        return this;
    }

    public void setVaccination(Vaccination vaccination) {
        this.vaccination = vaccination;
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
            "}";
    }
}
