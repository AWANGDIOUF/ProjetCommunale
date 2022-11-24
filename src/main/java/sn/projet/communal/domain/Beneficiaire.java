package sn.projet.communal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import sn.projet.communal.domain.enumeration.TypeBeneficiaire;
import sn.projet.communal.domain.enumeration.TypeMoral;

/**
 * A Beneficiaire.
 */
@Entity
@Table(name = "beneficiaire")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Beneficiaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_benefiaire")
    private TypeBeneficiaire typeBenefiaire;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_perso_moral")
    private TypeMoral typePersoMoral;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "nom")
    private String nom;

    @Column(name = "cin", unique = true)
    private String cin;

    @Column(name = "adresse")
    private String adresse;

    @NotNull
    @Column(name = "tel_1", nullable = false, unique = true)
    private String tel1;

    @Column(name = "autretel_1", unique = true)
    private String autretel1;

    @Column(name = "email_association", unique = true)
    private String emailAssociation;

    @Column(name = "nom_president")
    private String nomPresident;

    @Lob
    @Column(name = "description")
    private String description;

    @ManyToOne
    @JsonIgnoreProperties(value = { "dons", "beneficiaires", "donSangs", "vaccinations" }, allowSetters = true)
    private Annonce annonce;

    @OneToMany(mappedBy = "beneficiaire")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "equipe", "club", "beneficiaire" }, allowSetters = true)
    private Set<Quartier> quartiers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Beneficiaire id(Long id) {
        this.id = id;
        return this;
    }

    public TypeBeneficiaire getTypeBenefiaire() {
        return this.typeBenefiaire;
    }

    public Beneficiaire typeBenefiaire(TypeBeneficiaire typeBenefiaire) {
        this.typeBenefiaire = typeBenefiaire;
        return this;
    }

    public void setTypeBenefiaire(TypeBeneficiaire typeBenefiaire) {
        this.typeBenefiaire = typeBenefiaire;
    }

    public TypeMoral getTypePersoMoral() {
        return this.typePersoMoral;
    }

    public Beneficiaire typePersoMoral(TypeMoral typePersoMoral) {
        this.typePersoMoral = typePersoMoral;
        return this;
    }

    public void setTypePersoMoral(TypeMoral typePersoMoral) {
        this.typePersoMoral = typePersoMoral;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Beneficiaire prenom(String prenom) {
        this.prenom = prenom;
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return this.nom;
    }

    public Beneficiaire nom(String nom) {
        this.nom = nom;
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCin() {
        return this.cin;
    }

    public Beneficiaire cin(String cin) {
        this.cin = cin;
        return this;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getAdresse() {
        return this.adresse;
    }

    public Beneficiaire adresse(String adresse) {
        this.adresse = adresse;
        return this;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTel1() {
        return this.tel1;
    }

    public Beneficiaire tel1(String tel1) {
        this.tel1 = tel1;
        return this;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    public String getAutretel1() {
        return this.autretel1;
    }

    public Beneficiaire autretel1(String autretel1) {
        this.autretel1 = autretel1;
        return this;
    }

    public void setAutretel1(String autretel1) {
        this.autretel1 = autretel1;
    }

    public String getEmailAssociation() {
        return this.emailAssociation;
    }

    public Beneficiaire emailAssociation(String emailAssociation) {
        this.emailAssociation = emailAssociation;
        return this;
    }

    public void setEmailAssociation(String emailAssociation) {
        this.emailAssociation = emailAssociation;
    }

    public String getNomPresident() {
        return this.nomPresident;
    }

    public Beneficiaire nomPresident(String nomPresident) {
        this.nomPresident = nomPresident;
        return this;
    }

    public void setNomPresident(String nomPresident) {
        this.nomPresident = nomPresident;
    }

    public String getDescription() {
        return this.description;
    }

    public Beneficiaire description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Annonce getAnnonce() {
        return this.annonce;
    }

    public Beneficiaire annonce(Annonce annonce) {
        this.setAnnonce(annonce);
        return this;
    }

    public void setAnnonce(Annonce annonce) {
        this.annonce = annonce;
    }

    public Set<Quartier> getQuartiers() {
        return this.quartiers;
    }

    public Beneficiaire quartiers(Set<Quartier> quartiers) {
        this.setQuartiers(quartiers);
        return this;
    }

    public Beneficiaire addQuartier(Quartier quartier) {
        this.quartiers.add(quartier);
        quartier.setBeneficiaire(this);
        return this;
    }

    public Beneficiaire removeQuartier(Quartier quartier) {
        this.quartiers.remove(quartier);
        quartier.setBeneficiaire(null);
        return this;
    }

    public void setQuartiers(Set<Quartier> quartiers) {
        if (this.quartiers != null) {
            this.quartiers.forEach(i -> i.setBeneficiaire(null));
        }
        if (quartiers != null) {
            quartiers.forEach(i -> i.setBeneficiaire(this));
        }
        this.quartiers = quartiers;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Beneficiaire)) {
            return false;
        }
        return id != null && id.equals(((Beneficiaire) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Beneficiaire{" +
            "id=" + getId() +
            ", typeBenefiaire='" + getTypeBenefiaire() + "'" +
            ", typePersoMoral='" + getTypePersoMoral() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", nom='" + getNom() + "'" +
            ", cin='" + getCin() + "'" +
            ", adresse='" + getAdresse() + "'" +
            ", tel1='" + getTel1() + "'" +
            ", autretel1='" + getAutretel1() + "'" +
            ", emailAssociation='" + getEmailAssociation() + "'" +
            ", nomPresident='" + getNomPresident() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
