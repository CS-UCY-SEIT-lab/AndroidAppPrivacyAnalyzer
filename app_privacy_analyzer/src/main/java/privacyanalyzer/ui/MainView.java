package privacyanalyzer.ui;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.vaadin.spring.access.SecuredViewAccessControl;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.navigator.ViewLeaveAction;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.spring.annotation.UIScope;

import privacyanalyzer.app.security.SecurityUtils;
import privacyanalyzer.backend.data.Role;
import privacyanalyzer.backend.data.entity.User;
import privacyanalyzer.backend.service.UserService;
import privacyanalyzer.ui.navigation.NavigationManager;
import privacyanalyzer.ui.view.about.AboutView;
import privacyanalyzer.ui.view.admin.analyze.AdminAnalyzeView;
import privacyanalyzer.ui.view.admin.user.UserAdminView;
import privacyanalyzer.ui.view.analyze.AnalyzeView;
import privacyanalyzer.ui.view.apklist.ApkListView;
import privacyanalyzer.ui.view.dashboard.DashboardView;
import privacyanalyzer.ui.view.loginRegister.LoginRegisterView;
import privacyanalyzer.ui.view.logout.LogoutView;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;

/**
 * The main view containing the menu and the content area where actual views are
 * shown.
 * <p>
 * Created as a single View class because the logic is so simple that using a
 * pattern like MVP would add much overhead for little gain. If more complexity
 * is added to the class, you should consider splitting out a presenter.
 */
@SpringViewDisplay
@UIScope
public class MainView extends MainViewDesign implements ViewDisplay {

	private final Map<Class<? extends View>, Button> navigationButtons = new HashMap<>();
	private final NavigationManager navigationManager;
	private final SecuredViewAccessControl viewAccessControl;
	private final UserService userService;
	public static MainView thisview;
	@Autowired
	public MainView(NavigationManager navigationManager, SecuredViewAccessControl viewAccessControl,
			UserService userService) {
		this.navigationManager = navigationManager;
		this.viewAccessControl = viewAccessControl;
		this.userService = userService;
	}

	@PostConstruct
	public void init() {
		thisview=this;
		attachNavigation(analyze, AnalyzeView.class);
		attachNavigation(apkList, ApkListView.class);
		attachNavigation(dashboard, DashboardView.class);
		attachNavigation(users, UserAdminView.class);
		attachNavigation(about, AboutView.class);
		attachNavigation(adminAnalyze,AdminAnalyzeView.class);
		attachOnly(logout, LogoutView.class);
		logout.addClickListener(e -> logout());
		attachOnly(loginregister, LoginRegisterView.class);
		loginregister.addClickListener(e -> logout());
		setCurrentUserLabel();
	}

	/**
	 * Makes clicking the given button navigate to the given view if the user has
	 * access to the view.
	 * <p>
	 * If the user does not have access to the view, hides the button.
	 *
	 * @param navigationButton
	 *            the button to use for navigatio
	 * @param targetView
	 *            the view to navigate to when the user clicks the button
	 */
	private void attachNavigation(Button navigationButton, Class<? extends View> targetView) {

		boolean hasAccessToView = viewAccessControl.isAccessGranted(targetView);
		navigationButton.setVisible(hasAccessToView);

		if (hasAccessToView) {
			navigationButtons.put(targetView, navigationButton);
			navigationButton.addClickListener(e -> navigationManager.navigateTo(targetView));
		}
	}

	private void attachOnly(Button navigationButton, Class<? extends View> targetView) {

		boolean hasAccessToView = viewAccessControl.isAccessGranted(targetView);
		navigationButton.setVisible(hasAccessToView);

	}

	@Override
	public void showView(View view) {
		content.removeAllComponents();
		content.addComponent(view.getViewComponent());

		navigationButtons.forEach((viewClass, button) -> button.setStyleName("selected", viewClass == view.getClass()));

		Button menuItem = navigationButtons.get(view.getClass());
		String viewName = "";
		if (menuItem != null) {
			viewName = menuItem.getCaption();
		}
		activeViewName.setValue(viewName);
	}

	/**
	 * Logs the user out after ensuring the currently open view has no unsaved
	 * changes.
	 */
	public void logout() {
		ViewLeaveAction doLogout = () -> {
			UI ui = getUI();
			ui.getSession().getSession().invalidate();
			ui.getPage().reload();
		};
		navigationManager.runAfterLeaveConfirmation(doLogout);

	}
	public static void outnow() {
		thisview.logout();
	}

	public void setCurrentUserLabel() {
		User currentUser = SecurityUtils.getCurrentUser(userService);
		if (currentUser.getRole().equals(Role.GUEST)) {
			currentUserLabel.setValue("Guest User");
		} else
			currentUserLabel.setValue(currentUser.getName());
	}

}
