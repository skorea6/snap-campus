package com.example.snapcampus.redis.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Repository
public class RefreshTokenInfoRepositoryRedis {

    private static final String KEY_PREFIX = "refreshToken"; // refreshToken:{userId}:{refreshToken}
    private static final Long REFRESH_EXPIRATION_MILLISECONDS = 1000L * 60 * 60 * 24 * 30; // 30 days

    private final RedisTemplate<Object, Object> redisTemplate;

    public RefreshTokenInfoRepositoryRedis(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.redisTemplate.setKeySerializer(new StringRedisSerializer());
        this.redisTemplate.setValueSerializer(new StringRedisSerializer());
    }

    public void save(String userId, String refreshToken) {
        String key = KEY_PREFIX + ":" + userId + ":" + refreshToken;
        redisTemplate.opsForValue().set(key, "", REFRESH_EXPIRATION_MILLISECONDS, TimeUnit.MILLISECONDS);
    }

    public String findByRefreshToken(String refreshToken) {
        Set<Object> keysRaw = redisTemplate.keys(KEY_PREFIX + ":*:" + refreshToken);
        Set<String> keys = keysRaw.stream().map(Object::toString).collect(Collectors.toSet());
        String key = keys.stream().findFirst().orElse(null);
        return key != null ? (String) redisTemplate.opsForValue().get(key) : null;
    }

    public void deleteByRefreshToken(String refreshToken) {
        Set<Object> keysRaw = redisTemplate.keys(KEY_PREFIX + ":*:" + refreshToken);
        Set<String> keys = keysRaw.stream().map(Object::toString).collect(Collectors.toSet());
        redisTemplate.delete(keys);
    }

    public void deleteByUserId(String userId) {
        Set<Object> keysRaw = redisTemplate.keys(KEY_PREFIX + ":" + userId + ":*");
        Set<String> keys = keysRaw.stream().map(Object::toString).collect(Collectors.toSet());
        redisTemplate.delete(keys);
    }
}
