package com.elastic.stack.demo.elkDemo.controller;

import com.elastic.stack.demo.elkDemo.ElkDemoApplication;
import com.elastic.stack.demo.elkDemo.domain.MsgObject;
import com.elastic.stack.demo.elkDemo.utils.RedisUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liyang on 2017/8/22.
 */
@RestController
public class LogController {

    private final static Log logger = LogFactory.getLog(ElkDemoApplication.class);

    private final static Logger sl4jLogger = LoggerFactory.getLogger(ElkDemoApplication.class);

    private static Integer count = 0;

    @Autowired
    private RedisUtils redisUtils;

    @RequestMapping(value = "hello", method = RequestMethod.GET)
    @ResponseBody
    public String hello() {
        logger.info("request: ===[ " + ++count + " ]===");
        logger.info("response: ===[ " + count + " ]===");
        logger.info("test4ekl: ===[ " + count + " ]===");

        MsgObject msgObject = new MsgObject("Jim Green", true, 27);
        redisUtils.publishMsg(msgObject);
        return count.toString() ;
    }

    @RequestMapping(value = "stdin", method = RequestMethod.GET)
    public void stdin() {
        System.out.println("fsdfdsfdsf------------fsdfds:fsdf");
    }

    @RequestMapping(value = "log", method = RequestMethod.GET)
    public void log() {
        //logger.info("fsdfdsfdsf------------fsdfds:fsdf");
        while(count < 10000) {
            sl4jLogger.info("这是一个{}的早上,{}伴随着{}的号令, count:{}", "晴朗", "歌唱声", "起床", count++);
        }
        count = 0;

    }
}
