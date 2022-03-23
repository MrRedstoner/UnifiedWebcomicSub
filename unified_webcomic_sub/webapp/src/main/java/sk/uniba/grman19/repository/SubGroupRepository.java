package sk.uniba.grman19.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import sk.uniba.grman19.models.SubGroup;

@RepositoryRestResource(exported = false)
public interface SubGroupRepository extends CrudRepository<SubGroup, Long> {

}
