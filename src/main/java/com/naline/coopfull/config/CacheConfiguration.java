package com.naline.coopfull.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.boot.cache.autoconfigure.JCacheManagerCustomizer;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.config.JHipsterProperties;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        var ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries())
            )
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
            createCache(cm, com.naline.coopfull.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.naline.coopfull.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.naline.coopfull.domain.User.class.getName());
            createCache(cm, com.naline.coopfull.domain.Authority.class.getName());
            createCache(cm, com.naline.coopfull.domain.User.class.getName() + ".authorities");
            createCache(cm, com.naline.coopfull.domain.Cooperative.class.getName());
            createCache(cm, com.naline.coopfull.domain.Location.class.getName());
            createCache(cm, com.naline.coopfull.domain.CooperativeBranch.class.getName());
            createCache(cm, com.naline.coopfull.domain.Member.class.getName());
            createCache(cm, com.naline.coopfull.domain.IndividualMember.class.getName());
            createCache(cm, com.naline.coopfull.domain.OrganizationMember.class.getName());
            createCache(cm, com.naline.coopfull.domain.MembershipApplication.class.getName());
            createCache(cm, com.naline.coopfull.domain.SocialProfile.class.getName());
            createCache(cm, com.naline.coopfull.domain.FamilyMember.class.getName());
            createCache(cm, com.naline.coopfull.domain.ProfessionalProfile.class.getName());
            createCache(cm, com.naline.coopfull.domain.IdentityDocument.class.getName());
            createCache(cm, com.naline.coopfull.domain.MemberDocument.class.getName());
            createCache(cm, com.naline.coopfull.domain.EconomicActivityType.class.getName());
            createCache(cm, com.naline.coopfull.domain.EconomicActivity.class.getName());
            createCache(cm, com.naline.coopfull.domain.AgriculturalActivity.class.getName());
            createCache(cm, com.naline.coopfull.domain.AgriculturalActivity.class.getName() + ".productionses");
            createCache(cm, com.naline.coopfull.domain.Crop.class.getName());
            createCache(cm, com.naline.coopfull.domain.CropVariety.class.getName());
            createCache(cm, com.naline.coopfull.domain.AgriculturalProduction.class.getName());
            createCache(cm, com.naline.coopfull.domain.LivestockType.class.getName());
            createCache(cm, com.naline.coopfull.domain.LivestockActivity.class.getName());
            createCache(cm, com.naline.coopfull.domain.LivestockActivity.class.getName() + ".productionses");
            createCache(cm, com.naline.coopfull.domain.LivestockProduction.class.getName());
            createCache(cm, com.naline.coopfull.domain.AquaticSpecies.class.getName());
            createCache(cm, com.naline.coopfull.domain.AquacultureActivity.class.getName());
            createCache(cm, com.naline.coopfull.domain.AquacultureActivity.class.getName() + ".productionses");
            createCache(cm, com.naline.coopfull.domain.AquacultureProduction.class.getName());
            createCache(cm, com.naline.coopfull.domain.AppUser.class.getName());
            createCache(cm, com.naline.coopfull.domain.CooperativeRole.class.getName());
            createCache(cm, com.naline.coopfull.domain.CooperativeRole.class.getName() + ".permissionses");
            createCache(cm, com.naline.coopfull.domain.CooperativeUser.class.getName());
            createCache(cm, com.naline.coopfull.domain.BranchUser.class.getName());
            createCache(cm, com.naline.coopfull.domain.Permission.class.getName());
            createCache(cm, com.naline.coopfull.domain.Permission.class.getName() + ".roleses");
            createCache(cm, com.naline.coopfull.domain.AuditLog.class.getName());
            createCache(cm, com.naline.coopfull.domain.NumberSequence.class.getName());
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
}
