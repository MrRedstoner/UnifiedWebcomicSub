package sk.uniba.grman19.processing.mail;

import org.jsoup.nodes.Document;

public interface EmailSender {
	void sendSimpleMessage(String to, String subject, String text);

	void sendHtmlMail(String to, String subject, Document content);
}
