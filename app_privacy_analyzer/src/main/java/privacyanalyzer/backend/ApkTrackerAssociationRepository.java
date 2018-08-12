package privacyanalyzer.backend;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import privacyanalyzer.backend.data.entity.ApkModel;
import privacyanalyzer.backend.data.entity.ApkTrackerAssociation;
import privacyanalyzer.backend.data.entity.Tracker;

public interface ApkTrackerAssociationRepository extends JpaRepository<ApkTrackerAssociation, Long> {

	
	@Query("select apkTracker.tracker"
			+ " from ApkTrackerAssociation apkTracker"
			+ " where apkTracker.apk= :#{[0]}")
	List<Tracker> findAllTrackersByApkModel(ApkModel apkmodel);
	
	List<ApkTrackerAssociation> findByApk(ApkModel apk);
	
	List<ApkTrackerAssociation> findByApkAndTracker(ApkModel apk,Tracker tracker);
}
