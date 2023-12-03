package com.example.springrestgeo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import javax.sql.DataSource;


@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager theUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        theUserDetailsManager.setUsersByUsernameQuery("select user_id, password, enabled from users where user_id=?");
        theUserDetailsManager.setAuthoritiesByUsernameQuery("select user_id, role from authorities where user_id=?");
        return theUserDetailsManager;
        //return new JdbcUserDetailsManager(dataSource);
    }

    /*@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }*/


    @Bean
    protected SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
        http
                .httpBasic(Customizer.withDefaults())
                .logout(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .headers(AbstractHttpConfigurer::disable)
                .sessionManagement(ma -> ma.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/error").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/categories").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/categories/*").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/categories").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.POST, "/api/places").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/places/*").permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/places").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/api/places/*").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/places").authenticated()
                                .anyRequest().denyAll());
        return http.build();


    }


    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }


}