package sk.uniba.grman19.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.javatuples.Triplet;

import sk.uniba.grman19.models.entity.SourceAttribute;

public class UpdateUtils {
	/**
	 * Returns triplet of create, update, delete, all keyed by attribute name
	 */
	public static Triplet<Map<String, String>, Map<String, String>, Set<String>> merge(Map<String, SourceAttribute> oldAttributes, Map<String, String> attributes) {
		Set<String> delete = new HashSet<>(oldAttributes.keySet());
		Map<String, String> create = new HashMap<>();
		Map<String, String> update = new HashMap<>();

		for (Entry<String, String> attr : attributes.entrySet()) {
			String name = attr.getKey();
			String value = attr.getValue();

			if (oldAttributes.containsKey(name)) {
				// old name -> do not delete, check if new value
				delete.remove(name);
				if (!oldAttributes.get(name).equals(value)) {
					// value changed -> update
					update.put(name, value);
				}
			} else {
				// new name -> create
				create.put(name, value);
			}
		}

		return Triplet.with(create, update, delete);
	}
}
