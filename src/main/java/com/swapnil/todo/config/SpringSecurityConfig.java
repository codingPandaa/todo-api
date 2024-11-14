package com.swapnil.todo.config;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.swapnil.todo.security.JwtAuthenticationEntryPoint;
import com.swapnil.todo.security.JwtAuthenticationFilter;

import lombok.AllArgsConstructor;

@Configuration
@EnableMethodSecurity
@AllArgsConstructor
public class SpringSecurityConfig {

	private UserDetailsService userDetailsService;

	private JwtAuthenticationEntryPoint authenticationEntryPoint;

	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors();
		http.csrf((csrf) -> csrf.disable()).authorizeHttpRequests((authorize) -> {
			authorize.requestMatchers("/api/auth/**").permitAll();
			authorize.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll(); // preflight req
			authorize.anyRequest().authenticated();
		}).httpBasic(Customizer.withDefaults());

		http.exceptionHandling((exception) -> exception.authenticationEntryPoint(authenticationEntryPoint));

		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

}
