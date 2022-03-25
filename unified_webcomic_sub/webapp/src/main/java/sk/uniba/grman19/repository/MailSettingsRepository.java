package sk.uniba.grman19.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import sk.uniba.grman19.models.entity.MailSettings;

@RepositoryRestResource(exported = false)
public interface MailSettingsRepository extends CrudRepository<MailSettings, Long> {

}
