package io.opencherry.springboot.flux.service;

import io.opencherry.springboot.flux.model.URI;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class FluxService {

    protected WebClient client = WebClient.create(URI.HOST);

    public Mono<String> getYYInfo(Long id) {
        Mono<String> result = client.get()
                .uri(URI.URI_YY, 1)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .retrieve()
                .bodyToMono(String.class);
        return result;
    }
}
