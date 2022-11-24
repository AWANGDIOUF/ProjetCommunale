package sn.projet.communal.domain;

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
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "date_nais")
    private LocalDate dateNais;

    @Column(name = "lieu_nais")
    private String lieuNais;

    @Lob
    @Column(name = "photo")
    private byte[] photo;

    @Column(name = "photo_content_type")
    private String photoContentType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "competitions", "combattants" }, allowSetters = true)
    private Vainqueur combattant;

    @OneToMany(mappedBy = "conmbattant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "typeSport", "conmbattant", "quartiers", "competitions", "archves" }, allowSetters = true)
    private Set<Club> clubs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Combattant id(Long id) {
        this.id = id;
        return this;
    }

    public String getNom() {
        return this.nom;
    }

    public Combattant nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Combattant prenom(String prenom) {
        this.prenom = prenom;
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public LocalDate getDateNais() {
        return this.dateNais;
    }

    public Combattant dateNais(LocalDate dateNais) {
        this.dateNais = dateNais;
        return this;
    }

    public void setDateNais(LocalDate dateNais) {
        this.dateNais = dateNais;
    }

    public String getLieuNais() {
        return this.lieuNais;
    }

    public Combattant lieuNais(String lieuNais) {
        this.lieuNais = lieuNais;
        return this;
    }

    public void setLieuNais(String lieuNais) {
        this.lieuNais = lieuNais;
    }

    public byte[] getPhoto() {
        return this.photo;
    }

    public Combattant photo(byte[] photo) {
        this.photo = photo;
        return this;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getPhotoContentType() {
        return this.photoContentType;
    }

    public Combattant photoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
        return this;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public Vainqueur getCombattant() {
        return this.combattant;
    }

    public Combattant combattant(Vainqueur vainqueur) {
        this.setCombattant(vainqueur);
        return this;
    }

    public void setCombattant(Vainqueur vainqueur) {
        this.combattant = vainqueur;
    }

    public Set<Club> getClubs() {
        return this.clubs;
    }

    public Combattant clubs(Set<Club> clubs) {
        this.setClubs(clubs);
        return this;
    }

    public Combattant addClub(Club club) {
        this.clubs.add(club);
        club.setConmbattant(this);
        return this;
    }

    public Combattant removeClub(Club club) {
        this.clubs.remove(club);
        club.setConmbattant(null);
        return this;
    }

    public void setClubs(Set<Club> clubs) {
        if (this.clubs != null) {
            this.clubs.forEach(i -> i.setConmbattant(null));
        }
        if (clubs != null) {
            clubs.forEach(i -> i.setConmbattant(this));
        }
        this.clubs = clubs;
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
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", dateNais='" + getDateNais() + "'" +
            ", lieuNais='" + getLieuNais() + "'" +
            ", photo='" + getPhoto() + "'" +
            ", photoContentType='" + getPhotoContentType() + "'" +
            "}";
    }
}
