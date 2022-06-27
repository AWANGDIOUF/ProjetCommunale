package agir.gouv.sn.domain;

import agir.gouv.sn.domain.enumeration.TypeEntreprenariat;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Entreprenariat.
 */
@Entity
@Table(name = "entreprenariat")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Entreprenariat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_entre")
    private TypeEntreprenariat typeEntre;

    @Column(name = "autre_entre")
    private String autreEntre;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Entreprenariat id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeEntreprenariat getTypeEntre() {
        return this.typeEntre;
    }

    public Entreprenariat typeEntre(TypeEntreprenariat typeEntre) {
        this.setTypeEntre(typeEntre);
        return this;
    }

    public void setTypeEntre(TypeEntreprenariat typeEntre) {
        this.typeEntre = typeEntre;
    }

    public String getAutreEntre() {
        return this.autreEntre;
    }

    public Entreprenariat autreEntre(String autreEntre) {
        this.setAutreEntre(autreEntre);
        return this;
    }

    public void setAutreEntre(String autreEntre) {
        this.autreEntre = autreEntre;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Entreprenariat)) {
            return false;
        }
        return id != null && id.equals(((Entreprenariat) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Entreprenariat{" +
            "id=" + getId() +
            ", typeEntre='" + getTypeEntre() + "'" +
            ", autreEntre='" + getAutreEntre() + "'" +
            "}";
    }
}
