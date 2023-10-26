package com.ruoyi.gateway.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.stereotype.Component;
import com.ruoyi.common.core.utils.ServletUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 黑名单过滤器
 * 
 * @author ruoyi
 */
@Component
public class BlackListUrlFilter implements GatewayFilter {
    private static final Logger logger = LoggerFactory.getLogger(BlackListUrlFilter.class);

    private List<Pattern> blacklistUrlPattern = new ArrayList<>();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String url = exchange.getRequest().getURI().getPath();
        if (matchBlacklist(url)) {
            return ServletUtils.webFluxResponseWriter(exchange.getResponse(), "请求地址不允许访问");
        }
        return chain.filter(exchange);
    }

    public boolean matchBlacklist(String url) {
        return !blacklistUrlPattern.isEmpty() && blacklistUrlPattern.stream().anyMatch(p -> p.matcher(url).find());
    }

    public void setBlacklistUrl(List<String> blacklistUrl) {
        this.blacklistUrlPattern.clear();
        blacklistUrl.forEach(url -> {
            this.blacklistUrlPattern.add(Pattern.compile(url.replaceAll("\\*\\*", "(.*?)"), Pattern.CASE_INSENSITIVE));
        });
    }
}