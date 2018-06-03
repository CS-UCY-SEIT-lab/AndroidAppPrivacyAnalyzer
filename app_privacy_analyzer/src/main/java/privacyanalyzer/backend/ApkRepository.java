package privacyanalyzer.backend;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import privacyanalyzer.backend.data.entity.ApkModel;

public interface ApkRepository extends JpaRepository<ApkModel, Long> {

	
	@Query("select apk from ApkInfo apk where apk.sha256 = :#{#uapk.sha256}")
	List<ApkModel> checkIfExists(@Param("uapk") ApkModel uapk);
	
	@Query("select apk from ApkInfo apk where apk.sha256 = :#{[0]}")
	List<ApkModel> checkIfExists(String hash);
	
	ApkModel findBySha256(String sha256);
	
	ApkModel findById(Long id);
	
	List<ApkModel> findAllByOrderByIdDesc();
	
	@Query("SELECT COUNT(*) FROM ApkInfo apk where apk.isMalware=1 OR apk.score>=60")
	long findDangerousApk();
	
	@Query("SELECT apk FROM ApkInfo apk WHERE DATE(apk.createdAt) = CURDATE()")
	List<ApkModel> findTodayAddedAPKs();
	
	
	
}
