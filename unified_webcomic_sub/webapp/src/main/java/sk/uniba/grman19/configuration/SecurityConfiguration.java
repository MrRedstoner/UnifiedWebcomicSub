package sk.uniba.grman19.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import sk.uniba.grman19.models.UWSUser;
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
			.passwordEncoder(UWSUser.PASSWORD_ENCODER);
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
}
