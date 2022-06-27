package agir.gouv.sn.domain;

import agir.gouv.sn.domain.enumeration.TypeBeneficiaire;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Beneficiaire.
 */
@Entity
@Table(name = "beneficiaire")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Beneficiaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_beneficiaire")
    private TypeBeneficiaire typeBeneficiaire;

    @Column(name = "autre_beneficiaire")
    private String autreBeneficiaire;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "justification")
    private String justification;

    @OneToMany(mappedBy = "beneficiaire")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "don", "beneficiaire", "donSangs", "vaccinations" }, allowSetters = true)
    private Set<Annonce> annonces = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "artiste",
            "equipes",
            "clubs",
            "beneficiaires",
            "collecteurOdeurs",
            "vidanges",
            "recuperationRecyclables",
            "etablissements",
            "eleveurs",
        },
        allowSetters = true
    )
    private Quartier quartier;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Beneficiaire id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeBeneficiaire getTypeBeneficiaire() {
        return this.typeBeneficiaire;
    }

    public Beneficiaire typeBeneficiaire(TypeBeneficiaire typeBeneficiaire) {
        this.setTypeBeneficiaire(typeBeneficiaire);
        return this;
    }

    public void setTypeBeneficiaire(TypeBeneficiaire typeBeneficiaire) {
        this.typeBeneficiaire = typeBeneficiaire;
    }

    public String getAutreBeneficiaire() {
        return this.autreBeneficiaire;
    }

    public Beneficiaire autreBeneficiaire(String autreBeneficiaire) {
        this.setAutreBeneficiaire(autreBeneficiaire);
        return this;
    }

    public void setAutreBeneficiaire(String autreBeneficiaire) {
        this.autreBeneficiaire = autreBeneficiaire;
    }

    public String getDescription() {
        return this.description;
    }

    public Beneficiaire description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJustification() {
        return this.justification;
    }

    public Beneficiaire justification(String justification) {
        this.setJustification(justification);
        return this;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public Set<Annonce> getAnnonces() {
        return this.annonces;
    }

    public void setAnnonces(Set<Annonce> annonces) {
        if (this.annonces != null) {
            this.annonces.forEach(i -> i.setBeneficiaire(null));
        }
        if (annonces != null) {
            annonces.forEach(i -> i.setBeneficiaire(this));
        }
        this.annonces = annonces;
    }

    public Beneficiaire annonces(Set<Annonce> annonces) {
        this.setAnnonces(annonces);
        return this;
    }

    public Beneficiaire addAnnonce(Annonce annonce) {
        this.annonces.add(annonce);
        annonce.setBeneficiaire(this);
        return this;
    }

    public Beneficiaire removeAnnonce(Annonce annonce) {
        this.annonces.remove(annonce);
        annonce.setBeneficiaire(null);
        return this;
    }

    public Quartier getQuartier() {
        return this.quartier;
    }

    public void setQuartier(Quartier quartier) {
        this.quartier = quartier;
    }

    public Beneficiaire quartier(Quartier quartier) {
        this.setQuartier(quartier);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Beneficiaire)) {
            return false;
        }
        return id != null && id.equals(((Beneficiaire) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Beneficiaire{" +
            "id=" + getId() +
            ", typeBeneficiaire='" + getTypeBeneficiaire() + "'" +
            ", autreBeneficiaire='" + getAutreBeneficiaire() + "'" +
            ", description='" + getDescription() + "'" +
            ", justification='" + getJustification() + "'" +
            "}";
    }
}
