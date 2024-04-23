package org.example.account.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.account.config.RedisRepositoryConfig;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisTestService {

    private final RedissonClient redissonClient;    // redissonClient라는 이름을 가진 빈이 생성자로 주입됨

    public String getLock() {
        RLock lock = redissonClient.getLock("sampleLock");

        // 스핀락 시도
        try {
            // 최대 1초동안 락을 찾음. 락을 획득하면 5초동안 가지고 있다가 풀어줌
            boolean isLock = lock.tryLock(1, 5, TimeUnit.SECONDS);
            if (!isLock) {
                log.error("================ Lock acquisition failed ================");
                return "Lock failed";
            }
        } catch (Exception e) {
            log.error("Redis lock failed");
        }

        return "Lock success";
    }
}
