package in.ranjitkokare.expensetrackerapi.config;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import in.ranjitkokare.expensetrackerapi.security.CustomUserDetailsService;
import in.ranjitkokare.expensetrackerapi.security.JwtRequestFilter;

@Configuration
public class WebSecurityConfig {

	@Autowired
	private CustomUserDetailsService userDetailsService;

//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		
//		http
//			.csrf().disable()
//			.authorizeRequests()
//			.antMatchers("/login", "/register").permitAll()
//			.anyRequest().authenticated()
//			.and()
//			//telling spring security not to maintain session
//			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//		http.httpBasic();// here we omit FormBasic because we not uses front end forms
//	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		/*
		 * http.csrf().disable().authorizeHttpRequests().requestMatchers("/login",
		 * "/register").permitAll().anyRequest() .authenticated().and() // telling
		 * spring security not to maintain session
		 * .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		 * http.addFilterBefore(authenticationJwtTokenFilter(),
		 * UsernamePasswordAuthenticationFilter.class); http.httpBasic();// here we omit
		 * FormBasic because we not uses front end forms return http.build();
		 */
		
		return http.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(auth -> auth.requestMatchers("/login", "/register").permitAll().anyRequest().authenticated())
			//telling spring security not to maintain session
			.sessionManagement(session -> session.sessionCreationPolicy((SessionCreationPolicy.STATELESS)))
			.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
			.httpBasic(Customizer.withDefaults())
			.build(); //builder pattern
	}

	@Bean
	public JwtRequestFilter authenticationJwtTokenFilter() {
		return new JwtRequestFilter();
	}

	// also pass password encoder
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
	public AuthenticationManager authManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}
//	
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {	
//		//now we tell to spring security we configure user
//		auth.userDetailsService(userDetailsService);
//	}
//	
//	
//	@Bean
//	@Override
//	public AuthenticationManager authenticationManagerBean() throws Exception {
//		return super.authenticationManagerBean();
//	}
}
