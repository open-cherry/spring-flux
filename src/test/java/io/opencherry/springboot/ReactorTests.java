package io.opencherry.springboot;

import io.opencherry.springboot.flux.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

@RunWith(JUnit4.class)
public class ReactorTests {

    protected static Logger logger = LoggerFactory.getLogger(ReactorTests.class);

    @Before
    public void before() {
        logger.info("before------------------");
    }

    @After
    public void after() {
        logger.info("after------------------");
    }

    @Test
    public void monoTest() {
        User user = new User();
        user.setId(100l);
        user.setUsername("simon");


        Mono<User> mono = Mono.just(user);

        // 订阅并触发数据流
        mono.subscribe(i -> {
            User user1 = new User();
            user1.setId(101l);
            user1.setUsername("linson");
            logger.info("mono subscribe1: {}", i);
        });

        // 订阅并定义对正常数据元素和错误信号的处理
        mono.subscribe(i -> {
            logger.info("mono subscribe2: {}", i);
        }, e -> {
            logger.error("mono subscribe2 error: ", e);
        });

        // 订阅并定义对正常数据元素、错误信号和完成信号的处理
        mono.subscribe(i -> {
            logger.info("mono subscribe3: {}", i);
        }, e -> {
            logger.error("mono subscribe3 error: ", e);
        }, () -> {
            logger.info("mono subscribe3 complete");
        });

        // 订阅并定义对正常数据元素、错误信号和完成信号的处理，以及订阅发生时的处理逻辑
        mono.subscribe(i -> {
            logger.info("mono subscribe4: {}", i);
        }, e -> {
            logger.error("mono subscribe4 error: ", e);
        }, () -> {
            logger.info("mono subscribe4 complete");
        }, s -> {
            logger.info("mono on subscribe4 ");
            s.request(1);
        });

    }

    @Test
    public void monoAsync() throws Exception {
        User user = new User();
        user.setId(100l);
        user.setUsername("simon");


//        Mono<User> mono = Mono.just(user);
//
//        Mono.fromCallable(() -> "callable run ").subscribe(System.out::println);
        Mono<User> mono = Mono.just(user);
        mono.subscribe(i -> {
            logger.info("mono subscribe: {}", i);
        });
        Thread t = new Thread(() -> {
            mono.subscribe(i -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i.setPassword("1234");
            });
        });

        t.setName("thread-0");
        t.start();
        logger.info("mono: {}", user);
//        t.join();
        mono.subscribe(i -> {
            logger.info("mono subscribe1: {}", i);
        });
        logger.info("mono: {}", user);
    }


}
