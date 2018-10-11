package io.opencherry.springboot.flux.base;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

public class BaseHandler {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected RouterFunction<ServerResponse> get(String uri, HandlerFunction<ServerResponse> handlerFunction) {
        return route(GET(uri).and(accept(APPLICATION_JSON_UTF8)), handlerFunction);
    }

    protected RouterFunction<ServerResponse> post(String uri, HandlerFunction<ServerResponse> handlerFunction) {
        return route(POST(uri).and(accept(APPLICATION_JSON_UTF8)), handlerFunction);
    }

    protected Mono<ServerResponse> response(BodyInserter inserter) {
        return ok().contentType(APPLICATION_JSON_UTF8)
                .body(inserter);
    }

    protected Mono<ServerResponse> response(Publisher body) {
        return ok().contentType(APPLICATION_JSON_UTF8).body(body, body.getClass());
    }

    protected Mono<ServerResponse> response(Object body) {
        return response(fromObject(body));
    }
}
