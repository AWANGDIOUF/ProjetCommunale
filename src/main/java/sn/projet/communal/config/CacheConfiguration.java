package sn.projet.communal.config;

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
            createCache(cm, sn.projet.communal.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, sn.projet.communal.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, sn.projet.communal.domain.User.class.getName());
            createCache(cm, sn.projet.communal.domain.Authority.class.getName());
            createCache(cm, sn.projet.communal.domain.User.class.getName() + ".authorities");
            createCache(cm, sn.projet.communal.domain.Equipe.class.getName());
            createCache(cm, sn.projet.communal.domain.Equipe.class.getName() + ".quartiers");
            createCache(cm, sn.projet.communal.domain.Equipe.class.getName() + ".matches");
            createCache(cm, sn.projet.communal.domain.Equipe.class.getName() + ".archves");
            createCache(cm, sn.projet.communal.domain.TypeSport.class.getName());
            createCache(cm, sn.projet.communal.domain.Club.class.getName());
            createCache(cm, sn.projet.communal.domain.Club.class.getName() + ".quartiers");
            createCache(cm, sn.projet.communal.domain.Club.class.getName() + ".competitions");
            createCache(cm, sn.projet.communal.domain.Club.class.getName() + ".archves");
            createCache(cm, sn.projet.communal.domain.Joueur.class.getName());
            createCache(cm, sn.projet.communal.domain.Joueur.class.getName() + ".equipes");
            createCache(cm, sn.projet.communal.domain.Combattant.class.getName());
            createCache(cm, sn.projet.communal.domain.Combattant.class.getName() + ".clubs");
            createCache(cm, sn.projet.communal.domain.Quartier.class.getName());
            createCache(cm, sn.projet.communal.domain.ArchiveSport.class.getName());
            createCache(cm, sn.projet.communal.domain.ArchiveSport.class.getName() + ".equipes");
            createCache(cm, sn.projet.communal.domain.ArchiveSport.class.getName() + ".clubs");
            createCache(cm, sn.projet.communal.domain.Match.class.getName());
            createCache(cm, sn.projet.communal.domain.Match.class.getName() + ".equipes");
            createCache(cm, sn.projet.communal.domain.Competition.class.getName());
            createCache(cm, sn.projet.communal.domain.Competition.class.getName() + ".clubs");
            createCache(cm, sn.projet.communal.domain.Vainqueur.class.getName());
            createCache(cm, sn.projet.communal.domain.Vainqueur.class.getName() + ".competitions");
            createCache(cm, sn.projet.communal.domain.Vainqueur.class.getName() + ".combattants");
            createCache(cm, sn.projet.communal.domain.Don.class.getName());
            createCache(cm, sn.projet.communal.domain.Don.class.getName() + ".donneurs");
            createCache(cm, sn.projet.communal.domain.Donneur.class.getName());
            createCache(cm, sn.projet.communal.domain.Donneur.class.getName() + ".donSangs");
            createCache(cm, sn.projet.communal.domain.Beneficiaire.class.getName());
            createCache(cm, sn.projet.communal.domain.Beneficiaire.class.getName() + ".quartiers");
            createCache(cm, sn.projet.communal.domain.Annonce.class.getName());
            createCache(cm, sn.projet.communal.domain.Annonce.class.getName() + ".dons");
            createCache(cm, sn.projet.communal.domain.Annonce.class.getName() + ".beneficiaires");
            createCache(cm, sn.projet.communal.domain.Annonce.class.getName() + ".donSangs");
            createCache(cm, sn.projet.communal.domain.Annonce.class.getName() + ".vaccinations");
            createCache(cm, sn.projet.communal.domain.Vaccination.class.getName());
            createCache(cm, sn.projet.communal.domain.Vaccination.class.getName() + ".cibles");
            createCache(cm, sn.projet.communal.domain.TypeVaccin.class.getName());
            createCache(cm, sn.projet.communal.domain.Cible.class.getName());
            createCache(cm, sn.projet.communal.domain.DonSang.class.getName());
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
