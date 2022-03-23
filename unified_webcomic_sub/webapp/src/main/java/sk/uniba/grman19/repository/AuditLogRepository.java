package sk.uniba.grman19.repository;

import org.springframework.data.repository.CrudRepository;

import sk.uniba.grman19.models.AuditLog;

public interface AuditLogRepository extends CrudRepository<AuditLog, Long> {

}
