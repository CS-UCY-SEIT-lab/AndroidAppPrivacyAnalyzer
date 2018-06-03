package privacyanalyzer.ui.components;

import java.io.File;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

import org.springframework.web.util.HtmlUtils;
import org.vaadin.spring.annotation.PrototypeScope;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.renderers.ImageRenderer;

import privacyanalyzer.backend.data.entity.ApkModel;

@SpringComponent
@PrototypeScope
public class ApksGrid extends Grid<ApkModel> {


	public ApksGrid() {
		addStyleName("orders-grid");
		setSizeFull();
		removeHeaderRow(0);
		setStyleGenerator(apkmodel->"today");
		setSelectionMode(SelectionMode.SINGLE);
		
		Column <ApkModel,String> dateAdded= addColumn(apkmodel -> 
		twoRowCell(apkmodel.getDateString(),apkmodel.getTimeString()),
				new HtmlRenderer());
		dateAdded.setStyleGenerator(apkmodel->"due");
		dateAdded.setWidth(100);
		ImageRenderer<ApkModel> renderer= new ImageRenderer<ApkModel>();
		
		
		Column <ApkModel,ExternalResource> imageColumn =addColumn(
			    apkmodel ->  getStatusImg(apkmodel),
			    renderer).setId("imageColumn");
		imageColumn.setStyleGenerator(e->"imageColumn");
		imageColumn.setWidth(120);
		
		Column <ApkModel,String> apkSummary= addColumn(
				apkmodel -> fourRowCell(apkmodel.getAppName(),
						"Version: "+apkmodel.getPackageVersionName(),
						"Package name: "+apkmodel.getPackageName(),
						"SHA-256: "+apkmodel.getSha256()),
				new HtmlRenderer());
		apkSummary.setStyleGenerator(apkmodel->"summary");
		

		
	}



	private ExternalResource getStatusImg(ApkModel apkmodel) {

		ExternalResource resource;
		
		/*resource = new ExternalResource((
                "VAADIN/images/malware.png"));*/
		
		if (!apkmodel.isAnalyzed()) {
			resource = new ExternalResource(( "VAADIN/images/analyzing.png"));
			
		} else {
			if (apkmodel.isMalware()) {
				resource = new ExternalResource("VAADIN/images/malware.png");
				
			} else {
				resource = new ExternalResource("VAADIN/images/clean.png");
			}
		}
		return resource;
	}

	
	private static String twoRowCell(String header, String content) {
		return "<div class=\"header\">" + HtmlUtils.htmlEscape(header) + "</div><div class=\"content\">"
				+ HtmlUtils.htmlEscape(content) + "</div>";
	}	
	private static String threeRowCell(String header, String content, String content2) {
		return "<div class=\"header\"><b>" + HtmlUtils.htmlEscape(header) + "</b></div><div class=\"content\">"
				+ HtmlUtils.htmlEscape(content) + "</div>"+ "</div><div class=\"content\">"
						+ HtmlUtils.htmlEscape(content2) + "</div>";
	}
	
	private static String fourRowCell(String header, String content, String content2,String content3) {
		return "<div class=\"header\"><b>" + HtmlUtils.htmlEscape(header) + "</b></div>"+ 
			   "<div class=\"content\">"+ HtmlUtils.htmlEscape(content) + "</div>"+ 
			   "<div class=\"content\">"+ HtmlUtils.htmlEscape(content2) + "</div>"+
			   "<div class=\"content\">"+ HtmlUtils.htmlEscape(content3) + "</div>";
	}
	

}
