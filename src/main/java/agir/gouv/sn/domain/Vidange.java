package agir.gouv.sn.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Vidange.
 */
@Entity
@Table(name = "vidange")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Vidange implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_videur")
    private String nomVideur;

    @Column(name = "prenom_videur")
    private String prenomVideur;

    @Column(name = "tel_1")
    private String tel1;

    @Column(name = "tel_2")
    private String tel2;

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

    public Vidange id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomVideur() {
        return this.nomVideur;
    }

    public Vidange nomVideur(String nomVideur) {
        this.setNomVideur(nomVideur);
        return this;
    }

    public void setNomVideur(String nomVideur) {
        this.nomVideur = nomVideur;
    }

    public String getPrenomVideur() {
        return this.prenomVideur;
    }

    public Vidange prenomVideur(String prenomVideur) {
        this.setPrenomVideur(prenomVideur);
        return this;
    }

    public void setPrenomVideur(String prenomVideur) {
        this.prenomVideur = prenomVideur;
    }

    public String getTel1() {
        return this.tel1;
    }

    public Vidange tel1(String tel1) {
        this.setTel1(tel1);
        return this;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    public String getTel2() {
        return this.tel2;
    }

    public Vidange tel2(String tel2) {
        this.setTel2(tel2);
        return this;
    }

    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }

    public Quartier getQuartier() {
        return this.quartier;
    }

    public void setQuartier(Quartier quartier) {
        this.quartier = quartier;
    }

    public Vidange quartier(Quartier quartier) {
        this.setQuartier(quartier);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vidange)) {
            return false;
        }
        return id != null && id.equals(((Vidange) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vidange{" +
            "id=" + getId() +
            ", nomVideur='" + getNomVideur() + "'" +
            ", prenomVideur='" + getPrenomVideur() + "'" +
            ", tel1='" + getTel1() + "'" +
            ", tel2='" + getTel2() + "'" +
            "}";
    }
}
