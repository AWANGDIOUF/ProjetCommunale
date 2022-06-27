package agir.gouv.sn.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, agir.gouv.sn.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, agir.gouv.sn.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, agir.gouv.sn.domain.User.class.getName());
            createCache(cm, agir.gouv.sn.domain.Authority.class.getName());
            createCache(cm, agir.gouv.sn.domain.User.class.getName() + ".authorities");
            createCache(cm, agir.gouv.sn.domain.Equipe.class.getName());
            createCache(cm, agir.gouv.sn.domain.Equipe.class.getName() + ".joueurs");
            createCache(cm, agir.gouv.sn.domain.Equipe.class.getName() + ".matches");
            createCache(cm, agir.gouv.sn.domain.Equipe.class.getName() + ".archves");
            createCache(cm, agir.gouv.sn.domain.TypeSport.class.getName());
            createCache(cm, agir.gouv.sn.domain.Club.class.getName());
            createCache(cm, agir.gouv.sn.domain.Club.class.getName() + ".combattants");
            createCache(cm, agir.gouv.sn.domain.Club.class.getName() + ".competitions");
            createCache(cm, agir.gouv.sn.domain.Club.class.getName() + ".archves");
            createCache(cm, agir.gouv.sn.domain.Joueur.class.getName());
            createCache(cm, agir.gouv.sn.domain.Combattant.class.getName());
            createCache(cm, agir.gouv.sn.domain.Combattant.class.getName() + ".vainqueurs");
            createCache(cm, agir.gouv.sn.domain.Quartier.class.getName());
            createCache(cm, agir.gouv.sn.domain.Quartier.class.getName() + ".equipes");
            createCache(cm, agir.gouv.sn.domain.Quartier.class.getName() + ".clubs");
            createCache(cm, agir.gouv.sn.domain.Quartier.class.getName() + ".beneficiaires");
            createCache(cm, agir.gouv.sn.domain.Quartier.class.getName() + ".collecteurOdeurs");
            createCache(cm, agir.gouv.sn.domain.Quartier.class.getName() + ".vidanges");
            createCache(cm, agir.gouv.sn.domain.Quartier.class.getName() + ".recuperationRecyclables");
            createCache(cm, agir.gouv.sn.domain.Quartier.class.getName() + ".etablissements");
            createCache(cm, agir.gouv.sn.domain.Quartier.class.getName() + ".eleveurs");
            createCache(cm, agir.gouv.sn.domain.ArchiveSport.class.getName());
            createCache(cm, agir.gouv.sn.domain.Match.class.getName());
            createCache(cm, agir.gouv.sn.domain.Competition.class.getName());
            createCache(cm, agir.gouv.sn.domain.Competition.class.getName() + ".vainqueurs");
            createCache(cm, agir.gouv.sn.domain.Vainqueur.class.getName());
            createCache(cm, agir.gouv.sn.domain.Don.class.getName());
            createCache(cm, agir.gouv.sn.domain.Don.class.getName() + ".annonces");
            createCache(cm, agir.gouv.sn.domain.Donneur.class.getName());
            createCache(cm, agir.gouv.sn.domain.Donneur.class.getName() + ".dons");
            createCache(cm, agir.gouv.sn.domain.Donneur.class.getName() + ".donSangs");
            createCache(cm, agir.gouv.sn.domain.Beneficiaire.class.getName());
            createCache(cm, agir.gouv.sn.domain.Beneficiaire.class.getName() + ".annonces");
            createCache(cm, agir.gouv.sn.domain.Annonce.class.getName());
            createCache(cm, agir.gouv.sn.domain.Annonce.class.getName() + ".donSangs");
            createCache(cm, agir.gouv.sn.domain.Annonce.class.getName() + ".vaccinations");
            createCache(cm, agir.gouv.sn.domain.Vaccination.class.getName());
            createCache(cm, agir.gouv.sn.domain.Vaccination.class.getName() + ".cibles");
            createCache(cm, agir.gouv.sn.domain.TypeVaccin.class.getName());
            createCache(cm, agir.gouv.sn.domain.Cible.class.getName());
            createCache(cm, agir.gouv.sn.domain.DonSang.class.getName());
            createCache(cm, agir.gouv.sn.domain.ActivitePolitique.class.getName());
            createCache(cm, agir.gouv.sn.domain.SensibiisationInternet.class.getName());
            createCache(cm, agir.gouv.sn.domain.UtilisationInternet.class.getName());
            createCache(cm, agir.gouv.sn.domain.Logiciel.class.getName());
            createCache(cm, agir.gouv.sn.domain.Artiste.class.getName());
            createCache(cm, agir.gouv.sn.domain.InterviewsArtiste.class.getName());
            createCache(cm, agir.gouv.sn.domain.Evenement.class.getName());
            createCache(cm, agir.gouv.sn.domain.Vidange.class.getName());
            createCache(cm, agir.gouv.sn.domain.CollecteurOdeur.class.getName());
            createCache(cm, agir.gouv.sn.domain.RecuperationRecyclable.class.getName());
            createCache(cm, agir.gouv.sn.domain.Etablissement.class.getName());
            createCache(cm, agir.gouv.sn.domain.Etablissement.class.getName() + ".ensegnants");
            createCache(cm, agir.gouv.sn.domain.Etablissement.class.getName() + ".resultatExamen");
            createCache(cm, agir.gouv.sn.domain.ResultatExamen.class.getName());
            createCache(cm, agir.gouv.sn.domain.Proposition.class.getName());
            createCache(cm, agir.gouv.sn.domain.LienTutoriel.class.getName());
            createCache(cm, agir.gouv.sn.domain.Ensegnant.class.getName());
            createCache(cm, agir.gouv.sn.domain.Ensegnant.class.getName() + ".propositions");
            createCache(cm, agir.gouv.sn.domain.Ensegnant.class.getName() + ".lienTutoriels");
            createCache(cm, agir.gouv.sn.domain.Entreprenariat.class.getName());
            createCache(cm, agir.gouv.sn.domain.Entrepreneur.class.getName());
            createCache(cm, agir.gouv.sn.domain.DemandeInterview.class.getName());
            createCache(cm, agir.gouv.sn.domain.DomaineActivite.class.getName());
            createCache(cm, agir.gouv.sn.domain.Partenaires.class.getName());
            createCache(cm, agir.gouv.sn.domain.Eleveur.class.getName());
            createCache(cm, agir.gouv.sn.domain.Inscription.class.getName());
            createCache(cm, agir.gouv.sn.domain.CalendrierEvenement.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
