package agir.gouv.sn.domain;

import agir.gouv.sn.domain.enumeration.Examen;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ResultatExamen.
 */
@Entity
@Table(name = "resultat_examen")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ResultatExamen implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_exament")
    private Examen typeExament;

    @Column(name = "autre_examen")
    private String autreExamen;

    @Column(name = "taux_reuissite")
    private Double tauxReuissite;

    @Column(name = "annee")
    private LocalDate annee;

    @ManyToOne
    @JsonIgnoreProperties(value = { "ensegnants", "resultatExamen", "quartier" }, allowSetters = true)
    private Etablissement etablissement;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ResultatExamen id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Examen getTypeExament() {
        return this.typeExament;
    }

    public ResultatExamen typeExament(Examen typeExament) {
        this.setTypeExament(typeExament);
        return this;
    }

    public void setTypeExament(Examen typeExament) {
        this.typeExament = typeExament;
    }

    public String getAutreExamen() {
        return this.autreExamen;
    }

    public ResultatExamen autreExamen(String autreExamen) {
        this.setAutreExamen(autreExamen);
        return this;
    }

    public void setAutreExamen(String autreExamen) {
        this.autreExamen = autreExamen;
    }

    public Double getTauxReuissite() {
        return this.tauxReuissite;
    }

    public ResultatExamen tauxReuissite(Double tauxReuissite) {
        this.setTauxReuissite(tauxReuissite);
        return this;
    }

    public void setTauxReuissite(Double tauxReuissite) {
        this.tauxReuissite = tauxReuissite;
    }

    public LocalDate getAnnee() {
        return this.annee;
    }

    public ResultatExamen annee(LocalDate annee) {
        this.setAnnee(annee);
        return this;
    }

    public void setAnnee(LocalDate annee) {
        this.annee = annee;
    }

    public Etablissement getEtablissement() {
        return this.etablissement;
    }

    public void setEtablissement(Etablissement etablissement) {
        this.etablissement = etablissement;
    }

    public ResultatExamen etablissement(Etablissement etablissement) {
        this.setEtablissement(etablissement);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResultatExamen)) {
            return false;
        }
        return id != null && id.equals(((ResultatExamen) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResultatExamen{" +
            "id=" + getId() +
            ", typeExament='" + getTypeExament() + "'" +
            ", autreExamen='" + getAutreExamen() + "'" +
            ", tauxReuissite=" + getTauxReuissite() +
            ", annee='" + getAnnee() + "'" +
            "}";
    }
}
