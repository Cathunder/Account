package org.example.account.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import redis.embedded.RedisServer;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Configuration
public class LocalRedisConfig {

    // RedisRepositoryConfig 보다 먼저 떠있어야 정상 실행된다.
    // 아래 2가지 방법으로 해결할 수 있다. 이 프로젝트에서는 2번방법이 적용됨
    // 1. LocalRedisConfig가 있는 패키지 이름이 RedisRepositoryConfig가 있는 패키지 이름보다 알파벳 순으로 더 앞에 있어야함
    // 2. 만약 두 파일의 패키지가 동일하면 파일 이름이 알파벳순으로 더 빠른 파일을 먼저 스캔하게 됨

    @Value("${spring.data.redis.port}")     // application.yml에 있는 해당 값을 가져와 redisPort에 저장
    private int redisPort;

    private RedisServer redisServer;

    @PostConstruct      // 스프링빈의 초기화 작업. 해당 빈이 생성된 후 딱 1번 호출. LocalRedisConfig가 빈으로 생성될 때 호출됨
    public void startRedis() throws IOException {
        if (isArmMac()) {
            redisServer = new RedisServer(getRedisFileForArcMac(), redisPort);
        } else {
            redisServer = RedisServer.builder()
                    .port(redisPort)
                    .setting("maxmemory 128M")
                    .build();
        }

        try {
            redisServer.start();
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to start Redis server.", e);
        }
    }

    @PreDestroy         // 스프링빈이 소멸되기 전 한번 호출.
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }

    /*
     * 현재 시스템이 ARM 아키텍처를 사용하는 MAC인지 확인
     * System.getProperty("os.arch") : JVM이 실행되는 시스템 아키텍처 반환
     * System.getProperty("os.name") : 시스템 이름 반환
     */
    private boolean isArmMac() {
        return Objects.equals(System.getProperty("os.arch"), "aarch64")
                && Objects.equals(System.getProperty("os.name"), "Mac OS X");
    }

    /*
     * ARM 아키텍처를 사용하는 Mac에서 실행할 수 있는 Redis 바이너리 파일을 반환
     */
    private File getRedisFileForArcMac() throws IOException {
        try {
            return new ClassPathResource("redis/redis-server-7.2.3-mac-arm64").getFile();
        } catch (IOException e) {
            throw new IOException("Failed to load file", e);
        }
    }
}
