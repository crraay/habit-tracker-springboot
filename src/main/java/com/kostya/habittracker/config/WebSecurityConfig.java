package com.kostya.habittracker.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.kostya.habittracker.filter.JwtRequestFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Value("${cors.allowed-origins}")
	private String allowedOrigins;

	@Value("${cors.allowed-methods}")
	private String allowedMethods;

	@Value("${cors.allowed-headers}")
	private String allowedHeaders;

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		
		// Parse comma-separated values from properties
		List<String> origins = Arrays.asList(allowedOrigins.split(","));
		List<String> methods = Arrays.asList(allowedMethods.split(","));
		List<String> headers = Arrays.asList(allowedHeaders.split(","));
		
		configuration.setAllowedOrigins(origins);
		configuration.setAllowedMethods(methods);
		configuration.setAllowedHeaders(headers);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(request -> request
				.requestMatchers("/api/auth/**").permitAll()
				.requestMatchers("/actuator/health").permitAll()
				// TODO rework
				.requestMatchers("/swagger-ui/*").permitAll()
				.requestMatchers("/v3/api-docs/*").permitAll()
				.requestMatchers("/v3/api-docs").permitAll()
				.anyRequest().authenticated()
			)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.exceptionHandling(exceptionHandling -> exceptionHandling
				.authenticationEntryPoint((request, response, authException) -> {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
				})
				.accessDeniedHandler((request, response, accessDeniedException) -> {
					response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
				})
			);

		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

}
