package agir.gouv.sn.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Logiciel.
 */
@Entity
@Table(name = "logiciel")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Logiciel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_logiciel")
    private String nomLogiciel;

    @Lob
    @Column(name = "description")
    private String description;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Logiciel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomLogiciel() {
        return this.nomLogiciel;
    }

    public Logiciel nomLogiciel(String nomLogiciel) {
        this.setNomLogiciel(nomLogiciel);
        return this;
    }

    public void setNomLogiciel(String nomLogiciel) {
        this.nomLogiciel = nomLogiciel;
    }

    public String getDescription() {
        return this.description;
    }

    public Logiciel description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Logiciel)) {
            return false;
        }
        return id != null && id.equals(((Logiciel) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Logiciel{" +
            "id=" + getId() +
            ", nomLogiciel='" + getNomLogiciel() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
