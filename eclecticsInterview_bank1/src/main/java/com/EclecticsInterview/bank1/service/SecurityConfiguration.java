package com.EclecticsInterview.bank1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.EclecticsInterview.bank1.service.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	
	@Autowired
	private MyUserDetailService userDetailsService;
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Autowired
	private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)
	{
		try {
			return httpSecurity
//					.csrf( AbstractHttpConfigurer::disable )
					.csrf( httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable() )
					.authorizeHttpRequests(
						registry -> {
							registry
							.requestMatchers(
									"/",
									"/authenticate",
									"/myCss.css",
									"/user/reg/"
									)
							.permitAll();
							registry
							.requestMatchers(
									"/persons/**",
									"/admin/**"
									)
							.hasRole("ADMIN");
							registry
							.requestMatchers(
//									"/epersons/",
									"/user/**"
								)
							.hasRole("USER");
							registry.anyRequest().authenticated();
						}
					)
//					.formLogin( formLogin-> formLogin.permitAll() )
//					.httpBasic( withDefaults() )
					.formLogin(
							httpSecurityFormLoginConfigurer -> {
								httpSecurityFormLoginConfigurer
								.loginPage("/login")
								.defaultSuccessUrl("/")
//								.successHandler( new MyAuthenticationSuccessHandler() ) // big blunder ! no beans injected! 
								.successHandler( myAuthenticationSuccessHandler )
								.permitAll();
							}
					)
					.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
					.build();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/* // inline
	@Bean
	public UserDetailsService userDetailsService()
	{
		UserDetails normalUser = User.builder()
				.username("user")
				.password("$2a$12$xM2mlDQW6k3MsugiQtZQM.tyS1IuRPl8/RrzffKPSTpRTdHt/5KVO")
				.roles("USER")
				.build();
				
		UserDetails adminUser = User.builder()
				.username("admin")
				.password("$2a$12$xM2mlDQW6k3MsugiQtZQM.tyS1IuRPl8/RrzffKPSTpRTdHt/5KVO")
				.roles("ADMIN","USER")
				.build();
		
		return new InMemoryUserDetailsManager(normalUser, adminUser);
	}
	*/
	
	@Bean
	public UserDetailsService userDetailsService()
	{
		return userDetailsService;
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider()
	{
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}
	
	@Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }
	
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
}
