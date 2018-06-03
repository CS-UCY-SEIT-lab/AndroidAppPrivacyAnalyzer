package privacyanalyzer.backend;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import privacyanalyzer.backend.data.entity.ApkModel;
import privacyanalyzer.backend.data.entity.PermissionMethodCallModel;

public interface PermissionCallsRepository extends JpaRepository<PermissionMethodCallModel, Long> {

	
	List<PermissionMethodCallModel> findByApk(ApkModel apk);
	
}
