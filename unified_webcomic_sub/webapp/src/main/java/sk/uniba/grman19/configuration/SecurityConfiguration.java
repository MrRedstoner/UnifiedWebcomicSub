package sk.uniba.grman19.configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import sk.uniba.grman19.service.UWSUserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired
	private UWSUserService userDetailsService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
			.passwordEncoder(passwordEncoder());
	}

	@Bean(name = "authenticationManagerBean")
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//for testing
		http.authorizeRequests()
			.antMatchers("/h2-console/*").permitAll()
			.and().headers().frameOptions().disable()
			.and().csrf().disable();

		http.authorizeRequests()
			.antMatchers("/registration")
				.anonymous()
			.antMatchers("/built/**", "/main.css")
				.permitAll()
			.antMatchers("/index", "/api/sources", "/rest/user/getlogged")
				.permitAll()// TODO set up nicely
			.anyRequest()
				.authenticated()
			.and()
			.formLogin()
				.defaultSuccessUrl("/index", true)
				.permitAll()
			.and()
			.httpBasic()
			.and()
			.logout()
				.logoutSuccessUrl("/index");
	}

	@Bean
	protected PasswordEncoder passwordEncoder() {
		Map<String, PasswordEncoder> encoders = new HashMap<>();
		encoders.put("bcrypt", new BCryptPasswordEncoder());
		DelegatingPasswordEncoder passworEncoder = new DelegatingPasswordEncoder("bcrypt", encoders);
		return passworEncoder;
	}
}
