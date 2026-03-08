package com.gamyeon.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamyeon.core.response.ApiResponse;
import com.gamyeon.gateway.exception.GatewayException;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

@Component
@Order(-2)
public class GlobalExceptionFilter implements WebExceptionHandler {

    private final ObjectMapper objectMapper;

    public GlobalExceptionFilter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (ex instanceof GatewayException gatewayException) {
            return writeErrorResponse(exchange, gatewayException);
        }
        return Mono.error(ex);
    }

    private Mono<Void> writeErrorResponse(ServerWebExchange exchange, GatewayException ex) {
        ServerHttpResponse response = exchange.getResponse();
        HttpStatus status = ex.getErrorCode().getStatus();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ApiResponse<?> body = ApiResponse.fail(ex.getErrorCode());
        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsBytes(body);
        } catch (JsonProcessingException e) {
            bytes = "{\"success\":false,\"code\":\"INTERNAL_ERROR\",\"message\":\"Internal Server Error\"}".getBytes();
        }

        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }
}
