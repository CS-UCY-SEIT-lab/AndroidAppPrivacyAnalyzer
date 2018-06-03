package privacyanalyzer.backend;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import privacyanalyzer.backend.data.entity.ProtectionLevel;

public interface ProtectionLevelRepository extends JpaRepository<ProtectionLevel, Long>{

	
	ProtectionLevel findByName(String name);
	
	

	
}
