package agir.gouv.sn.domain;

import agir.gouv.sn.domain.enumeration.CibleVacc;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Cible.
 */
@Entity
@Table(name = "cible")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Cible implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "cible")
    private CibleVacc cible;

    @Column(name = "age")
    private Long age;

    @ManyToOne
    @JsonIgnoreProperties(value = { "typeVaccin", "annonce", "cibles" }, allowSetters = true)
    private Vaccination vaccination;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cible id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CibleVacc getCible() {
        return this.cible;
    }

    public Cible cible(CibleVacc cible) {
        this.setCible(cible);
        return this;
    }

    public void setCible(CibleVacc cible) {
        this.cible = cible;
    }

    public Long getAge() {
        return this.age;
    }

    public Cible age(Long age) {
        this.setAge(age);
        return this;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public Vaccination getVaccination() {
        return this.vaccination;
    }

    public void setVaccination(Vaccination vaccination) {
        this.vaccination = vaccination;
    }

    public Cible vaccination(Vaccination vaccination) {
        this.setVaccination(vaccination);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cible)) {
            return false;
        }
        return id != null && id.equals(((Cible) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cible{" +
            "id=" + getId() +
            ", cible='" + getCible() + "'" +
            ", age=" + getAge() +
            "}";
    }
}
