package sk.uniba.grman19.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sk.uniba.grman19.dao.SeenUpdateDAO;
import sk.uniba.grman19.models.entity.SourceUpdate;
import sk.uniba.grman19.models.entity.UWSUser;

@Service
public class SeenUpdateService {

	@Autowired
	private SeenUpdateDAO seenUpdateDAO;

	@Transactional(readOnly = false)
	public void updateSeenUpdates(UWSUser user, Collection<SourceUpdate> updates) {
		Map<Long, SourceUpdate> newest = new HashMap<>();
		for (SourceUpdate update : updates) {
			Long source = update.getSource().getId();
			if (newest.containsKey(source)) {
				newest.compute(source, (s, old) -> getNewer(old, update));
			} else {
				newest.put(source, update);
			}
		}

		for (SourceUpdate update : newest.values()) {
			seenUpdateDAO.updateSeenUpdate(user, update);
		}
	}

	private SourceUpdate getNewer(SourceUpdate update0, SourceUpdate update1) {
		if (update1.getUpdateTime().after(update0.getUpdateTime())) {
			return update1;
		} else {
			return update0;
		}
	}
}
