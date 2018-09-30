package io.opencherry.springboot.flux.handler;

import io.opencherry.springboot.flux.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * @author <a href="mailto:simling82@gmail.com">Simling</a>
 * @version v1.0 on 2018/9/28
 */
@Component
public class FluxHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public Mono<ServerResponse> collect(ServerRequest request) {
        // TODO Functional Programming Model
        logger.info("http uri: {}", request.uri());
        Mono<User> userMono = request.bodyToMono(User.class);
        return ok().contentType(APPLICATION_JSON)
                .body(fromObject("Hi , this is SpringWebFlux"));
    }

}
