package com.ruoyi.gateway.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SwaggerConfig {
    /**
     * 配置分组；在配置文件中增加路径
     * @param locator
     * @return
     */
    @Bean
    @Lazy(false)
    public List<GroupedOpenApi> apis(RouteDefinitionLocator locator) {
        List<GroupedOpenApi> groups = new ArrayList<>();
        List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
        assert definitions != null;
        definitions.stream().filter(routeDefinition -> routeDefinition.getId().contains("ruoyi-")).forEach(routeDefinition -> {
            String name = routeDefinition.getUri().getAuthority().replaceAll("ruoyi-","");
            GroupedOpenApi op =  GroupedOpenApi.builder().pathsToMatch("/" + name + "/**").group(name).build();
            groups.add(op);
        });
        return groups;
    }
}
