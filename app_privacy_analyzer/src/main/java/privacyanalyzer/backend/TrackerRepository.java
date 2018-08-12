package privacyanalyzer.backend;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import privacyanalyzer.backend.data.entity.Tracker;

public interface TrackerRepository extends JpaRepository<Tracker, Long> {

	
	List<Tracker> findAll();
	
	
	Tracker findById(Long id);
}
