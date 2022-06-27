package agir.gouv.sn.domain;

import agir.gouv.sn.domain.enumeration.TypeDon;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_don")
    private TypeDon typeDon;

    @Column(name = "montant")
    private Long montant;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "don")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "don", "beneficiaire", "donSangs", "vaccinations" }, allowSetters = true)
    private Set<Annonce> annonces = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "dons", "donSangs" }, allowSetters = true)
    private Donneur donneur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Don id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeDon getTypeDon() {
        return this.typeDon;
    }

    public Don typeDon(TypeDon typeDon) {
        this.setTypeDon(typeDon);
        return this;
    }

    public void setTypeDon(TypeDon typeDon) {
        this.typeDon = typeDon;
    }

    public Long getMontant() {
        return this.montant;
    }

    public Don montant(Long montant) {
        this.setMontant(montant);
        return this;
    }

    public void setMontant(Long montant) {
        this.montant = montant;
    }

    public String getDescription() {
        return this.description;
    }

    public Don description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Annonce> getAnnonces() {
        return this.annonces;
    }

    public void setAnnonces(Set<Annonce> annonces) {
        if (this.annonces != null) {
            this.annonces.forEach(i -> i.setDon(null));
        }
        if (annonces != null) {
            annonces.forEach(i -> i.setDon(this));
        }
        this.annonces = annonces;
    }

    public Don annonces(Set<Annonce> annonces) {
        this.setAnnonces(annonces);
        return this;
    }

    public Don addAnnonce(Annonce annonce) {
        this.annonces.add(annonce);
        annonce.setDon(this);
        return this;
    }

    public Don removeAnnonce(Annonce annonce) {
        this.annonces.remove(annonce);
        annonce.setDon(null);
        return this;
    }

    public Donneur getDonneur() {
        return this.donneur;
    }

    public void setDonneur(Donneur donneur) {
        this.donneur = donneur;
    }

    public Don donneur(Donneur donneur) {
        this.setDonneur(donneur);
        return this;
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
