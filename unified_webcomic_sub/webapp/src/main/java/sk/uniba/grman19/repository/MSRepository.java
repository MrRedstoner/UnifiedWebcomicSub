package sk.uniba.grman19.repository;

import org.springframework.data.repository.CrudRepository;

import sk.uniba.grman19.models.MailSettings;

public interface MSRepository extends CrudRepository<MailSettings, Long> {

}
