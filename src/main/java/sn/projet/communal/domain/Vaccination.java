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
 * A Vaccination.
 */
@Entity
@Table(name = "vaccination")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Vaccination implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "duree")
    private Boolean duree;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    private LocalDate dateFin;

    @ManyToOne
    @JsonIgnoreProperties(value = { "dons", "beneficiaires", "donSangs", "vaccinations" }, allowSetters = true)
    private Annonce annonce;

    @JsonIgnoreProperties(value = { "vaccination" }, allowSetters = true)
    @OneToOne(mappedBy = "vaccination")
    private TypeVaccin typeVaccin;

    @OneToMany(mappedBy = "vaccination")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "vaccination" }, allowSetters = true)
    private Set<Cible> cibles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vaccination id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Vaccination date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return this.description;
    }

    public Vaccination description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDuree() {
        return this.duree;
    }

    public Vaccination duree(Boolean duree) {
        this.duree = duree;
        return this;
    }

    public void setDuree(Boolean duree) {
        this.duree = duree;
    }

    public LocalDate getDateDebut() {
        return this.dateDebut;
    }

    public Vaccination dateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
        return this;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return this.dateFin;
    }

    public Vaccination dateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
        return this;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Annonce getAnnonce() {
        return this.annonce;
    }

    public Vaccination annonce(Annonce annonce) {
        this.setAnnonce(annonce);
        return this;
    }

    public void setAnnonce(Annonce annonce) {
        this.annonce = annonce;
    }

    public TypeVaccin getTypeVaccin() {
        return this.typeVaccin;
    }

    public Vaccination typeVaccin(TypeVaccin typeVaccin) {
        this.setTypeVaccin(typeVaccin);
        return this;
    }

    public void setTypeVaccin(TypeVaccin typeVaccin) {
        if (this.typeVaccin != null) {
            this.typeVaccin.setVaccination(null);
        }
        if (typeVaccin != null) {
            typeVaccin.setVaccination(this);
        }
        this.typeVaccin = typeVaccin;
    }

    public Set<Cible> getCibles() {
        return this.cibles;
    }

    public Vaccination cibles(Set<Cible> cibles) {
        this.setCibles(cibles);
        return this;
    }

    public Vaccination addCible(Cible cible) {
        this.cibles.add(cible);
        cible.setVaccination(this);
        return this;
    }

    public Vaccination removeCible(Cible cible) {
        this.cibles.remove(cible);
        cible.setVaccination(null);
        return this;
    }

    public void setCibles(Set<Cible> cibles) {
        if (this.cibles != null) {
            this.cibles.forEach(i -> i.setVaccination(null));
        }
        if (cibles != null) {
            cibles.forEach(i -> i.setVaccination(this));
        }
        this.cibles = cibles;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vaccination)) {
            return false;
        }
        return id != null && id.equals(((Vaccination) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vaccination{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            ", description='" + getDescription() + "'" +
            ", duree='" + getDuree() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFin='" + getDateFin() + "'" +
            "}";
    }
}
