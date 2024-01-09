package com.mintyn.cardservice.apiCall;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebClientCall {

    @Value("${application.http.callout.api.bin.list.endpoint.url}")
    private String BIN_LIST_ENDPOINT;

    public String cardServiceApiCall(String request) {

        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000)
                .responseTimeout(Duration.ofSeconds(30))
                .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(30, TimeUnit.SECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(30, TimeUnit.SECONDS)));

        WebClient client = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
        WebClient.UriSpec<WebClient.RequestBodySpec> uriSpec = client.method(HttpMethod.POST);
        WebClient.RequestBodySpec bodySpec = uriSpec.uri(BIN_LIST_ENDPOINT + request);
        WebClient.RequestHeadersSpec<?> headersSpec = bodySpec.bodyValue(request);
        headersSpec.header(HttpHeaders.CONTENT_TYPE, String.valueOf(MediaType.TEXT_XML));

        Mono<String> nibssTransferResponse = headersSpec.exchangeToMono(response -> {
            if (response.statusCode().equals(HttpStatus.OK)) {
                log.info("200 ok response");
                return response.bodyToMono(String.class);
            } else if (response.statusCode().is4xxClientError()) {
                log.info("400 error response");
                return Mono.just("400xx");
            } else if (response.statusCode().is5xxServerError()) {
                log.info("500 error response");
                return Mono.just("500xx");
            } else {
                log.info("other error types");
                return Mono.just("900xx");
            }
        });
        return nibssTransferResponse.block();
    }
}
