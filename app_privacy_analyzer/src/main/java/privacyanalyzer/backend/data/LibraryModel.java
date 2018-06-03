package privacyanalyzer.backend.data;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.Entity;



public class LibraryModel implements Serializable{

	private String Library;
	private String Match_Ratio;
	private String Package;
	private ArrayList<String> Permission = new ArrayList<String>();
	private String Popularity;
	private String Standard_Package;
	private String Type;
	private String Website;
	private String Weight;

	/**
	 * 
	 */
	public LibraryModel() {

	}

	

	public String getLibrary() {
		return Library;
	}

	public void setLibrary(String library) {
		Library = library;
	}

	public String getMatch_Ratio() {
		return Match_Ratio;
	}

	public void setMatch_Ratio(String match_Ratio) {
		Match_Ratio = match_Ratio;
	}

	public String getPackage() {
		return Package;
	}

	public void setPackage(String package1) {
		Package = package1;
	}

	public ArrayList<String> getPermission() {
		return Permission;
	}

	public void setPermission(ArrayList<String> permission) {
		Permission = permission;
	}

	public String getPopularity() {
		return Popularity;
	}

	public void setPopularity(String popularity) {
		Popularity = popularity;
	}

	public String getStandard_Package() {
		return Standard_Package;
	}

	public void setStandard_Package(String standard_Package) {
		Standard_Package = standard_Package;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getWebsite() {
		return Website;
	}

	public void setWebsite(String website) {
		Website = website;
	}

	public String getWeight() {
		return Weight;
	}

	public void setWeight(String weight) {
		Weight = weight;
	}

	

	
}
