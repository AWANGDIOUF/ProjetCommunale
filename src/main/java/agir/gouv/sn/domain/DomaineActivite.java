package agir.gouv.sn.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DomaineActivite.
 */
@Entity
@Table(name = "domaine_activite")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DomaineActivite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "type_activite")
    private String typeActivite;

    @Column(name = "description")
    private String description;

    @Column(name = "date_activite")
    private ZonedDateTime dateActivite;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DomaineActivite id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeActivite() {
        return this.typeActivite;
    }

    public DomaineActivite typeActivite(String typeActivite) {
        this.setTypeActivite(typeActivite);
        return this;
    }

    public void setTypeActivite(String typeActivite) {
        this.typeActivite = typeActivite;
    }

    public String getDescription() {
        return this.description;
    }

    public DomaineActivite description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getDateActivite() {
        return this.dateActivite;
    }

    public DomaineActivite dateActivite(ZonedDateTime dateActivite) {
        this.setDateActivite(dateActivite);
        return this;
    }

    public void setDateActivite(ZonedDateTime dateActivite) {
        this.dateActivite = dateActivite;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DomaineActivite)) {
            return false;
        }
        return id != null && id.equals(((DomaineActivite) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DomaineActivite{" +
            "id=" + getId() +
            ", typeActivite='" + getTypeActivite() + "'" +
            ", description='" + getDescription() + "'" +
            ", dateActivite='" + getDateActivite() + "'" +
            "}";
    }
}
