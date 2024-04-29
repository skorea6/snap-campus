package com.example.snapcampus.common.redis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        LettuceClientConfiguration lettuceClientConfiguration = LettuceClientConfiguration.builder()
                .shutdownTimeout(Duration.ZERO)
                .build();
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(host, port);
        return new LettuceConnectionFactory(redisStandaloneConfiguration, lettuceClientConfiguration);
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate() {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(lettuceConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    public CacheManager cacheManager() {
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .entryTtl(Duration.ofMinutes(2))
                .disableCachingNullValues();
        return RedisCacheManager.builder(lettuceConnectionFactory())
                .cacheDefaults(configuration)
                .build();
    }
}
