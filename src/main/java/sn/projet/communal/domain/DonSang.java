package sn.projet.communal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DonSang.
 */
@Entity
@Table(name = "don_sang")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DonSang implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "organisateur")
    private String organisateur;

    @Lob
    @Column(name = "description")
    private String description;

    @ManyToOne
    @JsonIgnoreProperties(value = { "dons", "beneficiaires", "donSangs", "vaccinations" }, allowSetters = true)
    private Annonce annonce;

    @ManyToOne
    @JsonIgnoreProperties(value = { "don", "donSangs" }, allowSetters = true)
    private Donneur donneur;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DonSang id(Long id) {
        this.id = id;
        return this;
    }

    public String getOrganisateur() {
        return this.organisateur;
    }

    public DonSang organisateur(String organisateur) {
        this.organisateur = organisateur;
        return this;
    }

    public void setOrganisateur(String organisateur) {
        this.organisateur = organisateur;
    }

    public String getDescription() {
        return this.description;
    }

    public DonSang description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Annonce getAnnonce() {
        return this.annonce;
    }

    public DonSang annonce(Annonce annonce) {
        this.setAnnonce(annonce);
        return this;
    }

    public void setAnnonce(Annonce annonce) {
        this.annonce = annonce;
    }

    public Donneur getDonneur() {
        return this.donneur;
    }

    public DonSang donneur(Donneur donneur) {
        this.setDonneur(donneur);
        return this;
    }

    public void setDonneur(Donneur donneur) {
        this.donneur = donneur;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DonSang)) {
            return false;
        }
        return id != null && id.equals(((DonSang) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DonSang{" +
            "id=" + getId() +
            ", organisateur='" + getOrganisateur() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
