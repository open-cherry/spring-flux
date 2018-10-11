package io.opencherry.springboot.flux.handler;

import io.opencherry.springboot.flux.base.BaseHandler;
import io.opencherry.springboot.flux.model.URI;
import io.opencherry.springboot.flux.model.User;
import io.opencherry.springboot.flux.service.FluxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static io.opencherry.springboot.flux.model.URI.URI_COLLECT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

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
        return post(URI_COLLECT, request -> {
            // TODO Functional Programming Model
            logger.info("http uri: {}", request.uri());

            request.bodyToMono(User.class).subscribe(user -> {
                logger.info("userMono: {}", user);

            });
            Mono<String> result = result = fluxService.getYYInfo();

            Mono<User> newUser = result.map(s -> {
                logger.info("newUser s: {}", s);
                User u = new User();
                u.setId(555l);
                u.setPassword(s);
                return u;
            });
            result.subscribe(s -> logger.info("subscribe1: {}", s));
            result.subscribe(s -> logger.info("subscribe2: {}", s));
            logger.info("end: {}");

            return response(newUser);
        });
    }

}
