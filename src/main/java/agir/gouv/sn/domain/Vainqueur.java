package agir.gouv.sn.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Vainqueur.
 */
@Entity
@Table(name = "vainqueur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Vainqueur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "prix")
    private Double prix;

    @ManyToOne
    @JsonIgnoreProperties(value = { "vainqueurs", "club" }, allowSetters = true)
    private Competition competition;

    @ManyToOne
    @JsonIgnoreProperties(value = { "vainqueurs", "club" }, allowSetters = true)
    private Combattant combattant;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Vainqueur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPrix() {
        return this.prix;
    }

    public Vainqueur prix(Double prix) {
        this.setPrix(prix);
        return this;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public Competition getCompetition() {
        return this.competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public Vainqueur competition(Competition competition) {
        this.setCompetition(competition);
        return this;
    }

    public Combattant getCombattant() {
        return this.combattant;
    }

    public void setCombattant(Combattant combattant) {
        this.combattant = combattant;
    }

    public Vainqueur combattant(Combattant combattant) {
        this.setCombattant(combattant);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vainqueur)) {
            return false;
        }
        return id != null && id.equals(((Vainqueur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vainqueur{" +
            "id=" + getId() +
            ", prix=" + getPrix() +
            "}";
    }
}
