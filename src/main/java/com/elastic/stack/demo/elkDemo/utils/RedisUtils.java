package com.elastic.stack.demo.elkDemo.utils;

import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by liyang on 2017/8/22.
 */
@Component
public class RedisUtils {

    private final static Log logger = LogFactory.getLog(RedisUtils.class);

    @Autowired
    private RedisTemplate redisTemplate;

    private final static Gson gson = new Gson();

    private final static String CHANNEL = "logstastetseh-redis-test";

    public void publishMsg(Object msgObject) {
        String msg = gson.toJson(msgObject);
        logger.info(msg);
        redisTemplate.convertAndSend(CHANNEL, msg);
    }
}
