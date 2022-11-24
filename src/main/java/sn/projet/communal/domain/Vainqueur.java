package sn.projet.communal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
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
    private Long id;

    @Column(name = "prix")
    private Double prix;

    @OneToMany(mappedBy = "vainqueur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "vainqueur", "clubs" }, allowSetters = true)
    private Set<Competition> competitions = new HashSet<>();

    @OneToMany(mappedBy = "combattant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "combattant", "clubs" }, allowSetters = true)
    private Set<Combattant> combattants = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vainqueur id(Long id) {
        this.id = id;
        return this;
    }

    public Double getPrix() {
        return this.prix;
    }

    public Vainqueur prix(Double prix) {
        this.prix = prix;
        return this;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public Set<Competition> getCompetitions() {
        return this.competitions;
    }

    public Vainqueur competitions(Set<Competition> competitions) {
        this.setCompetitions(competitions);
        return this;
    }

    public Vainqueur addCompetition(Competition competition) {
        this.competitions.add(competition);
        competition.setVainqueur(this);
        return this;
    }

    public Vainqueur removeCompetition(Competition competition) {
        this.competitions.remove(competition);
        competition.setVainqueur(null);
        return this;
    }

    public void setCompetitions(Set<Competition> competitions) {
        if (this.competitions != null) {
            this.competitions.forEach(i -> i.setVainqueur(null));
        }
        if (competitions != null) {
            competitions.forEach(i -> i.setVainqueur(this));
        }
        this.competitions = competitions;
    }

    public Set<Combattant> getCombattants() {
        return this.combattants;
    }

    public Vainqueur combattants(Set<Combattant> combattants) {
        this.setCombattants(combattants);
        return this;
    }

    public Vainqueur addCombattant(Combattant combattant) {
        this.combattants.add(combattant);
        combattant.setCombattant(this);
        return this;
    }

    public Vainqueur removeCombattant(Combattant combattant) {
        this.combattants.remove(combattant);
        combattant.setCombattant(null);
        return this;
    }

    public void setCombattants(Set<Combattant> combattants) {
        if (this.combattants != null) {
            this.combattants.forEach(i -> i.setCombattant(null));
        }
        if (combattants != null) {
            combattants.forEach(i -> i.setCombattant(this));
        }
        this.combattants = combattants;
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
