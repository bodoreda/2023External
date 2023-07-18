package msa.external.v1.common.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import msa.external.v1.common.model.UserInfo;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * packageName : msa.external.v1.redis.util
 * fileName : RedisUtils
 * author : BH
 * date : 2023-07-04
 * description :
 * ================================================
 * DATE                AUTHOR              NOTE
 * ================================================
 * 2023-07-04       RedisUtils       최초 생성
 */

@Component
@RequiredArgsConstructor
@Log4j2
public class RedisUtils {
    private final RedisTemplate<String, Object> redisTemplate;

    public void setData(String key, String value, Long expiredTime){
        log.info("setData // key : {} , value : {}", key, value);
        redisTemplate.opsForValue().set(key, value, expiredTime, TimeUnit.MILLISECONDS);
    }

    public void setUserInfo(String key, UserInfo userInfo, Long expiredTime) {
        log.info("setUserInfo // key : {} , value : {}", key, userInfo);
        redisTemplate.opsForValue().set(key, userInfo, expiredTime, TimeUnit.MILLISECONDS);
    }

    public String getData(String key){
        return (String) redisTemplate.opsForValue().get(key);
    }

    public UserInfo getUserInfo(String key) {
        return (UserInfo) redisTemplate.opsForValue().get(key);
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }
}
