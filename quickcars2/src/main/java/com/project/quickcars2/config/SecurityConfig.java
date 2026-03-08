package com.project.quickcars2.config;

import com.project.quickcars2.util.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/","/index.html","/vehiclepage.html","/js/**","/images/**",  "/home/**","/vehicle/**" ,"/login", "/oauth2/**", "/logout","/navbar.html","/rental/**","/thankyoupage.html","/userdashboard.html","/admin/auth/**").permitAll()
                        .requestMatchers("/adminlogin.html", "/adminsignup.html", "/admindashboard.html").permitAll()

                        .requestMatchers("/home/api/user/profile","/rentalpage.html","/home/api/auth/check","/userdashboard/*").authenticated()
                        .requestMatchers("/admin/dashboard/**").authenticated()

                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth -> oauth
                        .defaultSuccessUrl("/", true)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );
        return http.build();
    }
}
