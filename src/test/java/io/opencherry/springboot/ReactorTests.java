package io.opencherry.springboot;

import io.opencherry.springboot.flux.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(JUnit4.class)
public class ReactorTests {

    protected static Logger logger = LoggerFactory.getLogger(ReactorTests.class);

    private  Map<Long, User> users = new HashMap<>();

    @Before
    public void before() {
        logger.info("before------------------");
        User user = new User();
        user.setId(100l);
        user.setUsername("simon");
        users.put(user.getId(), user);

        user = new User();
        user.setId(101l);
        user.setUsername("simon1");
        users.put(user.getId(), user);
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

    @Test
    public void fluxAsyncTest() throws Exception {

        CountDownLatch countDownLatch = new CountDownLatch(1);

        Flux<Long> userFlux = Flux.just(100l, 101l);

        Flux<User> combinations = userFlux.flatMap(id -> {
            logger.info("in flatMap id: {}", id);
            Mono<User> mono = Mono.fromCallable(() -> getUserAsync(id))    // 1
                    .subscribeOn(Schedulers.elastic());  // 2

            Mono<String> mono1 = Mono.fromCallable(() -> getPasswordAsync())    // 1
                    .subscribeOn(Schedulers.elastic());  // 2

            return mono.zipWith(mono1, (u, p) -> {
                logger.info("in zipWith!");
                u.setPassword(p);
                return u;
            });
        });

        combinations.subscribe(s -> logger.info("mono: {}", s ), null, countDownLatch::countDown);
        countDownLatch.await(10, TimeUnit.SECONDS);
    }

    private User getUserAsync(Long id) {
        logger.info("in getUserAsync!");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return users.get(id);
    }

    private String getPasswordAsync() {
        logger.info("in getPasswordAsync!");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "124555";
    }

    private String getStringSync() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Hello, Reactor!";
    }

    @Test
    public void testSyncToAsync() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Mono<String> mono = Mono.fromCallable(() -> getStringSync())    // 1
                .subscribeOn(Schedulers.elastic());  // 2
        mono.subscribe(s -> logger.info("mono: {}", s ), null, countDownLatch::countDown);
        countDownLatch.await(10, TimeUnit.SECONDS);
    }

}
