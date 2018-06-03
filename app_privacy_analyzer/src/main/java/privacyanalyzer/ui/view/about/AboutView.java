package privacyanalyzer.ui.view.about;

import java.io.File;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.Binder;
import com.vaadin.data.HasValue;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.HeaderRow;

import privacyanalyzer.ui.navigation.NavigationManager;

@SpringView
public class AboutView extends AboutViewDesign implements View {
	
	private final NavigationManager navigationManager;
	
		
	@Autowired
	public AboutView(NavigationManager navigationManager) {
		this.navigationManager = navigationManager;
		
	}
	
	@PostConstruct
    protected void init() {
		String basepath = VaadinService.getCurrent()
                .getBaseDirectory().getAbsolutePath();
		FileResource resource = new FileResource(new File(basepath +
                "/VAADIN/themes/apptheme/AndroidIcon.png"));
	this.image.setSource(resource);
	image.setWidth(100, Unit.PIXELS);
	image.setHeight(100, Unit.PIXELS);
       image.setVisible(true);

    }
	

}