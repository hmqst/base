package com.example.base.config.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author benben
 * @program base
 * @Description RedisKey失效监听器
 * @date 2021-03-22 15:50
 */
@Slf4j
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    /**
     * Creates new {@link RedisMessageListenerContainer} for {@code __keyevent@*__:expired} messages.
     *
     * @param listenerContainer must not be {@literal null}.
     */
    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 暂停耗材计时
        String key = message.toString();
        log.info("Redis中Key：" + key + " 已失效");
        super.onMessage(message, pattern);
    }
}
