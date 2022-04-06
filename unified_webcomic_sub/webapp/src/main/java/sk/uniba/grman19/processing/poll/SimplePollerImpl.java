package sk.uniba.grman19.processing.poll;

import static sk.uniba.grman19.processing.Constants.SIMPLE_POLL_ATTRIBUTE;
import static sk.uniba.grman19.processing.Constants.SIMPLE_POLL_CHECK;
import static sk.uniba.grman19.processing.Constants.SIMPLE_POLL_TAG;
import static sk.uniba.grman19.processing.Constants.SIMPLE_POLL_URL;
import static sk.uniba.grman19.util.FunctionUtils.compose;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import sk.uniba.grman19.models.entity.Source;
import sk.uniba.grman19.models.entity.SourceUpdate;
import sk.uniba.grman19.service.SourceService;
import sk.uniba.grman19.util.ConversionUtils;

@Component
public class SimplePollerImpl implements SimplePoller {
	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	@Autowired
	private SourceService sourceService;
	private final RestTemplate restTemplate;

	@Autowired
	public SimplePollerImpl(RestTemplateBuilder builder) {
		this.restTemplate = builder.build();
	}

	/** Sequential for the time being */
	@Override
	public void runPolling() {
		// TODO timeouts/parallel etc.
		for (SimplePollSource source : resolveSources()) {
			try {
				runSource(source);
			} catch (Exception e) {
				logger.error("Error during source processing", e);
			}
		}
	}

	private void runSource(SimplePollSource source) {
		ResponseEntity<String> res = restTemplate.getForEntity(source.getUrl(), String.class);
		if (!res.getStatusCode().is2xxSuccessful()) {
			logger.warn("Failed polling source {}", source.getSource().getId());
			return;
		}
		Document doc = Jsoup.parse(res.getBody(), source.getUrl());
		Optional<String> elem = doc.select(source.getTagSelect())
			.stream()
			.filter(e -> source.getContentCheck().matcher(e.text()).matches())
			.filter(e -> e.hasAttr(source.getAttrName()))
			.map(e -> e.attr(source.getAttrName()))
			.findFirst();
		if (!elem.isPresent()) {
			logger.warn("Failed to find in response from source {}", source.getSource().getId());
			return;
		}
		String newVal = elem.get();
		Optional<String> lastVal = source.getLastUpdate().map(u -> u.getValue());
		if (!lastVal.isPresent() || !lastVal.get().equals(newVal)) {
			sourceService.onSourceUpdate(source.getSource(), newVal);
		}
	}

	@Transactional(readOnly = true)
	private List<SimplePollSource> resolveSources() {
		List<Source> sources = sourceService.getSimplePollSources();
		Map<Long, SourceUpdate> updates = sourceService.getLastUpdates(sources)
			.stream()
			.collect(Collectors.toMap(compose(SourceUpdate::getSource, Source::getId), u -> u));

		List<SimplePollSource> resolved = new ArrayList<SimplePollSource>();
		for (Source source : sources) {
			Map<String, String> attributes = ConversionUtils.toMap(source.getSourceAttribute());
			boolean valid=true;
			String url = attributes.getOrDefault(SIMPLE_POLL_URL, null);
			if(url==null)valid=false;
			Optional<SourceUpdate> lastUpdate=Optional.ofNullable(updates.getOrDefault(source.getId(), null));
			String tagSelect = attributes.getOrDefault(SIMPLE_POLL_TAG, "a");
			Pattern regex= Pattern.compile(attributes.getOrDefault(SIMPLE_POLL_CHECK, ".*"));
			String attribute = attributes.getOrDefault(SIMPLE_POLL_ATTRIBUTE, "href");
			
			if(!valid){
				logger.warn("Incorrect configuration on source {}", source.getId());
				continue;
			}
			resolved.add(new SimplePollSource(source, url, lastUpdate, tagSelect, regex, attribute));
		}
		return resolved;
	}

	private static class SimplePollSource {
		private final Source source;
		private final String url;
		private final Optional<SourceUpdate> lastUpdate;
		private final String tagSelect;
		private final Pattern contentCheck;
		private final String attrName;

		public SimplePollSource(Source source, String url, Optional<SourceUpdate> lastUpdate, String tagSelect, Pattern contentCheck, String attrName) {
			this.source = source;
			this.url = url;
			this.lastUpdate = lastUpdate;
			this.tagSelect = tagSelect;
			this.contentCheck = contentCheck;
			this.attrName = attrName;
		}

		public Source getSource() {
			return source;
		}

		public String getUrl() {
			return url;
		}

		public Optional<SourceUpdate> getLastUpdate() {
			return lastUpdate;
		}

		public String getTagSelect() {
			return tagSelect;
		}

		public Pattern getContentCheck() {
			return contentCheck;
		}

		public String getAttrName() {
			return attrName;
		}
	}
}
