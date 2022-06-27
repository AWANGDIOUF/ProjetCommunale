package agir.gouv.sn.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SensibiisationInternet.
 */
@Entity
@Table(name = "sensibiisation_internet")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SensibiisationInternet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "theme")
    private LocalDate theme;

    @Lob
    @Column(name = "interdiction")
    private String interdiction;

    @Lob
    @Column(name = "bonne_pratique")
    private String bonnePratique;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SensibiisationInternet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getTheme() {
        return this.theme;
    }

    public SensibiisationInternet theme(LocalDate theme) {
        this.setTheme(theme);
        return this;
    }

    public void setTheme(LocalDate theme) {
        this.theme = theme;
    }

    public String getInterdiction() {
        return this.interdiction;
    }

    public SensibiisationInternet interdiction(String interdiction) {
        this.setInterdiction(interdiction);
        return this;
    }

    public void setInterdiction(String interdiction) {
        this.interdiction = interdiction;
    }

    public String getBonnePratique() {
        return this.bonnePratique;
    }

    public SensibiisationInternet bonnePratique(String bonnePratique) {
        this.setBonnePratique(bonnePratique);
        return this;
    }

    public void setBonnePratique(String bonnePratique) {
        this.bonnePratique = bonnePratique;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SensibiisationInternet)) {
            return false;
        }
        return id != null && id.equals(((SensibiisationInternet) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SensibiisationInternet{" +
            "id=" + getId() +
            ", theme='" + getTheme() + "'" +
            ", interdiction='" + getInterdiction() + "'" +
            ", bonnePratique='" + getBonnePratique() + "'" +
            "}";
    }
}
