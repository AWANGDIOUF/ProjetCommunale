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
 * A Combattant.
 */
@Entity
@Table(name = "combattant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Combattant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_combattant")
    private String nomCombattant;

    @Column(name = "prenom_combattant")
    private String prenomCombattant;

    @Column(name = "date_nais_combattant")
    private LocalDate dateNaisCombattant;

    @Column(name = "lieu_nais_combattant")
    private String lieuNaisCombattant;

    @Lob
    @Column(name = "photo_combattant")
    private byte[] photoCombattant;

    @Column(name = "photo_combattant_content_type")
    private String photoCombattantContentType;

    @OneToMany(mappedBy = "combattant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "competition", "combattant" }, allowSetters = true)
    private Set<Vainqueur> vainqueurs = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "typeSport", "combattants", "competitions", "archves", "quartier" }, allowSetters = true)
    private Club club;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Combattant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomCombattant() {
        return this.nomCombattant;
    }

    public Combattant nomCombattant(String nomCombattant) {
        this.setNomCombattant(nomCombattant);
        return this;
    }

    public void setNomCombattant(String nomCombattant) {
        this.nomCombattant = nomCombattant;
    }

    public String getPrenomCombattant() {
        return this.prenomCombattant;
    }

    public Combattant prenomCombattant(String prenomCombattant) {
        this.setPrenomCombattant(prenomCombattant);
        return this;
    }

    public void setPrenomCombattant(String prenomCombattant) {
        this.prenomCombattant = prenomCombattant;
    }

    public LocalDate getDateNaisCombattant() {
        return this.dateNaisCombattant;
    }

    public Combattant dateNaisCombattant(LocalDate dateNaisCombattant) {
        this.setDateNaisCombattant(dateNaisCombattant);
        return this;
    }

    public void setDateNaisCombattant(LocalDate dateNaisCombattant) {
        this.dateNaisCombattant = dateNaisCombattant;
    }

    public String getLieuNaisCombattant() {
        return this.lieuNaisCombattant;
    }

    public Combattant lieuNaisCombattant(String lieuNaisCombattant) {
        this.setLieuNaisCombattant(lieuNaisCombattant);
        return this;
    }

    public void setLieuNaisCombattant(String lieuNaisCombattant) {
        this.lieuNaisCombattant = lieuNaisCombattant;
    }

    public byte[] getPhotoCombattant() {
        return this.photoCombattant;
    }

    public Combattant photoCombattant(byte[] photoCombattant) {
        this.setPhotoCombattant(photoCombattant);
        return this;
    }

    public void setPhotoCombattant(byte[] photoCombattant) {
        this.photoCombattant = photoCombattant;
    }

    public String getPhotoCombattantContentType() {
        return this.photoCombattantContentType;
    }

    public Combattant photoCombattantContentType(String photoCombattantContentType) {
        this.photoCombattantContentType = photoCombattantContentType;
        return this;
    }

    public void setPhotoCombattantContentType(String photoCombattantContentType) {
        this.photoCombattantContentType = photoCombattantContentType;
    }

    public Set<Vainqueur> getVainqueurs() {
        return this.vainqueurs;
    }

    public void setVainqueurs(Set<Vainqueur> vainqueurs) {
        if (this.vainqueurs != null) {
            this.vainqueurs.forEach(i -> i.setCombattant(null));
        }
        if (vainqueurs != null) {
            vainqueurs.forEach(i -> i.setCombattant(this));
        }
        this.vainqueurs = vainqueurs;
    }

    public Combattant vainqueurs(Set<Vainqueur> vainqueurs) {
        this.setVainqueurs(vainqueurs);
        return this;
    }

    public Combattant addVainqueur(Vainqueur vainqueur) {
        this.vainqueurs.add(vainqueur);
        vainqueur.setCombattant(this);
        return this;
    }

    public Combattant removeVainqueur(Vainqueur vainqueur) {
        this.vainqueurs.remove(vainqueur);
        vainqueur.setCombattant(null);
        return this;
    }

    public Club getClub() {
        return this.club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public Combattant club(Club club) {
        this.setClub(club);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Combattant)) {
            return false;
        }
        return id != null && id.equals(((Combattant) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Combattant{" +
            "id=" + getId() +
            ", nomCombattant='" + getNomCombattant() + "'" +
            ", prenomCombattant='" + getPrenomCombattant() + "'" +
            ", dateNaisCombattant='" + getDateNaisCombattant() + "'" +
            ", lieuNaisCombattant='" + getLieuNaisCombattant() + "'" +
            ", photoCombattant='" + getPhotoCombattant() + "'" +
            ", photoCombattantContentType='" + getPhotoCombattantContentType() + "'" +
            "}";
    }
}
