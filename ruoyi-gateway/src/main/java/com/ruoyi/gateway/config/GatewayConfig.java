package com.ruoyi.gateway.config;

import com.ruoyi.gateway.filter.CacheRequestFilter;
import com.ruoyi.gateway.filter.ValidateCodeFilter;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import com.ruoyi.gateway.handler.SentinelFallbackHandler;

/**
 * 网关限流配置
 *
 * @author ruoyi
 */
@Configuration
public class GatewayConfig {
    @Resource
    private RouteLocatorBuilder builder;
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelFallbackHandler sentinelGatewayExceptionHandler() {
        return new SentinelFallbackHandler();
    }

    @Autowired
    private ValidateCodeFilter validateCodeFilter;
    @Autowired
    private CacheRequestFilter cacheRequestFilter;
    @Bean
    public RouteLocator routes() {
        return builder.routes()
                .route("ruoyi-auth", r -> r.path("/auth/**")
                        .filters(f -> f.filters(cacheRequestFilter)
                                .filter(validateCodeFilter)
                                .stripPrefix(1))
                        .uri("lb://ruoyi-auth")
                )
                .route("ruoyi-code", r -> r.path("/code/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://ruoyi-gen")
                )
                .route("ruoyi-schedule", r -> r.path("/schedule/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://ruoyi-job")
                )
                .route("ruoyi-system", r -> r.path("/system/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://ruoyi-system")
                )
                .route("ruoyi-file", r -> r.path("/file/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("lb://ruoyi-file")
                )
                .route("openapi", r -> r.path("/v3/api-docs/**")
                        .filters(f -> f.rewritePath("/v3/api-docs/(?<path>.*)", "/${path}/v3/api-docs"))
                        .uri("http://localhost:8080"))
                .build();
    }
}