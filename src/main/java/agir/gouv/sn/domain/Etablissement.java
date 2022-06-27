package agir.gouv.sn.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Etablissement.
 */
@Entity
@Table(name = "etablissement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Etablissement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_etat")
    private String nomEtat;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @Lob
    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "logo")
    private byte[] logo;

    @Column(name = "logo_content_type")
    private String logoContentType;

    @OneToMany(mappedBy = "etablissement")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "propositions", "lienTutoriels", "etablissement" }, allowSetters = true)
    private Set<Ensegnant> ensegnants = new HashSet<>();

    @OneToMany(mappedBy = "etablissement")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "etablissement" }, allowSetters = true)
    private Set<ResultatExamen> resultatExamen = new HashSet<>();

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

    public Etablissement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomEtat() {
        return this.nomEtat;
    }

    public Etablissement nomEtat(String nomEtat) {
        this.setNomEtat(nomEtat);
        return this;
    }

    public void setNomEtat(String nomEtat) {
        this.nomEtat = nomEtat;
    }

    public LocalDate getDateCreation() {
        return this.dateCreation;
    }

    public Etablissement dateCreation(LocalDate dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getDescription() {
        return this.description;
    }

    public Etablissement description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getLogo() {
        return this.logo;
    }

    public Etablissement logo(byte[] logo) {
        this.setLogo(logo);
        return this;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getLogoContentType() {
        return this.logoContentType;
    }

    public Etablissement logoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
        return this;
    }

    public void setLogoContentType(String logoContentType) {
        this.logoContentType = logoContentType;
    }

    public Set<Ensegnant> getEnsegnants() {
        return this.ensegnants;
    }

    public void setEnsegnants(Set<Ensegnant> ensegnants) {
        if (this.ensegnants != null) {
            this.ensegnants.forEach(i -> i.setEtablissement(null));
        }
        if (ensegnants != null) {
            ensegnants.forEach(i -> i.setEtablissement(this));
        }
        this.ensegnants = ensegnants;
    }

    public Etablissement ensegnants(Set<Ensegnant> ensegnants) {
        this.setEnsegnants(ensegnants);
        return this;
    }

    public Etablissement addEnsegnant(Ensegnant ensegnant) {
        this.ensegnants.add(ensegnant);
        ensegnant.setEtablissement(this);
        return this;
    }

    public Etablissement removeEnsegnant(Ensegnant ensegnant) {
        this.ensegnants.remove(ensegnant);
        ensegnant.setEtablissement(null);
        return this;
    }

    public Set<ResultatExamen> getResultatExamen() {
        return this.resultatExamen;
    }

    public void setResultatExamen(Set<ResultatExamen> resultatExamen) {
        if (this.resultatExamen != null) {
            this.resultatExamen.forEach(i -> i.setEtablissement(null));
        }
        if (resultatExamen != null) {
            resultatExamen.forEach(i -> i.setEtablissement(this));
        }
        this.resultatExamen = resultatExamen;
    }

    public Etablissement resultatExamen(Set<ResultatExamen> resultatExamen) {
        this.setResultatExamen(resultatExamen);
        return this;
    }

    public Etablissement addResultatExamen(ResultatExamen resultatExamen) {
        this.resultatExamen.add(resultatExamen);
        resultatExamen.setEtablissement(this);
        return this;
    }

    public Etablissement removeResultatExamen(ResultatExamen resultatExamen) {
        this.resultatExamen.remove(resultatExamen);
        resultatExamen.setEtablissement(null);
        return this;
    }

    public Quartier getQuartier() {
        return this.quartier;
    }

    public void setQuartier(Quartier quartier) {
        this.quartier = quartier;
    }

    public Etablissement quartier(Quartier quartier) {
        this.setQuartier(quartier);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Etablissement)) {
            return false;
        }
        return id != null && id.equals(((Etablissement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Etablissement{" +
            "id=" + getId() +
            ", nomEtat='" + getNomEtat() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", description='" + getDescription() + "'" +
            ", logo='" + getLogo() + "'" +
            ", logoContentType='" + getLogoContentType() + "'" +
            "}";
    }
}
