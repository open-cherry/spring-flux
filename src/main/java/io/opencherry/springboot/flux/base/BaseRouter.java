package io.opencherry.springboot.flux.base;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * @author <a href="mailto:simling82@gmail.com">Simling</a>
 * @version v1.0 on 2018/9/29
 */
public abstract class BaseRouter {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected WebClient client = WebClient.create("http://wap.yy.com");

    protected RouterFunction<ServerResponse> get(String uri, HandlerFunction<ServerResponse> handlerFunction) {
        return route(GET(uri).and(accept(APPLICATION_JSON)), handlerFunction);
    }

    protected RouterFunction<ServerResponse> post(String uri, HandlerFunction<ServerResponse> handlerFunction) {
        return route(POST(uri).and(accept(APPLICATION_JSON)), handlerFunction);
    }

    protected Mono<ServerResponse> response(BodyInserter inserter) {
        return ok().contentType(APPLICATION_JSON)
                .body(inserter);
    }

    protected Mono<ServerResponse> response(Publisher body) {
        return ok().contentType(APPLICATION_JSON).body(body, body.getClass());
    }

    protected Mono<ServerResponse> response(Object body) {
        return response(fromObject(body));
    }
}
