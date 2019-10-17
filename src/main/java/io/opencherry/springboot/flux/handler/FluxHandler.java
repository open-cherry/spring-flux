package io.opencherry.springboot.flux.handler;

import io.opencherry.springboot.flux.base.BaseHandler;
import io.opencherry.springboot.flux.model.User;
import io.opencherry.springboot.flux.service.FluxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static io.opencherry.springboot.flux.model.URI.URI_COLLECT;

/**
 * @author <a href="mailto:simling82@gmail.com">Simling</a>
 * @version v1.0 on 2018/9/28
 */
@Configuration
public class FluxHandler extends BaseHandler {

    @Autowired
    private FluxService fluxService;

    @Bean
    public RouterFunction<ServerResponse> collect() {
        RouterFunction<ServerResponse> function = post(URI_COLLECT, request -> {
            // Functional Programming Model
            logger.info("http uri: {}", request.uri());

            Mono<User> newUser = request.bodyToMono(User.class).flatMap(user -> { // 异步消费请求user
                logger.info("userMono: {}", user);
                Mono<String> result = fluxService.getYYInfo(user.getId()); // 异步获取用户
                return result.map(s -> { // 组装用户数据，返回
                    logger.info("newUser s: {}", s);
                    user.setPassword(s);
                    return user;
                });
            });

            return response(newUser);
        });
        return function;
    }
}
