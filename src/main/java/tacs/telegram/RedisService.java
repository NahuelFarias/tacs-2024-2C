package tacs.telegram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Service("telegramRedisService")
public class RedisService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void storeToken(String key, String token) {
        redisTemplate.opsForValue().set(key, token, Duration.ofMinutes(15));
    }

    public String getToken(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteToken(String key) {
        redisTemplate.delete(key);
    }
} 