package sk.uniba.grman19.processing;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import sk.uniba.grman19.processing.poll.SimplePoller;

@Configuration
@EnableScheduling
public class ProcessingManager {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Autowired
	private SimplePoller simplePoller;

	@Scheduled(cron = "${polling.simple.cron:-}")
	public void runSimplePoll() {
		logger.info("Running simple polling");
		simplePoller.runPolling();
	}
}
