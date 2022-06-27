package agir.gouv.sn.domain;

import agir.gouv.sn.domain.enumeration.Domaine;
import agir.gouv.sn.domain.enumeration.Profil;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UtilisationInternet.
 */
@Entity
@Table(name = "utilisation_internet")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UtilisationInternet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "profil")
    private Profil profil;

    @Column(name = "autre")
    private String autre;

    @Enumerated(EnumType.STRING)
    @Column(name = "domaine")
    private Domaine domaine;

    @Lob
    @Column(name = "description")
    private String description;

    @OneToOne
    @JoinColumn(unique = true)
    private Logiciel logiciel;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UtilisationInternet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Profil getProfil() {
        return this.profil;
    }

    public UtilisationInternet profil(Profil profil) {
        this.setProfil(profil);
        return this;
    }

    public void setProfil(Profil profil) {
        this.profil = profil;
    }

    public String getAutre() {
        return this.autre;
    }

    public UtilisationInternet autre(String autre) {
        this.setAutre(autre);
        return this;
    }

    public void setAutre(String autre) {
        this.autre = autre;
    }

    public Domaine getDomaine() {
        return this.domaine;
    }

    public UtilisationInternet domaine(Domaine domaine) {
        this.setDomaine(domaine);
        return this;
    }

    public void setDomaine(Domaine domaine) {
        this.domaine = domaine;
    }

    public String getDescription() {
        return this.description;
    }

    public UtilisationInternet description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Logiciel getLogiciel() {
        return this.logiciel;
    }

    public void setLogiciel(Logiciel logiciel) {
        this.logiciel = logiciel;
    }

    public UtilisationInternet logiciel(Logiciel logiciel) {
        this.setLogiciel(logiciel);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UtilisationInternet)) {
            return false;
        }
        return id != null && id.equals(((UtilisationInternet) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UtilisationInternet{" +
            "id=" + getId() +
            ", profil='" + getProfil() + "'" +
            ", autre='" + getAutre() + "'" +
            ", domaine='" + getDomaine() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
