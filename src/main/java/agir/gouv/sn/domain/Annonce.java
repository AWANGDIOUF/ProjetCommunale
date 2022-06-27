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
 * A Annonce.
 */
@Entity
@Table(name = "annonce")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Annonce implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
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

    @ManyToOne
    @JsonIgnoreProperties(value = { "annonces", "donneur" }, allowSetters = true)
    private Don don;

    @ManyToOne
    @JsonIgnoreProperties(value = { "annonces", "quartier" }, allowSetters = true)
    private Beneficiaire beneficiaire;

    @OneToMany(mappedBy = "annonce")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "annonce", "donneur" }, allowSetters = true)
    private Set<DonSang> donSangs = new HashSet<>();

    @OneToMany(mappedBy = "annonce")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "typeVaccin", "annonce", "cibles" }, allowSetters = true)
    private Set<Vaccination> vaccinations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Annonce id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return this.titre;
    }

    public Annonce titre(String titre) {
        this.setTitre(titre);
        return this;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return this.description;
    }

    public Annonce description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Annonce date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getLieu() {
        return this.lieu;
    }

    public Annonce lieu(String lieu) {
        this.setLieu(lieu);
        return this;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public Don getDon() {
        return this.don;
    }

    public void setDon(Don don) {
        this.don = don;
    }

    public Annonce don(Don don) {
        this.setDon(don);
        return this;
    }

    public Beneficiaire getBeneficiaire() {
        return this.beneficiaire;
    }

    public void setBeneficiaire(Beneficiaire beneficiaire) {
        this.beneficiaire = beneficiaire;
    }

    public Annonce beneficiaire(Beneficiaire beneficiaire) {
        this.setBeneficiaire(beneficiaire);
        return this;
    }

    public Set<DonSang> getDonSangs() {
        return this.donSangs;
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

    public Set<Vaccination> getVaccinations() {
        return this.vaccinations;
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
