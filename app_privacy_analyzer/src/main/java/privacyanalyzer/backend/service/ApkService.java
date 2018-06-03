package privacyanalyzer.backend.service;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import privacyanalyzer.app.security.SecurityUtils;
import privacyanalyzer.backend.ApkRepository;
import privacyanalyzer.backend.data.Role;
import privacyanalyzer.backend.data.entity.ApkModel;
import privacyanalyzer.backend.data.entity.User;

@Service
public class ApkService extends MyCrudService<ApkModel> implements Serializable{

	private final ApkRepository apkRepository;
	private final UserService userService;
	

	
	@Autowired
	public ApkService(ApkRepository apkRepository,UserService userService) {
		super();
		this.apkRepository = apkRepository;
		this.userService= userService;
	}
	


	
//@Override
	@Transactional
	public ApkModel save(ApkModel apk,	User current) {
		//User current=SecurityUtils.getCurrentUser(userService);
		if (!current.getRole().equals(Role.GUEST)) {
		apk.setUser(current);
		}
		if (apk.getAppName()==null || apk.getAppName().equals(""))
			apk.setAppName(apk.getPackageName());
		return getRepository().save(apk);
	}
	

	@Override
	public ApkRepository getRepository() {
		// TODO Auto-generated method stub
		return this.apkRepository;
	}

	@Override
	public long countAnyMatching(Optional<String> filter) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Page<ApkModel> findAnyMatching(Optional<String> filter, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

}
