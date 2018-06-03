package privacyanalyzer.ui.view.admin.analyze;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;

import com.vaadin.annotations.Push;
import com.vaadin.navigator.View;
import com.vaadin.server.Page;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Notification;

import privacyanalyzer.app.security.SecurityUtils;
import privacyanalyzer.backend.data.Role;
import privacyanalyzer.backend.data.entity.ApkModel;
import privacyanalyzer.backend.data.entity.User;
import privacyanalyzer.backend.service.AnalyzeService;
import privacyanalyzer.backend.service.ApkService;
import privacyanalyzer.backend.service.UserService;
import privacyanalyzer.ui.navigation.NavigationManager;
import privacyanalyzer.ui.util.Hash;

@Push
@SpringView
@Secured(Role.ADMIN)
public class AdminAnalyzeView extends AdminAnalyzeViewDesign implements View {

	private final NavigationManager navigationManager;
	private final ApkService apkService;
	private final AnalyzeService analyzeService;
	private final UserService userService;

	/**
	 * @param navigationManager
	 * @param apkService
	 * @param analyzeService
	 * @param userService
	 */
	@Autowired
	public AdminAnalyzeView(NavigationManager navigationManager, ApkService apkService, AnalyzeService analyzeService,
			UserService userService) {
		this.navigationManager = navigationManager;
		this.apkService = apkService;
		this.analyzeService = analyzeService;
		this.userService = userService;
	}

	@PostConstruct
	public void init() {
		this.analyzebutton.setVisible(false);
		this.findbutton.addClickListener(e -> findbuttonClick());
		this.analyzebutton.addClickListener(e -> analyzebuttonClick());

	}

	private void analyzebuttonClick() {
		System.out.println();
		new Notification("Analyzing APKs..", Notification.Type.ASSISTIVE_NOTIFICATION).show(Page.getCurrent());
		for (File f : listSelect.getSelectedItems()) {
			String sha256 = Hash.SHA256.getHash(f);
			ApkModel apkmodel = apkService.getRepository().findBySha256(sha256);
			if (apkmodel == null) {
				System.out.println("APK not in DB, adding...");
				User current = SecurityUtils.getCurrentUser(userService);
				analyzeService.analyzeAndSaveAPK(f, current);

			}
		}
		return;
	}

	private void findbuttonClick() {
		String path = this.field.getValue();
		File rootDir = new File(path);

		final String[] SUFFIX = { "apk" }; // use the suffix to filter
		Collection<File> files = FileUtils.listFiles(rootDir, SUFFIX, true);
		String s = "";
		List<String> fileName = new ArrayList<String>();
		for (File f : files) {
			fileName.add(f.getAbsolutePath());
			s = s + f.getAbsolutePath() + "\n";
		}
		this.listSelect.setItems(files);
		this.listSelect.setVisible(true);
		this.analyzebutton.setVisible(true);
		this.textarea.setValue(s);
		return;
	}

}
