package privacyanalyzer.backend;

import org.springframework.data.jpa.repository.JpaRepository;

import privacyanalyzer.backend.data.entity.Variables;

public interface VariablesRepository extends JpaRepository<Variables, Long> {

	
	
	Variables findByName(String name);
	
}
