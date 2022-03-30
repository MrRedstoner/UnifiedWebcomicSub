package sk.uniba.grman19.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import sk.uniba.grman19.models.entity.Source;

@RepositoryRestResource(exported = false)
public interface SourceRepository extends CrudRepository<Source, Long>{

}
