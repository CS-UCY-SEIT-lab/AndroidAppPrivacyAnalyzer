package privacyanalyzer.backend.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import privacyanalyzer.backend.data.entity.AbstractEntity;
import privacyanalyzer.backend.data.entity.MyAbstractEntity;

public abstract class MyCrudService<T extends MyAbstractEntity> {

	protected abstract CrudRepository<T, Long> getRepository();

	public T save(T entity) {
		return getRepository().save(entity);
	}

	public void delete(long id) {
		getRepository().delete(id);
	}

	public T load(long id) {
		return getRepository().findOne(id);
	}

	public abstract long countAnyMatching(Optional<String> filter);

	public abstract Page<T> findAnyMatching(Optional<String> filter, Pageable pageable);

}
