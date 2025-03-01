package hrms.rnt.login.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//import lombok.extern.slf4j.Slf4j;

/**
 * @author Vishal Dabade
 *
 */
@Configuration
@EnableWebSecurity
@EnableWebMvc
@SuppressWarnings("deprecation")
public class MyConfig implements WebMvcConfigurer {

	@Autowired
	CustomeUserDetailsService customUserDetails;
	@Autowired
	JwtRequestFilter authenticationFilter;
	@Autowired
	JWTAuthenticationEntryPoint authenticationEntryPoint;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors();
		http.csrf(csrf -> {
			try {
				csrf.disable().authorizeHttpRequests()
						// we can give give access to the api based on the role or using
						// e.g.antMatchers("/api/users/{path}").hasRole(null)
						.antMatchers(AuthenticationUtil.PUBLIC_URLS).permitAll().antMatchers(HttpMethod.OPTIONS, "/**")
						.permitAll().requestMatchers(CorsUtils::isPreFlightRequest).permitAll().anyRequest()
						.authenticated();
			} catch (Exception e) {
				// log.error("error occurred in the securityFilterChain... {}", e.getMessage());
			}
		}).exceptionHandling(handling -> handling.authenticationEntryPoint(authenticationEntryPoint))
				.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

		http.authenticationProvider(daoAuthenticationProvider());

		return http.build();
	}


	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	@Bean
	public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {

		return authenticationConfiguration.getAuthenticationManager();
	}

	
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(this.customUserDetails);
		provider.setPasswordEncoder(passwordEncoder());

		return provider;
	}

	/**
	 * This method will configure cross origin access to api's.
	 * 
	 * @since version 1.0
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/" + "**").allowedOrigins("*").allowedMethods("*").allowedHeaders("*").exposedHeaders("*");
	}

}
