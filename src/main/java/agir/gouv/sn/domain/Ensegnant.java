package agir.gouv.sn.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Ensegnant.
 */
@Entity
@Table(name = "ensegnant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Ensegnant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @NotNull
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull
    @Column(name = "tel", nullable = false, unique = true)
    private String tel;

    @Column(name = "tel_1", unique = true)
    private String tel1;

    @OneToMany(mappedBy = "enseignant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "enseignant" }, allowSetters = true)
    private Set<Proposition> propositions = new HashSet<>();

    @OneToMany(mappedBy = "enseignant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "enseignant" }, allowSetters = true)
    private Set<LienTutoriel> lienTutoriels = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "ensegnants", "resultatExamen", "quartier" }, allowSetters = true)
    private Etablissement etablissement;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ensegnant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Ensegnant nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Ensegnant prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return this.email;
    }

    public Ensegnant email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return this.tel;
    }

    public Ensegnant tel(String tel) {
        this.setTel(tel);
        return this;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTel1() {
        return this.tel1;
    }

    public Ensegnant tel1(String tel1) {
        this.setTel1(tel1);
        return this;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    public Set<Proposition> getPropositions() {
        return this.propositions;
    }

    public void setPropositions(Set<Proposition> propositions) {
        if (this.propositions != null) {
            this.propositions.forEach(i -> i.setEnseignant(null));
        }
        if (propositions != null) {
            propositions.forEach(i -> i.setEnseignant(this));
        }
        this.propositions = propositions;
    }

    public Ensegnant propositions(Set<Proposition> propositions) {
        this.setPropositions(propositions);
        return this;
    }

    public Ensegnant addProposition(Proposition proposition) {
        this.propositions.add(proposition);
        proposition.setEnseignant(this);
        return this;
    }

    public Ensegnant removeProposition(Proposition proposition) {
        this.propositions.remove(proposition);
        proposition.setEnseignant(null);
        return this;
    }

    public Set<LienTutoriel> getLienTutoriels() {
        return this.lienTutoriels;
    }

    public void setLienTutoriels(Set<LienTutoriel> lienTutoriels) {
        if (this.lienTutoriels != null) {
            this.lienTutoriels.forEach(i -> i.setEnseignant(null));
        }
        if (lienTutoriels != null) {
            lienTutoriels.forEach(i -> i.setEnseignant(this));
        }
        this.lienTutoriels = lienTutoriels;
    }

    public Ensegnant lienTutoriels(Set<LienTutoriel> lienTutoriels) {
        this.setLienTutoriels(lienTutoriels);
        return this;
    }

    public Ensegnant addLienTutoriel(LienTutoriel lienTutoriel) {
        this.lienTutoriels.add(lienTutoriel);
        lienTutoriel.setEnseignant(this);
        return this;
    }

    public Ensegnant removeLienTutoriel(LienTutoriel lienTutoriel) {
        this.lienTutoriels.remove(lienTutoriel);
        lienTutoriel.setEnseignant(null);
        return this;
    }

    public Etablissement getEtablissement() {
        return this.etablissement;
    }

    public void setEtablissement(Etablissement etablissement) {
        this.etablissement = etablissement;
    }

    public Ensegnant etablissement(Etablissement etablissement) {
        this.setEtablissement(etablissement);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ensegnant)) {
            return false;
        }
        return id != null && id.equals(((Ensegnant) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ensegnant{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", email='" + getEmail() + "'" +
            ", tel='" + getTel() + "'" +
            ", tel1='" + getTel1() + "'" +
            "}";
    }
}
