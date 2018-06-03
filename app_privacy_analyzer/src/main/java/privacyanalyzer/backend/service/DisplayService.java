package privacyanalyzer.backend.service;

import java.io.Serializable;

import org.springframework.stereotype.Service;

import privacyanalyzer.backend.data.entity.ApkModel;

@Service
public class DisplayService  implements Serializable{

	
	private final ApkService apkService;
	private final PermissionService permissionService;
	private final AnalyzeService analyzeService;
	private final TrackerService trackerService;
	private final PermissionCallsService permissionCallsService;
	
	/**
	 * @param apkService
	 * @param permissionService
	 * @param analyzeService
	 * @param trackerService
	 * @param permissionCallsService
	 */
	public DisplayService(ApkService apkService, PermissionService permissionService, AnalyzeService analyzeService,
			TrackerService trackerService, PermissionCallsService permissionCallsService) {
		super();
		this.apkService = apkService;
		this.permissionService = permissionService;
		this.analyzeService = analyzeService;
		this.trackerService = trackerService;
		this.permissionCallsService = permissionCallsService;
	}
	
	
	public void display(ApkModel apkmodel) {
		
	}
	
}
