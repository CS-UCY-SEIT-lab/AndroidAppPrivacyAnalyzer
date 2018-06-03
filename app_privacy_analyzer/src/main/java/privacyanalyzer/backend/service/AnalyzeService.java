package privacyanalyzer.backend.service;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Notification;

import privacyanalyzer.backend.data.ApplicationPermissionsModel;
import privacyanalyzer.backend.data.LibraryModel;
import privacyanalyzer.backend.data.entity.ApkModel;
import privacyanalyzer.backend.data.entity.Permission;
import privacyanalyzer.backend.data.entity.PermissionMethodCallModel;
import privacyanalyzer.backend.data.entity.User;
import privacyanalyzer.functionalities.APKAnalyzer;
import privacyanalyzer.functionalities.MalwarePrediction;
import privacyanalyzer.ui.util.Paths;
import privacyanalyzer.virustotal.VirusTotal;
import privacyanalyzer.virustotal.VirusTotalReportResponse;
import privacyanalyzer.virustotal.VirusTotalUploadResponse;
import weka.classifiers.Classifier;

@Service
public class AnalyzeService extends APKAnalyzer implements Serializable {

	private final ApkService apkService;
	private final PermissionService permissionService;
	private final TrackerService trackerService;
	private final PermissionCallsService permissionCallsService;
	private final VirusTotal virusTotal;
	private final VariablesService variableService;
	
	@Autowired
	public AnalyzeService(VariablesService variableService,ApkService apkService, PermissionService permissionService, TrackerService trackerService,
			PermissionCallsService permissionCallsService) {
		this.apkService = apkService;
		this.permissionService = permissionService;
		this.trackerService = trackerService;
		this.permissionCallsService = permissionCallsService;
		this.variableService=variableService;
		this.virusTotal= new VirusTotal("4a71f3d4c1ba14831e8edce8b32261d2f7f61757761e2e838e84ec911a018e37");
	}

	public boolean predict(ArrayList<String> permissionList) {
		try {
			Classifier cls = (Classifier) weka.core.SerializationHelper.read(Paths.wekaModelPath);
			MalwarePrediction malpred = new MalwarePrediction(cls, permissionList);

			return malpred.predict() == 1;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	
	public void analyzeAndSaveAPK(File file, User current) {
		System.out.println("Analyzing " + file.getAbsolutePath());
		try {
			ApkModel apkmodel = getApkInformation(file.getAbsolutePath());
			if (apkmodel == null) {
				System.out.println("NOT APK");
				return;
			}
			System.out.println("APK Name: *"+apkmodel.getAppName()+"*");
			apkmodel.setAnalyzed(false);
			apkService.save(apkmodel, current);
			
			Thread virusTotalThread = new Thread(() -> {
				
				try {
					System.out.println("Checking if file exists in VirusTotal DB...");
					int code;
					VirusTotalReportResponse report=this.virusTotal.requestReportBySHA256(apkmodel.getSha256());
					code=report.getResponseCode();
					if (code==1) { System.out.println("APK exists in VirusTotal DB"); return;}
					int count=0;
					while (code==204) {
						count++;
						if (count>10) {System.out.println("10 mins slept, still problem.. exiting..");return;}
						System.out.println("VirusTotal API overused... sleeping for 1min...");
						TimeUnit.MINUTES.sleep(1);
						report=this.virusTotal.requestReportBySHA256(apkmodel.getSha256());
						code=report.getResponseCode();
						if (code==1 || code==-2) { System.out.println("APK exists in VirusTotal DB"); return;}
					}
					if (code==0) {
					System.out.println("Uploading APK to VirusTotal...");

					VirusTotalUploadResponse vtur=this.virusTotal.uploadAndScanAPK(file.getAbsolutePath());
					code=vtur.getResponseCode();
					count=0;
					while (code==204) {
						count++;
						if (count>10) {System.out.println("10 mins slept, still problem.. exiting..");return;}
						System.out.println("VirusTotal API overused... sleeping for 1min...");
						TimeUnit.MINUTES.sleep(1);
						vtur=this.virusTotal.uploadAndScanAPK(file.getAbsolutePath());
						code=vtur.getResponseCode();
						if (code==1 || code==-2) { System.out.println("APK uploaded to VirusTotal DB"); return;}
					}
					
					}
				} catch (ParseException | IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			virusTotalThread.start();
			
			
			System.out.println("Getting permissions");
			ApplicationPermissionsModel apm = getAPKPermissions(file.getAbsolutePath());
			if (apm == null) {
				System.out.println("ERROR FOUND");
				apkService.delete(apkmodel.getId());
				return;
			}
			permissionService.saveApkPermissions(apm.getDeclared(), apkmodel, "Declared");
			permissionService.saveApkPermissions(apm.getNotRequiredButUsed(), apkmodel, "NotRequiredButUsed");
			permissionService.saveApkPermissions(apm.getRequiredAndUsed(), apkmodel, "RequiredAndUsed");
			permissionService.saveApkPermissions(apm.getRequiredButNotUsed(), apkmodel, "RequiredButNotUsed");

			System.out.println("checking if malware");
			Set<String> hsmalware = new HashSet<>();
			hsmalware.addAll(apm.getDeclared());
			hsmalware.addAll(apm.getNotRequiredButUsed());
			ArrayList<String> malwarePermissionToCheck = new ArrayList<String>();
			malwarePermissionToCheck.addAll(hsmalware);
			apkmodel.setMalware(predict(malwarePermissionToCheck));
			apkService.save(apkmodel, current);

			System.out.println("Getting libraries");
			LibraryModel[] libModels = getLibrariesPermissions(file.getAbsolutePath());
			
			ArrayList<String> libsPermissions = new ArrayList<String>();
			if (libModels!=null) {
			for (int i = 0; i < libModels.length; i++) {
				libsPermissions.addAll(libModels[i].getPermission());
			}
			trackerService.saveTrackers(libModels, apkmodel);
			}
			Set<String> hs = new HashSet<>();
			hs.addAll(libsPermissions);
			libsPermissions.clear();
			libsPermissions.addAll(hs);
			permissionService.saveApkPermissions(libsPermissions, apkmodel, "LibraryPermission");
			

			ArrayList<String> usedpermissionsList = new ArrayList<String>();
			usedpermissionsList.addAll(apm.getRequiredAndUsed());
			usedpermissionsList.addAll(apm.getNotRequiredButUsed());
			System.out.println("Getting permission calls");

			ArrayList<PermissionMethodCallModel> calllist = getCalls(file.getAbsolutePath(), usedpermissionsList);
			if (calllist!=null) {
			permissionCallsService.saveAll(calllist, apkmodel);}

			// save calls
			apkService.save(apkmodel, current);
			apkmodel.setScore(calculateScore(apkmodel));
			apkmodel.setAnalyzed(true);
			apkService.save(apkmodel, current);
			virusTotalThread.join();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Analysis completed");

		
		
		return;
	
	}
	
	@Async
	public void asyncAnalyzeAndSaveAPK(File file, User current) {
		analyzeAndSaveAPK(file,current);
		file.delete();
		return;
	}

	public float calculateScore(ApkModel apkmodel) {
		float score = 0;
		// List<Permission>
		// declared=permissionService.getApkPermissionAssociationRepository().findAllPermissionsByApkModelAndPermissionType(apkmodel,
		// "Declared");
		List<Permission> notDeclaredButUsed = permissionService.getApkPermissionAssociationRepository()
				.findAllPermissionsByApkModelAndPermissionType(apkmodel, "NotRequiredButUsed");
		List<Permission> declaredAndUsed = permissionService.getApkPermissionAssociationRepository()
				.findAllPermissionsByApkModelAndPermissionType(apkmodel, "RequiredAndUsed");
		List<Permission> declaredAndNotUsed = permissionService.getApkPermissionAssociationRepository()
				.findAllPermissionsByApkModelAndPermissionType(apkmodel, "RequiredButNotUsed");
		List<Permission> libraryPermission = permissionService.getApkPermissionAssociationRepository()
				.findAllPermissionsByApkModelAndPermissionType(apkmodel, "LibraryPermission");

		for (Permission p : declaredAndUsed) {

			String protectionlvl = p.getProtectionlvlName();
			if (protectionlvl.equals("dangerous")) {
				score += variableService.getVariables().getDeclaredAndUsedDangerousPermissionScore();
			} else if (protectionlvl.equals("signature") || protectionlvl.equals("signature|system")) {
				score += variableService.getVariables().getDeclaredAndUsedSignatureSystemPermissionScore();
			}
		}

		for (Permission p : declaredAndNotUsed) {
			String protectionlvl = p.getProtectionlvlName();
			if (protectionlvl.equals("dangerous")) {
				score += variableService.getVariables().getDeclaredAndNotUsedDangerousPermissionScore();
			} else if (protectionlvl.equals("signature") || protectionlvl.equals("signature|system")) {
				score += variableService.getVariables().getDeclaredAndNotUsedSignatureSystemPermissionScore();
			}
		}

		for (Permission p : notDeclaredButUsed) {
			String protectionlvl = p.getProtectionlvlName();
			if (protectionlvl.equals("dangerous")) {
				score += variableService.getVariables().getNotDeclaredButUsedDangerousPermissionScore();
			} else if (protectionlvl.equals("signature") || protectionlvl.equals("signature|system")) {
				score += variableService.getVariables().getNotDeclaredButUsedSignatureSystemPermissionScore();
			} else {
				score += variableService.getVariables().getNotDeclaredButUsedNormalPermissionScore();
			}
		}

		for (Permission p : libraryPermission) {
			String protectionlvl = p.getProtectionlvlName();
			if (protectionlvl.equals("dangerous")) {
				score += variableService.getVariables().getLibraryDangerousPermissionScore();
			} else if (protectionlvl.equals("signature") || protectionlvl.equals("signature|system")) {
				score += variableService.getVariables().getLibrarySignatureSystemPermissionScore();
			}
		}

		if (apkmodel.getIsAdbBackupEnabled().equals("true"))
			score += variableService.getVariables().getAdbScore();

		if (apkmodel.getIsDebuggable().equals("true"))
			score += variableService.getVariables().getDebuggableScore();

		if (apkmodel.isMalware())
			score += variableService.getVariables().getMalwareScore();

		if (score >= variableService.getVariables().getMaximumRiskScore())
			score = (float) variableService.getVariables().getMaximumRiskScore();
		return score;
	}

	public VirusTotalReportResponse getVirusTotalReport(String apkHash) throws ClientProtocolException, IOException {
		
		return this.virusTotal.requestReportBySHA256(apkHash);
	}
	
}
