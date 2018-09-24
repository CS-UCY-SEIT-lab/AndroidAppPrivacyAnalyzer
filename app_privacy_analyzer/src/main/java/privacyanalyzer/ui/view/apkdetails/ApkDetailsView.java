package privacyanalyzer.ui.view.apkdetails;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

import privacyanalyzer.app.security.SecurityUtils;
import privacyanalyzer.backend.ApkRepository;
import privacyanalyzer.backend.PermissionRepository;
import privacyanalyzer.backend.data.Role;
import privacyanalyzer.backend.data.entity.ApkModel;
import privacyanalyzer.backend.data.entity.User;
import privacyanalyzer.backend.service.AnalyzeService;
import privacyanalyzer.backend.service.ApkService;
import privacyanalyzer.backend.service.PermissionCallsService;
import privacyanalyzer.backend.service.PermissionService;
import privacyanalyzer.backend.service.TrackerService;
import privacyanalyzer.backend.service.VariablesService;
import privacyanalyzer.ui.navigation.NavigationManager;
import privacyanalyzer.ui.view.analyze.AnalyzeView;
import privacyanalyzer.ui.view.apklist.ApkListView;
import privacyanalyzer.virustotal.VirusTotalReportResponse;

@SpringView(name = "apk")
public class ApkDetailsView extends ApkDetailsViewDesign implements View {

	private final NavigationManager navigationManager;
	private final ApkService apkService;
	private final PermissionService permissionService;
	private final AnalyzeService analyzeService;
	private final TrackerService trackerService;
	private final PermissionCallsService permissionCallsService;
	private final VariablesService variableService;

	@Autowired
	public ApkDetailsView(VariablesService variableService, NavigationManager navigationManager, ApkService apkService,
			PermissionService permissionService, AnalyzeService analyzeService, TrackerService trackerService,
			PermissionCallsService permissionCallsService) {
		this.navigationManager = navigationManager;
		this.apkService = apkService;
		this.variableService = variableService;
		this.permissionService = permissionService;
		this.analyzeService = analyzeService;
		this.trackerService = trackerService;
		this.permissionCallsService = permissionCallsService;
	}

	@PostConstruct
	public void init() {
		this.backButton.addClickListener(e -> goBack());
		this.analyzeApk.addClickListener(e -> goToAnalyzeView());
		this.analyzeAgainButton.addClickListener(e -> analyzeAgain());
		this.scoreBar.setVisible(false);
	}

	private void goBack() {
		// TODO Auto-generated method stub
		navigationManager.navigateTo(ApkListView.class);
	}

	private void goToAnalyzeView() {
		this.navigationManager.navigateTo(AnalyzeView.class);
	}

	public Long apkid;

	private void analyzeAgain() {
		
		ApkModel apkmodel = this.apkService.getRepository().findById(apkid);
		if (apkmodel == null) {
			showNotFound();
			return;
		}
		
		apkmodel.setScore(analyzeService.calculateScore(apkmodel));
		if (apkmodel.getUser()==null) {
		User guestuser= new User("guest", "guest", "guest", Role.GUEST);
		
		apkService.save(apkmodel,guestuser );
		}else 
		{
			apkService.save(apkmodel,apkmodel.getUser() );
		}
		Page.getCurrent().reload();
	}

	@Override
	public void enter(ViewChangeEvent event) {
		String apkId = event.getParameters();
		if ("".equals(apkId)) {
			enterView(null);
		} else {
			try {
				Long id = Long.parseLong(apkId);
				apkid = id;
				if (Long.toString(id).equalsIgnoreCase(apkId)) {
					enterView(id);
				} else
					enterView(null);

			} catch (NumberFormatException e) {
				enterView(null);
			}
		}
	}

	private void enterView(Long id) {
		ApkModel apkmodel;
		if (id == null) {
			showNotFound();
			return;
		}
		apkmodel = this.apkService.getRepository().findById(id);
		if (apkmodel == null) {
			showNotFound();
			return;
		}
		refreshView(apkmodel);

	}

	private void refreshView(ApkModel apkmodel) {
		this.appNameLabel.setValue(apkmodel.getAppName());
		this.dateLabel.setValue(apkmodel.getDateString() + " " + apkmodel.getTimeString());
		User u = apkmodel.getUser();
		if (u == null)
			this.userLabel.setValue("Guest User");
		else
			this.userLabel.setValue(u.getName());
		this.packageName.setValue("Package Name: " + apkmodel.getPackageName());
		this.packageVersionCode.setValue("Package Version Code: " + apkmodel.getPackageVersionCode());
		this.packageVersionName.setValue("Package Version Name: " + apkmodel.getPackageVersionName());
		this.minSDK.setValue("Min SDK: " + apkmodel.getMinSDK());
		this.targetSDK.setValue("Target SDK: " + apkmodel.getTargetSDK());
		this.sha256.setValue(apkmodel.getSha256());
		if (apkmodel.getIsAdbBackupEnabled().equalsIgnoreCase("true")) {
			this.adbCheckBox.setValue(true);
		} else {
			this.adbCheckBox.setValue(false);
		}
		if (apkmodel.getIsDebuggable().equalsIgnoreCase("true")) {
			this.debugCheckBox.setValue(true);
		} else {
			this.debugCheckBox.setValue(false);
		}

		setStatus(apkmodel);
		setVirusTotalStatus(apkmodel);
		permissionService.setGridbyPermissions(permissionService.getApkPermissionAssociationRepository()
				.findAllPermissionsByApkModelAndPermissionType(apkmodel, "Declared"), this.declaredPermissionsGrid);

		permissionService
				.setGridbyPermissions(
						permissionService.getApkPermissionAssociationRepository()
								.findAllPermissionsByApkModelAndPermissionType(apkmodel, "NotRequiredButUsed"),
						this.notDeclaredButUsedPermissionsGrid);

		permissionService
				.setGridbyPermissions(
						permissionService.getApkPermissionAssociationRepository()
								.findAllPermissionsByApkModelAndPermissionType(apkmodel, "RequiredAndUsed"),
						this.declaredAndUsedPermissionsGrid1);

		permissionService
				.setGridbyPermissions(
						permissionService.getApkPermissionAssociationRepository()
								.findAllPermissionsByApkModelAndPermissionType(apkmodel, "RequiredButNotUsed"),
						this.declaredAndNotUsedPermissionsGrid11);

		permissionService.setGridbyPermissions(permissionService.getApkPermissionAssociationRepository()
				.findAllPermissionsByApkModelAndPermissionType(apkmodel, "LibraryPermission"), this.libraryPermissions);

		permissionCallsService.setGrid(apkmodel, this.callsGrid);

		trackerService.setGrid(apkmodel, this.trackersGrid);

	}

	private void showNotFound() {
		removeAllComponents();
		HorizontalLayout hl = new HorizontalLayout();
		Label notFound = new Label("Apk not found");
		hl.setWidth("100%");

		hl.setDefaultComponentAlignment(Alignment.TOP_CENTER);
		hl.addComponent(notFound);
		addComponent(hl);
	}

	private void setStatus(ApkModel apkmodel) {
		boolean mal = apkmodel.isMalware();
		String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
		FileResource resource;
		if (!apkmodel.isAnalyzed()) {
			resource = new FileResource(new File(basepath + "/VAADIN/images/analyzing.png"));
			statusLabel.setValue("Status: Analyzing APK...");
		} else {

			float score = apkmodel.getScore();
			this.scoreBar.setCaptionAsHtml(true);
			if (score == variableService.getVariables().getMaximumRiskScore()) {
				this.scoreBar
						.setCaption("<center>APK risk score: " + variableService.getVariables().getMaximumRiskScore()
								+ "/" + variableService.getVariables().getMaximumRiskScore() + "</center>");
			} else {
				this.scoreBar.setCaption("<center>APK risk score: " + score + "/"
						+ variableService.getVariables().getMaximumRiskScore() + "</center>");
			}

			if (score < variableService.getVariables().getGreenRisk()) {
				this.scoreBar.setPrimaryStyleName("greenpbar");
			} else if (score < variableService.getVariables().getYellowRisk()) {
				this.scoreBar.setPrimaryStyleName("yellowpbar");
			} else if (score < variableService.getVariables().getOrangRisk()) {
				this.scoreBar.setPrimaryStyleName("orangepbar");
			} else if (score <= variableService.getVariables().getMaximumRiskScore()) {
				this.scoreBar.setPrimaryStyleName("redpbar");
			}

			this.scoreBar.setValue((float) (score / variableService.getVariables().getMaximumRiskScore()));
			this.scoreBar.setVisible(true);

			if (mal) {
				resource = new FileResource(new File(basepath + "/VAADIN/images/malware.png"));
				statusLabel.setValue("Status: APK might be Dangerous!");
				statusLabel.setDescription("This result is based on permissions. "
						+ "This APK looks suspicious because it uses some permissions dangerous/signature. "
						+ "This does not mean that the APK is harmful but it indicates that the APK might be "
						+ "able to get sensitive data from the user device");

			} else {
				resource = new FileResource(new File(basepath + "/VAADIN/images/clean.png"));
				statusLabel.setValue("Status: No dangerous activity found!");
				statusLabel.setDescription("This result is based on permissions. " + "This APK looks non-harmful. "
						+ "This does not mean that the APK is not a malware but it indicates "
						+ "that the permissions that are used (or no permission is used) are not "
						+ "dangerous enough to get sensitive data from the user device.");
			}
		}

		image.setDescription("This result is based on permissions.");
		this.image.setSource(resource);
		image.setWidth(100, Unit.PIXELS);
		image.setHeight(100, Unit.PIXELS);
		image.setVisible(true);
	}

	private void setVirusTotalStatus(ApkModel apkmodel) {
		String link = "https://www.virustotal.com/#/file/" + apkmodel.getSha256();
		try {
			VirusTotalReportResponse report = analyzeService.getVirusTotalReport(apkmodel.getSha256());
			this.virusTotalNumberLabel.setDescription("by VirusTotal.com");
			this.virusTotalLabel.setDescription("by VirusTotal.com");
			if (report == null) {
				this.virusTotalNumberLabel.setVisible(false);
				this.virusTotalLabel.setValue("VirusTotal is scanning the apk...");
				return;
			}
			if (report.getResponseCode() == 204 || report.getResponseCode() >= 400) {
				this.virusTotalNumberLabel.setVisible(false);
				// this.virusTotalLabel.setValue();

				this.virusTotalLabel.setValue(
						"Unable to get Information from VirusTotal, please wait 1 minute and refresh (or <a href='"
								+ link + "' rel='noopener noreferrer' target='_blank'>click here</a>)");
				return;
			}
			if (report.getResponseCode() == 0) {
				this.virusTotalNumberLabel.setVisible(false);
				// this.virusTotalLabel.setValue();

				this.virusTotalLabel.setValue("APK does not exist in VirusTotal's database.");
				return;
			}

			if (report.getResponseCode() == -2) {
				this.virusTotalNumberLabel.setVisible(false);
				// this.virusTotalLabel.setValue();

				this.virusTotalLabel
						.setValue("VirusTotal is analyzing the APK, please wait a few minutes for the results");
				return;
			}
			if (report.getResponseCode() == 1) {
				this.virusTotalNumberLabel.setVisible(true);
				// System.out.println(report);
				this.virusTotalNumberLabel.setValue(report.getPositives() + "/" + report.getTotal());
				if (report.getPositives() == 0) {
					this.virusTotalLabel
							.setValue("No engines detected this file as Malware (for more information <a href='" + link
									+ "' rel='noopener noreferrer' target='_blank'>click here</a>)");
					this.virusTotalNumberLabel.setPrimaryStyleName("vitustotallabelgreen");

				} else if (report.getPositives() == 1) {
					this.virusTotalLabel
							.setValue("One engine detected this file as Malware (for more information <a href='" + link
									+ "' rel='noopener noreferrer' target='_blank'>click here</a>)");
					this.virusTotalNumberLabel.setPrimaryStyleName("vitustotallabelred");
				} else if (report.getPositives() > 1) {
					this.virusTotalLabel.setValue(report.getPositives()
							+ " engines detected this file as Malware (for more information <a href='" + link
							+ "' rel='noopener noreferrer' target='_blank'>click here</a>)");
					this.virusTotalNumberLabel.setPrimaryStyleName("vitustotallabelred");

				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
