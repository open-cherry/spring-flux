package io.opencherry.springboot.flux.router;

import io.opencherry.springboot.flux.base.BaseRouter;
import io.opencherry.springboot.flux.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:simling82@gmail.com">Simling</a>
 * @version v1.0 on 2018/9/28
 */
@Configuration
public class FluxRouter extends BaseRouter {

    public static final String URI_COLLECT = "/collect";

    @Bean
    public RouterFunction<ServerResponse> collect() {
        return post(URI_COLLECT, request -> {
            // TODO Functional Programming Model
            logger.info("http uri: {}", request.uri());
            Mono<User> userMono = request.bodyToMono(User.class);
            userMono.subscribe(user -> logger.info("userMono: {}", user));
            // TODO service
            Map<String, String> map = new HashMap<String, String>();
            map.put("uid", "1");
            Mono<String> result = client.get()
                    .uri("/mobileweb/play/getUserRecords?uid={0}", 1).accept(MediaType.APPLICATION_JSON_UTF8)
                    .retrieve()
                    .bodyToMono(String.class);
            Mono<User> newUser = null;
            result.subscribe(s -> {
                logger.info("s: {}", s);
                User user = new User();
                user.setId(555l);
                user.setUsername(s);
                Mono<User> t = Mono.just(user);
                t.and(result);
            });

            logger.info("end: {}");

            return response(result);
//            return response(user);
//            return response(t);
        });
    }


}
