package sk.uniba.grman19;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.invoke.MethodHandles;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import sk.uniba.grman19.dao.UWSUserDAO;
import sk.uniba.grman19.models.entity.UWSUser;
import sk.uniba.grman19.models.rest.UserRegistration;
import sk.uniba.grman19.service.UWSUserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class MailResolvingTest {
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Autowired
	private UWSUserDAO userDao;
	@PersistenceContext
	private EntityManager entityManager;

	@Test
	public void test() {
		assertNotNull(userDao);
		Optional<UWSUser> user = userDao.getUser("name");
		assertEquals(true, user.isPresent());
	}


	private static UserRegistration makeUserRegistration(String name, String pass, String email) {
		UserRegistration user = new UserRegistration();
		user.setUsername(name);
		user.setPassword(pass);
		user.setEmail(email);
		return user;
	}

	@Configuration
	@ComponentScan("sk.uniba.grman19.dao")
	@Import(HibernateTestConfig.class)
	public static class Config {
		@Autowired
		private UWSUserService userService;
		@SuppressWarnings("deprecation")
		@Bean
		public PasswordEncoder passwordEncoder(){
			return org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
		}
		@Bean
		public UWSUserService userService() {
			return new UWSUserService();
		}

		@EventListener
		public void handleContextRefresh(ContextRefreshedEvent event) {
			// fill DB
			userService.registerUser(makeUserRegistration("name", "password1", "mail@address"));
		}
	}
}
