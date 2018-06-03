package privacyanalyzer.backend;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import privacyanalyzer.backend.data.entity.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
	
	List<Permission> findAll();
	
	Permission findByPermissionValue(String permissionValue);

}
