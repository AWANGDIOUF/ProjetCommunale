package agir.gouv.sn.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A InterviewsArtiste.
 */
@Entity
@Table(name = "interviews_artiste")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class InterviewsArtiste implements Serializable {

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

    @Column(name = "lien")
    private String lien;

    @OneToOne
    @JoinColumn(unique = true)
    private Artiste artiste;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InterviewsArtiste id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return this.titre;
    }

    public InterviewsArtiste titre(String titre) {
        this.setTitre(titre);
        return this;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return this.description;
    }

    public InterviewsArtiste description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLien() {
        return this.lien;
    }

    public InterviewsArtiste lien(String lien) {
        this.setLien(lien);
        return this;
    }

    public void setLien(String lien) {
        this.lien = lien;
    }

    public Artiste getArtiste() {
        return this.artiste;
    }

    public void setArtiste(Artiste artiste) {
        this.artiste = artiste;
    }

    public InterviewsArtiste artiste(Artiste artiste) {
        this.setArtiste(artiste);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InterviewsArtiste)) {
            return false;
        }
        return id != null && id.equals(((InterviewsArtiste) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InterviewsArtiste{" +
            "id=" + getId() +
            ", titre='" + getTitre() + "'" +
            ", description='" + getDescription() + "'" +
            ", lien='" + getLien() + "'" +
            "}";
    }
}
