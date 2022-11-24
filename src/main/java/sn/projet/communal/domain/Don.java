package sn.projet.communal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.projet.communal.domain.enumeration.TypeDon;

/**
 * A Don.
 */
@Entity
@Table(name = "don")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Don implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_don")
    private TypeDon typeDon;

    @Column(name = "montant")
    private Long montant;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JsonIgnoreProperties(value = { "dons", "beneficiaires", "donSangs", "vaccinations" }, allowSetters = true)
    private Annonce annonce;

    @OneToMany(mappedBy = "don")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "don", "donSangs" }, allowSetters = true)
    private Set<Donneur> donneurs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Don id(Long id) {
        this.id = id;
        return this;
    }

    public TypeDon getTypeDon() {
        return this.typeDon;
    }

    public Don typeDon(TypeDon typeDon) {
        this.typeDon = typeDon;
        return this;
    }

    public void setTypeDon(TypeDon typeDon) {
        this.typeDon = typeDon;
    }

    public Long getMontant() {
        return this.montant;
    }

    public Don montant(Long montant) {
        this.montant = montant;
        return this;
    }

    public void setMontant(Long montant) {
        this.montant = montant;
    }

    public String getDescription() {
        return this.description;
    }

    public Don description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Annonce getAnnonce() {
        return this.annonce;
    }

    public Don annonce(Annonce annonce) {
        this.setAnnonce(annonce);
        return this;
    }

    public void setAnnonce(Annonce annonce) {
        this.annonce = annonce;
    }

    public Set<Donneur> getDonneurs() {
        return this.donneurs;
    }

    public Don donneurs(Set<Donneur> donneurs) {
        this.setDonneurs(donneurs);
        return this;
    }

    public Don addDonneur(Donneur donneur) {
        this.donneurs.add(donneur);
        donneur.setDon(this);
        return this;
    }

    public Don removeDonneur(Donneur donneur) {
        this.donneurs.remove(donneur);
        donneur.setDon(null);
        return this;
    }

    public void setDonneurs(Set<Donneur> donneurs) {
        if (this.donneurs != null) {
            this.donneurs.forEach(i -> i.setDon(null));
        }
        if (donneurs != null) {
            donneurs.forEach(i -> i.setDon(this));
        }
        this.donneurs = donneurs;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Don)) {
            return false;
        }
        return id != null && id.equals(((Don) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Don{" +
            "id=" + getId() +
            ", typeDon='" + getTypeDon() + "'" +
            ", montant=" + getMontant() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
