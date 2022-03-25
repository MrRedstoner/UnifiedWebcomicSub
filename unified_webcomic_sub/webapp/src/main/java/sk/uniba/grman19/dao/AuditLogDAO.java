package sk.uniba.grman19.dao;

import sk.uniba.grman19.models.entity.UWSUser;

public interface AuditLogDAO {
	void saveLog(UWSUser user, String message);
}
