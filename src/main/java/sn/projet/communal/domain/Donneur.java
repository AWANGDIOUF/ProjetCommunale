package sn.projet.communal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.projet.communal.domain.enumeration.TypeDonneur;

/**
 * A Donneur.
 */
@Entity
@Table(name = "donneur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Donneur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_donneur")
    private TypeDonneur typeDonneur;

    @Column(name = "prenom")
    private String prenom;

    @NotNull
    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "tel_1", unique = true)
    private String tel1;

    @Column(name = "ville")
    private String ville;

    @Lob
    @Column(name = "description")
    private String description;

    @ManyToOne
    @JsonIgnoreProperties(value = { "annonce", "donneurs" }, allowSetters = true)
    private Don don;

    @OneToMany(mappedBy = "donneur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "annonce", "donneur" }, allowSetters = true)
    private Set<DonSang> donSangs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Donneur id(Long id) {
        this.id = id;
        return this;
    }

    public TypeDonneur getTypeDonneur() {
        return this.typeDonneur;
    }

    public Donneur typeDonneur(TypeDonneur typeDonneur) {
        this.typeDonneur = typeDonneur;
        return this;
    }

    public void setTypeDonneur(TypeDonneur typeDonneur) {
        this.typeDonneur = typeDonneur;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Donneur prenom(String prenom) {
        this.prenom = prenom;
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return this.nom;
    }

    public Donneur nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return this.email;
    }

    public Donneur email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Donneur adresse(String adresse) {
        this.adresse = adresse;
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTel1() {
        return this.tel1;
    }

    public Donneur tel1(String tel1) {
        this.tel1 = tel1;
        return this;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    public String getVille() {
        return this.ville;
    }

    public Donneur ville(String ville) {
        this.ville = ville;
        return this;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getDescription() {
        return this.description;
    }

    public Donneur description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Don getDon() {
        return this.don;
    }

    public Donneur don(Don don) {
        this.setDon(don);
        return this;
    }

    public void setDon(Don don) {
        this.don = don;
    }

    public Set<DonSang> getDonSangs() {
        return this.donSangs;
    }

    public Donneur donSangs(Set<DonSang> donSangs) {
        this.setDonSangs(donSangs);
        return this;
    }

    public Donneur addDonSang(DonSang donSang) {
        this.donSangs.add(donSang);
        donSang.setDonneur(this);
        return this;
    }

    public Donneur removeDonSang(DonSang donSang) {
        this.donSangs.remove(donSang);
        donSang.setDonneur(null);
        return this;
    }

    public void setDonSangs(Set<DonSang> donSangs) {
        if (this.donSangs != null) {
            this.donSangs.forEach(i -> i.setDonneur(null));
        }
        if (donSangs != null) {
            donSangs.forEach(i -> i.setDonneur(this));
        }
        this.donSangs = donSangs;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Donneur)) {
            return false;
        }
        return id != null && id.equals(((Donneur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Donneur{" +
            "id=" + getId() +
            ", typeDonneur='" + getTypeDonneur() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", nom='" + getNom() + "'" +
            ", email='" + getEmail() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", tel1='" + getTel1() + "'" +
            ", ville='" + getVille() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
