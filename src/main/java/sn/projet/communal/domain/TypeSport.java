package sn.projet.communal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.projet.communal.domain.enumeration.Sport;

/**
 * A TypeSport.
 */
@Entity
@Table(name = "type_sport")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TypeSport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "sport")
    private Sport sport;

    @JsonIgnoreProperties(value = { "typeSport", "joueur", "quartiers", "matches", "archves" }, allowSetters = true)
    @OneToOne(mappedBy = "typeSport")
    private Equipe equipe;

    @JsonIgnoreProperties(value = { "typeSport", "conmbattant", "quartiers", "competitions", "archves" }, allowSetters = true)
    @OneToOne(mappedBy = "typeSport")
    private Club club;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeSport id(Long id) {
        this.id = id;
        return this;
    }

    public Sport getSport() {
        return this.sport;
    }

    public TypeSport sport(Sport sport) {
        this.sport = sport;
        return this;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public Equipe getEquipe() {
        return this.equipe;
    }

    public TypeSport equipe(Equipe equipe) {
        this.setEquipe(equipe);
        return this;
    }

    public void setEquipe(Equipe equipe) {
        if (this.equipe != null) {
            this.equipe.setTypeSport(null);
        }
        if (equipe != null) {
            equipe.setTypeSport(this);
        }
        this.equipe = equipe;
    }

    public Club getClub() {
        return this.club;
    }

    public TypeSport club(Club club) {
        this.setClub(club);
        return this;
    }

    public void setClub(Club club) {
        if (this.club != null) {
            this.club.setTypeSport(null);
        }
        if (club != null) {
            club.setTypeSport(this);
        }
        this.club = club;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TypeSport)) {
            return false;
        }
        return id != null && id.equals(((TypeSport) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TypeSport{" +
            "id=" + getId() +
            ", sport='" + getSport() + "'" +
            "}";
    }
}
