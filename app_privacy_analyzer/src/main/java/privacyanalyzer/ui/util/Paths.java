package privacyanalyzer.ui.util;

import java.io.File;

import com.vaadin.server.VaadinService;

public class Paths {

	public static String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
	public static String resourcepath = basepath + "/WEB-INF/classes/";
	public static File theDir = new File(basepath + "/WEB-INF/apks/");
	public static String liteRadarPath=resourcepath+"mypythonscripts/literadar.py";
	public static String permissionCheckerPath=resourcepath+"PermissionChecker.jar";
	public static String pythonScript=resourcepath+"mypythonscripts/myscript.py";
	public static String wekaModelPath=resourcepath+"myModel.model";
	public static String wekaAttributesPath=resourcepath+"Datasets/myAttributes.arff";
	
	

	
}
