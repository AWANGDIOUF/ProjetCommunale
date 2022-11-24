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
 * A Annonce.
 */
@Entity
@Table(name = "annonce")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Annonce implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "titre")
    private String titre;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "lieu")
    private String lieu;

    @OneToMany(mappedBy = "annonce")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "annonce", "donneurs" }, allowSetters = true)
    private Set<Don> dons = new HashSet<>();

    @OneToMany(mappedBy = "annonce")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "annonce", "quartiers" }, allowSetters = true)
    private Set<Beneficiaire> beneficiaires = new HashSet<>();

    @OneToMany(mappedBy = "annonce")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "annonce", "donneur" }, allowSetters = true)
    private Set<DonSang> donSangs = new HashSet<>();

    @OneToMany(mappedBy = "annonce")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "annonce", "typeVaccin", "cibles" }, allowSetters = true)
    private Set<Vaccination> vaccinations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Annonce id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitre() {
        return this.titre;
    }

    public Annonce titre(String titre) {
        this.titre = titre;
        return this;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return this.description;
    }

    public Annonce description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Annonce date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getLieu() {
        return this.lieu;
    }

    public Annonce lieu(String lieu) {
        this.lieu = lieu;
        return this;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public Set<Don> getDons() {
        return this.dons;
    }

    public Annonce dons(Set<Don> dons) {
        this.setDons(dons);
        return this;
    }

    public Annonce addDon(Don don) {
        this.dons.add(don);
        don.setAnnonce(this);
        return this;
    }

    public Annonce removeDon(Don don) {
        this.dons.remove(don);
        don.setAnnonce(null);
        return this;
    }

    public void setDons(Set<Don> dons) {
        if (this.dons != null) {
            this.dons.forEach(i -> i.setAnnonce(null));
        }
        if (dons != null) {
            dons.forEach(i -> i.setAnnonce(this));
        }
        this.dons = dons;
    }

    public Set<Beneficiaire> getBeneficiaires() {
        return this.beneficiaires;
    }

    public Annonce beneficiaires(Set<Beneficiaire> beneficiaires) {
        this.setBeneficiaires(beneficiaires);
        return this;
    }

    public Annonce addBeneficiaire(Beneficiaire beneficiaire) {
        this.beneficiaires.add(beneficiaire);
        beneficiaire.setAnnonce(this);
        return this;
    }

    public Annonce removeBeneficiaire(Beneficiaire beneficiaire) {
        this.beneficiaires.remove(beneficiaire);
        beneficiaire.setAnnonce(null);
        return this;
    }

    public void setBeneficiaires(Set<Beneficiaire> beneficiaires) {
        if (this.beneficiaires != null) {
            this.beneficiaires.forEach(i -> i.setAnnonce(null));
        }
        if (beneficiaires != null) {
            beneficiaires.forEach(i -> i.setAnnonce(this));
        }
        this.beneficiaires = beneficiaires;
    }

    public Set<DonSang> getDonSangs() {
        return this.donSangs;
    }

    public Annonce donSangs(Set<DonSang> donSangs) {
        this.setDonSangs(donSangs);
        return this;
    }

    public Annonce addDonSang(DonSang donSang) {
        this.donSangs.add(donSang);
        donSang.setAnnonce(this);
        return this;
    }

    public Annonce removeDonSang(DonSang donSang) {
        this.donSangs.remove(donSang);
        donSang.setAnnonce(null);
        return this;
    }

    public void setDonSangs(Set<DonSang> donSangs) {
        if (this.donSangs != null) {
            this.donSangs.forEach(i -> i.setAnnonce(null));
        }
        if (donSangs != null) {
            donSangs.forEach(i -> i.setAnnonce(this));
        }
        this.donSangs = donSangs;
    }

    public Set<Vaccination> getVaccinations() {
        return this.vaccinations;
    }

    public Annonce vaccinations(Set<Vaccination> vaccinations) {
        this.setVaccinations(vaccinations);
        return this;
    }

    public Annonce addVaccination(Vaccination vaccination) {
        this.vaccinations.add(vaccination);
        vaccination.setAnnonce(this);
        return this;
    }

    public Annonce removeVaccination(Vaccination vaccination) {
        this.vaccinations.remove(vaccination);
        vaccination.setAnnonce(null);
        return this;
    }

    public void setVaccinations(Set<Vaccination> vaccinations) {
        if (this.vaccinations != null) {
            this.vaccinations.forEach(i -> i.setAnnonce(null));
        }
        if (vaccinations != null) {
            vaccinations.forEach(i -> i.setAnnonce(this));
        }
        this.vaccinations = vaccinations;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Annonce)) {
            return false;
        }
        return id != null && id.equals(((Annonce) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Annonce{" +
            "id=" + getId() +
            ", titre='" + getTitre() + "'" +
            ", description='" + getDescription() + "'" +
            ", date='" + getDate() + "'" +
            ", lieu='" + getLieu() + "'" +
            "}";
    }
}
