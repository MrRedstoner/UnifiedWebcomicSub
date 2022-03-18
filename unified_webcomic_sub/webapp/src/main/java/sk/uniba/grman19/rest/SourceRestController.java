package sk.uniba.grman19.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import sk.nociar.jpacloner.JpaCloner;
import sk.uniba.grman19.dao.SourceDAO;
import sk.uniba.grman19.models.Source;

@RestController
@RequestMapping("/rest/source")
public class SourceRestController {
	@Autowired
	private SourceDAO sourceDao;

	//first simple attempt, TODO connect to this
	@RequestMapping(method = RequestMethod.GET, path = "/readAll", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Source> readAll() {
		List<Source> sources = sourceDao.getAllSources();

		return JpaCloner.clone(sources);
	}
}
