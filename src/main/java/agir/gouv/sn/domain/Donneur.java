package agir.gouv.sn.domain;

import agir.gouv.sn.domain.enumeration.TypeDonneur;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
    @Column(name = "id")
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

    @OneToMany(mappedBy = "donneur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "annonces", "donneur" }, allowSetters = true)
    private Set<Don> dons = new HashSet<>();

    @OneToMany(mappedBy = "donneur")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "annonce", "donneur" }, allowSetters = true)
    private Set<DonSang> donSangs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Donneur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeDonneur getTypeDonneur() {
        return this.typeDonneur;
    }

    public Donneur typeDonneur(TypeDonneur typeDonneur) {
        this.setTypeDonneur(typeDonneur);
        return this;
    }

    public void setTypeDonneur(TypeDonneur typeDonneur) {
        this.typeDonneur = typeDonneur;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Donneur prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return this.nom;
    }

    public Donneur nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return this.email;
    }

    public Donneur email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Donneur adresse(String adresse) {
        this.setAdresse(adresse);
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTel1() {
        return this.tel1;
    }

    public Donneur tel1(String tel1) {
        this.setTel1(tel1);
        return this;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    public String getVille() {
        return this.ville;
    }

    public Donneur ville(String ville) {
        this.setVille(ville);
        return this;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getDescription() {
        return this.description;
    }

    public Donneur description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Don> getDons() {
        return this.dons;
    }

    public void setDons(Set<Don> dons) {
        if (this.dons != null) {
            this.dons.forEach(i -> i.setDonneur(null));
        }
        if (dons != null) {
            dons.forEach(i -> i.setDonneur(this));
        }
        this.dons = dons;
    }

    public Donneur dons(Set<Don> dons) {
        this.setDons(dons);
        return this;
    }

    public Donneur addDon(Don don) {
        this.dons.add(don);
        don.setDonneur(this);
        return this;
    }

    public Donneur removeDon(Don don) {
        this.dons.remove(don);
        don.setDonneur(null);
        return this;
    }

    public Set<DonSang> getDonSangs() {
        return this.donSangs;
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
