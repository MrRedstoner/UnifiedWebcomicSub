package sk.uniba.grman19.processing;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import sk.uniba.grman19.processing.poll.SimplePoller;
import sk.uniba.grman19.service.MailSendingService;

@Configuration
@EnableScheduling
public class ProcessingManager {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Autowired
	private SimplePoller simplePoller;
	@Autowired
	private MailSendingService mailSendingService;

	@Scheduled(cron = "${polling.simple.cron:-}")
	public void runSimplePoll() {
		logger.info("Running simple polling");
		simplePoller.runPolling();
	}

	@Scheduled(cron = "${mail.daily.cron:-}")
	@Transactional(readOnly = true)
	public void sendDailyMail() {
		logger.info("Sending daily mail");
		mailSendingService.sendDailyMail();
	}

	@Scheduled(cron = "${mail.weekly.cron:-}")
	@Transactional(readOnly = true)
	public void sendWeeklyMail() {
		logger.info("Sending weekly mail");
		mailSendingService.sendWeeklyMail();
	}
}
