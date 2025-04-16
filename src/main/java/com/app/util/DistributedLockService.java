package com.app.util;


import com.app.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 分布式锁服务
 *
 * @author yoominic
 * @date 2024/03/14
 */
@Service
public class DistributedLockService {

    private static final int LOCK_EXPIRE_TIME_SECONDS = 5;
    @Autowired
    private RedisService redisService; // 假设这是正确的类名

    public boolean acquireLock(String lockKey, String requestId) {
        return redisService.setIfAbsent(lockKey, requestId, LOCK_EXPIRE_TIME_SECONDS);
    }

    public void releaseLock(String lockKey, String requestId) {
        redisService.delIfEquals(lockKey, requestId);
    }
}
