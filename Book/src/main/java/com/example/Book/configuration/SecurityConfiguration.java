package com.example.Book.configuration;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.Book.service.UserService;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
	
	 private final JwtAuthenticationFilter jwtAuthenticationFilter;
	 private final UserService userService;
 
	 @Bean
	 OpenAPI openAPI() {
	        return new OpenAPI().addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
	            .components(new Components().addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()))
	            .info(new Info().title("My REST API")
	                .description("Some custom description of API.")
	                .license(new License().name("License of API")
	                    .url("API license URL")));
	    }

	private SecurityScheme createAPIKeyScheme() {
	        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
	            .bearerFormat("JWT")
	            .scheme("bearer");
	}
	     
	 private static final String[] AUTH_WHITELIST = {
	    		"/v2/api-docs",
	            "/swagger-resources",
	            "/swagger-resources/**",
	            "/configuration/ui",
	            "/configuration/security",
	            "/swagger-ui.html",
	            "/webjars/**",
	            "/v3/api-docs/**",
	            "/swagger-ui/**"
	    };
	 
	 @Bean
	 SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http.csrf(AbstractHttpConfigurer::disable)
	               .authorizeHttpRequests(request -> 
	               request.requestMatchers(AUTH_WHITELIST).permitAll()
	               .requestMatchers("/error").permitAll()
	               .requestMatchers(HttpMethod.POST, "/api/v1/book").hasAuthority("ADMIN")
	               .requestMatchers(HttpMethod.PUT, "/api/v1/book/**").hasAuthority("ADMIN")
	               .requestMatchers(HttpMethod.DELETE, "/api/v1/book/**").hasAuthority("ADMIN")
	               .requestMatchers("/api/v1/book/**").permitAll().anyRequest()
	               .authenticated()
	               )
	               .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
	               .authenticationProvider(authenticationProvider()).addFilterBefore(
	                        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	        return http.build();
	    }
	 
	 @Bean
	 PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	 }
	 
	 @Bean
	 AuthenticationProvider authenticationProvider() {
	        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	        authProvider.setUserDetailsService(userService.userDetailsService());
	        authProvider.setPasswordEncoder(passwordEncoder());
	        return authProvider;
	 }
	 
	 @Bean
	 AuthenticationManager authenticationManager(AuthenticationConfiguration config)
	            throws Exception {
	        return config.getAuthenticationManager();
	 }
}

