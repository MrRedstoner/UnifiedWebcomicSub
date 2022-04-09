package sk.uniba.grman19.processing.mail;

import java.lang.invoke.MethodHandles;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
public class EmailSenderDev implements EmailSender {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Override
	public void sendSimpleMessage(String to, String subject, String text) {
		logger.info("sendSimpleMessage to: {} subject: {} body: {}", to, subject, text);
	}

	@Override
	public void sendHtmlMail(String to, String subject, Document content) {
		logger.info("sendHtmlMail to: {} subject: {} body: {}", to, subject, content.toString());
	}
}
