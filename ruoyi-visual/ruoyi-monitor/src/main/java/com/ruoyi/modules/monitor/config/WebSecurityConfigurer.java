package com.ruoyi.modules.monitor.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

/**
 * 监控权限配置
 * 
 * @author ruoyi
 */
@EnableWebSecurity
public class WebSecurityConfigurer
{
    private final String adminContextPath;

    public WebSecurityConfigurer(AdminServerProperties adminServerProperties)
    {
        this.adminContextPath = adminServerProperties.getContextPath();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception
    {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(adminContextPath + "/");

        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(adminContextPath + "/assets/**"
                                , adminContextPath + "/login"
                                , adminContextPath + "/actuator/**"
                                , adminContextPath + "/instances/**")
                        .permitAll()
                        .anyRequest().authenticated()
                )
                .headers(headers->headers.frameOptions(fo->fo.disable()))
                .formLogin(login->login.loginPage(adminContextPath + "/login").successHandler(successHandler))
                .logout(out->out.logoutUrl(adminContextPath + "/logout"))
                .httpBasic(Customizer.withDefaults())
                .build();
    }
}
