package agir.gouv.sn.domain;

import agir.gouv.sn.domain.enumeration.Sport;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TypeSport.
 */
@Entity
@Table(name = "type_sport")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TypeSport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "sport")
    private Sport sport;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TypeSport id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Sport getSport() {
        return this.sport;
    }

    public TypeSport sport(Sport sport) {
        this.setSport(sport);
        return this;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeSport)) {
            return false;
        }
        return id != null && id.equals(((TypeSport) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeSport{" +
            "id=" + getId() +
            ", sport='" + getSport() + "'" +
            "}";
    }
}
