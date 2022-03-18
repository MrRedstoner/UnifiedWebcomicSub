package sk.uniba.grman19.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import sk.uniba.grman19.models.UWSUser;
import sk.uniba.grman19.service.UWSUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired
	private UWSUserDetailsService userDetailsService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
			.passwordEncoder(UWSUser.PASSWORD_ENCODER);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/built/**", "/main.css")
				.permitAll()
			.antMatchers("/index.html", "/api/sources")
				.permitAll()// TODO set up nicely
			.anyRequest()
				.authenticated()
			.and()
			.formLogin()
				.defaultSuccessUrl("/index.html", true)
				.permitAll()
			.and()
			.httpBasic()
			.and()
			.csrf()
				.disable()//TODO supposedly better for testing this way
			.logout()
				.logoutSuccessUrl("/index.html");
	}
}
