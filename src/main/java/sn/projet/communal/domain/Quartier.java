package sn.projet.communal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Quartier.
 */
@Entity
@Table(name = "quartier")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Quartier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom_quartier")
    private String nomQuartier;

    @ManyToOne
    @JsonIgnoreProperties(value = { "typeSport", "joueur", "quartiers", "matches", "archves" }, allowSetters = true)
    private Equipe equipe;

    @ManyToOne
    @JsonIgnoreProperties(value = { "typeSport", "conmbattant", "quartiers", "competitions", "archves" }, allowSetters = true)
    private Club club;

    @ManyToOne
    @JsonIgnoreProperties(value = { "annonce", "quartiers" }, allowSetters = true)
    private Beneficiaire beneficiaire;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Quartier id(Long id) {
        this.id = id;
        return this;
    }

    public String getNomQuartier() {
        return this.nomQuartier;
    }

    public Quartier nomQuartier(String nomQuartier) {
        this.nomQuartier = nomQuartier;
        return this;
    }

    public void setNomQuartier(String nomQuartier) {
        this.nomQuartier = nomQuartier;
    }

    public Equipe getEquipe() {
        return this.equipe;
    }

    public Quartier equipe(Equipe equipe) {
        this.setEquipe(equipe);
        return this;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }

    public Club getClub() {
        return this.club;
    }

    public Quartier club(Club club) {
        this.setClub(club);
        return this;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public Beneficiaire getBeneficiaire() {
        return this.beneficiaire;
    }

    public Quartier beneficiaire(Beneficiaire beneficiaire) {
        this.setBeneficiaire(beneficiaire);
        return this;
    }

    public void setBeneficiaire(Beneficiaire beneficiaire) {
        this.beneficiaire = beneficiaire;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Quartier)) {
            return false;
        }
        return id != null && id.equals(((Quartier) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Quartier{" +
            "id=" + getId() +
            ", nomQuartier='" + getNomQuartier() + "'" +
            "}";
    }
}
