package agir.gouv.sn.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LienTutoriel.
 */
@Entity
@Table(name = "lien_tutoriel")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LienTutoriel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "description_lien")
    private String descriptionLien;

    @Column(name = "lien")
    private String lien;

    @ManyToOne
    @JsonIgnoreProperties(value = { "propositions", "lienTutoriels", "etablissement" }, allowSetters = true)
    private Ensegnant enseignant;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LienTutoriel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescriptionLien() {
        return this.descriptionLien;
    }

    public LienTutoriel descriptionLien(String descriptionLien) {
        this.setDescriptionLien(descriptionLien);
        return this;
    }

    public void setDescriptionLien(String descriptionLien) {
        this.descriptionLien = descriptionLien;
    }

    public String getLien() {
        return this.lien;
    }

    public LienTutoriel lien(String lien) {
        this.setLien(lien);
        return this;
    }

    public void setLien(String lien) {
        this.lien = lien;
    }

    public Ensegnant getEnseignant() {
        return this.enseignant;
    }

    public void setEnseignant(Ensegnant ensegnant) {
        this.enseignant = ensegnant;
    }

    public LienTutoriel enseignant(Ensegnant ensegnant) {
        this.setEnseignant(ensegnant);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LienTutoriel)) {
            return false;
        }
        return id != null && id.equals(((LienTutoriel) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LienTutoriel{" +
            "id=" + getId() +
            ", descriptionLien='" + getDescriptionLien() + "'" +
            ", lien='" + getLien() + "'" +
            "}";
    }
}
