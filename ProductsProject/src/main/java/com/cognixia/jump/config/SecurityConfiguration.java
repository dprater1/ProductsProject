package com.cognixia.jump.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cognixia.jump.filter.JwtRequestFilter;

@Configuration
public class SecurityConfiguration {

	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	JwtRequestFilter jwtRequestFilter;
	
	//Authentication
	@Bean
	protected UserDetailsService userDetailsService() {
		
		return userDetailsService;
	}
	
	//Authorization
		@Bean
		protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
			
			http.csrf().disable()
				.authorizeRequests()
				.antMatchers("/user/add").permitAll()
				.antMatchers("/manga/all").authenticated()
				.antMatchers("/manga/*").hasRole("ADMIN")
				.antMatchers("/user/all").hasRole("ADMIN")
				.antMatchers("/user/delete/*").hasRole("ADMIN")
				.antMatchers("/user/*").authenticated()
				.antMatchers("/v3/api-docs").authenticated()// all apis you have to have an user account
				.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // tell Spring Security to NOT create sessions 
			
			//request will go through many filters, but typically the first filter it checks is the one for username and password
			// however, we will set it up, that our JWT filter gets checked first, or else authentication will fail, since spring security
			
			http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
			
			return http.build();
		}
	
		@Bean
		protected PasswordEncoder encoder() {
			
			//encrpyt the password with BC
			return new BCryptPasswordEncoder();
			
		}
		
		// load in the encoder and user details service that are needed for security to do authentication & authorization 
		@Bean
		protected DaoAuthenticationProvider authenticationProvider() {
			DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
			
			authProvider.setUserDetailsService(userDetailsService);
			authProvider.setPasswordEncoder(encoder());
			
			return authProvider;
		}
		
		// can autowire and access the authentication manager (manages authentication login of our project)
		@Bean
		protected AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception{
			return authConfig.getAuthenticationManager();
		}
	
}
