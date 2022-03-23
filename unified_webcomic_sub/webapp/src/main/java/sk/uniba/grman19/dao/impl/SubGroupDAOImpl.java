package sk.uniba.grman19.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import sk.uniba.grman19.dao.SubGroupDAO;
import sk.uniba.grman19.models.SubGroup;
import sk.uniba.grman19.repository.SubGroupRepository;

@Component
@Transactional(readOnly = true)
public class SubGroupDAOImpl implements SubGroupDAO {
	@Autowired
	private SubGroupRepository repository;

	@Override
	@Transactional(readOnly = false)
	public SubGroup createUserGroup() {
		return repository.save(new SubGroup("", "", true));
	}

}
