package agir.gouv.sn.domain;

import agir.gouv.sn.domain.enumeration.Domaine;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Artiste.
 */
@Entity
@Table(name = "artiste")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Artiste implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_artiste")
    private String nomArtiste;

    @Column(name = "prenom_artiste")
    private String prenomArtiste;

    @Enumerated(EnumType.STRING)
    @Column(name = "domaine")
    private Domaine domaine;

    @Column(name = "autre_domaine")
    private String autreDomaine;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Artiste id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomArtiste() {
        return this.nomArtiste;
    }

    public Artiste nomArtiste(String nomArtiste) {
        this.setNomArtiste(nomArtiste);
        return this;
    }

    public void setNomArtiste(String nomArtiste) {
        this.nomArtiste = nomArtiste;
    }

    public String getPrenomArtiste() {
        return this.prenomArtiste;
    }

    public Artiste prenomArtiste(String prenomArtiste) {
        this.setPrenomArtiste(prenomArtiste);
        return this;
    }

    public void setPrenomArtiste(String prenomArtiste) {
        this.prenomArtiste = prenomArtiste;
    }

    public Domaine getDomaine() {
        return this.domaine;
    }

    public Artiste domaine(Domaine domaine) {
        this.setDomaine(domaine);
        return this;
    }

    public void setDomaine(Domaine domaine) {
        this.domaine = domaine;
    }

    public String getAutreDomaine() {
        return this.autreDomaine;
    }

    public Artiste autreDomaine(String autreDomaine) {
        this.setAutreDomaine(autreDomaine);
        return this;
    }

    public void setAutreDomaine(String autreDomaine) {
        this.autreDomaine = autreDomaine;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Artiste)) {
            return false;
        }
        return id != null && id.equals(((Artiste) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Artiste{" +
            "id=" + getId() +
            ", nomArtiste='" + getNomArtiste() + "'" +
            ", prenomArtiste='" + getPrenomArtiste() + "'" +
            ", domaine='" + getDomaine() + "'" +
            ", autreDomaine='" + getAutreDomaine() + "'" +
            "}";
    }
}
