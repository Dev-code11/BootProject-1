package com.example.demo.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.example.demo.services.impl.SecurityCustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	 @Autowired
	    private SecurityCustomUserDetailsService userDetailService;
	 
	 @Autowired
	 private OAuthAuthenticationSuccessHandler handler;

	   // @Autowired
	  ///  private AuthenticationSuccessHandler handler;

	    @Autowired
	    private AuthFailtureHandler authFailtureHandler;

	
	
	 @Bean
	    public DaoAuthenticationProvider authenticationProvider() {
	        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
	        // user detail service ka object:
	        daoAuthenticationProvider.setUserDetailsService(userDetailService);
	        // password encoder ka object
	        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

	        return daoAuthenticationProvider;
	    }
	 
	 
	  @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
System.out.println("security working");
	        // configuration

	        // urls configure kiay hai ki koun se public rangenge aur koun se private
	        // rangenge
	        httpSecurity.authorizeHttpRequests(authorize -> {
	            // authorize.requestMatchers("/home", "/register", "/services").permitAll();
	            authorize.requestMatchers("/user/**").authenticated();
	            authorize.anyRequest().permitAll();
	        });
	        
	       // httpSecurity.formLogin(Customizer.withDefaults());
	       // return httpSecurity.build();
	       
	       
	        
	       httpSecurity.formLogin(formLogin -> {
	        	
	        	 formLogin.loginPage("/login");
	            formLogin.loginProcessingUrl("/authenticate");
	           formLogin.successForwardUrl("/user/dashboard");
	            // formLogin.failureForwardUrl("/login?error=true");
	            // formLogin.defaultSuccessUrl("/home");
	             formLogin.usernameParameter("email");
	             formLogin.passwordParameter("password");
	            // httpSecurity.csrf(AbstractHttpConfigurer::disable);
	             formLogin.failureHandler(authFailtureHandler);
	        	 
	        });
	      httpSecurity.csrf(AbstractHttpConfigurer::disable);
	       
	       httpSecurity.logout(logoutForm -> {
	           logoutForm.logoutUrl("/do-logout");
	           logoutForm.logoutSuccessUrl("/login?logout=true");
	       });
	       
	    //  httpSecurity.oauth2Login(Customizer.withDefaults());
	      httpSecurity.oauth2Login(oauth-> {
	          oauth.loginPage("/login"); 
	          oauth.successHandler(handler);// Default login page for OAuth2
	         //              .defaultSuccessUrl("/user/dashboard", true)  // Redirect to /user/dashboard after successful login
	               //        .failureUrl("/login?error=true");  // Redirect to login page with error on failure
	        });
	       
	        return httpSecurity.build();

	    }
	 
	 
	 
	  @Bean
	    public PasswordEncoder passwordEncoder() {
		  System.out.println("password encoder works");
	        return new BCryptPasswordEncoder();
	    }

}
