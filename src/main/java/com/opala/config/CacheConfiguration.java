package com.opala.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(com.opala.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(com.opala.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(com.opala.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(com.opala.domain.PersistentToken.class.getName(), jcacheConfiguration);
            cm.createCache(com.opala.domain.User.class.getName() + ".persistentTokens", jcacheConfiguration);
            cm.createCache(com.opala.domain.SocialUserConnection.class.getName(), jcacheConfiguration);
            cm.createCache(com.opala.domain.Agendamento.class.getName(), jcacheConfiguration);
            cm.createCache(com.opala.domain.Agendamento.class.getName() + ".listaPassageiros", jcacheConfiguration);
            cm.createCache(com.opala.domain.Agendamento.class.getName() + ".listaDestinos", jcacheConfiguration);
            cm.createCache(com.opala.domain.Cliente.class.getName(), jcacheConfiguration);
            cm.createCache(com.opala.domain.Cliente.class.getName() + ".listaSolicitantes", jcacheConfiguration);
            cm.createCache(com.opala.domain.Motorista.class.getName(), jcacheConfiguration);
            cm.createCache(com.opala.domain.Passageiro.class.getName(), jcacheConfiguration);
            cm.createCache(com.opala.domain.Veiculo.class.getName(), jcacheConfiguration);
            cm.createCache(com.opala.domain.Solicitante.class.getName(), jcacheConfiguration);
            cm.createCache(com.opala.domain.Solicitante.class.getName() + ".listaAgendamentos", jcacheConfiguration);
            cm.createCache(com.opala.domain.Itinerario.class.getName(), jcacheConfiguration);
            cm.createCache(com.opala.domain.Endereco.class.getName(), jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
