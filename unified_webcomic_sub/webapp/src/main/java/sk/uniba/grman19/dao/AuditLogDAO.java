package sk.uniba.grman19.dao;

import sk.uniba.grman19.models.UWSUser;

public interface AuditLogDAO {
	void saveLog(UWSUser user, String message);
}
