package agir.gouv.sn.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Proposition.
 */
@Entity
@Table(name = "proposition")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Proposition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "description")
    private String description;

    @ManyToOne
    @JsonIgnoreProperties(value = { "propositions", "lienTutoriels", "etablissement" }, allowSetters = true)
    private Ensegnant enseignant;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Proposition id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public Proposition description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Ensegnant getEnseignant() {
        return this.enseignant;
    }

    public void setEnseignant(Ensegnant ensegnant) {
        this.enseignant = ensegnant;
    }

    public Proposition enseignant(Ensegnant ensegnant) {
        this.setEnseignant(ensegnant);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Proposition)) {
            return false;
        }
        return id != null && id.equals(((Proposition) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Proposition{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
