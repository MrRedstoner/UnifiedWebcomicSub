package sk.uniba.grman19.processing.mail;

import java.lang.invoke.MethodHandles;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Profile("!dev")
@Component
public class EmailSenderImpl implements EmailSender {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Value("${mail.from.address:noreply.uniwebsub@gmail.com}")
	private String FROM_ADDRESS;

	@Autowired
	private JavaMailSender emailSender;

	public void sendSimpleMessage(String to, String subject, String text) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(FROM_ADDRESS);
			message.setTo(to);
			message.setSubject(subject);
			message.setText(text);

			emailSender.send(message);
		} catch (MailException e) {
			logger.error("Exception when sending mail", e);
		}
	}

	public void sendHtmlMail(String to, String subject, Document content) {
		try {
			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(FROM_ADDRESS);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content.toString(), true);

			emailSender.send(message);
		} catch (MailException | MessagingException e) {
			logger.error("Exception when sending mail", e);
		}
	}
}
