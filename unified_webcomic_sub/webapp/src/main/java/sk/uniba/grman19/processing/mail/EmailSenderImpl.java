package sk.uniba.grman19.processing.mail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.jsoup.nodes.Document;
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

	@Value("${mail.from.address:noreply.uniwebsub@gmail.com}")
	private String FROM_ADDRESS;

	@Autowired
	private JavaMailSender emailSender;

	public void sendSimpleMessage(String to, String subject, String text) throws Exception {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(FROM_ADDRESS);
			message.setTo(to);
			message.setSubject(subject);
			message.setText(text);

			emailSender.send(message);
		} catch (MailException e) {
			throw new Exception("Exception when sending mail", e);
		}
	}

	public void sendHtmlMail(String to, String subject, Document content) throws Exception {
		try {
			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(FROM_ADDRESS);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content.toString(), true);

			emailSender.send(message);
		} catch (MailException | MessagingException e) {
			throw new Exception("Exception when sending mail", e);
		}
	}
}
