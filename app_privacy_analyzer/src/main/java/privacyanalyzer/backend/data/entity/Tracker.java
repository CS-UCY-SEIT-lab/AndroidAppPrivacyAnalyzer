package privacyanalyzer.backend.data.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name="trackers")
public class Tracker extends MyAbstractEntity{


	
	private String website;
	
	private String name;
	
	private String codeSignature;

	/**
	 * 
	 */
	public Tracker() {
	
	}

	/**
	 * @param trackersPropertiesId
	 * @param trackersPropertiesWebsite
	 * @param trackersPropertiesName
	 * @param trackersPropertiesCodeSignature
	 */
	public Tracker(String trackersPropertiesWebsite, String trackersPropertiesName,
			String trackersPropertiesCodeSignature) {
		super();
		
		this.website = trackersPropertiesWebsite;
		this.name = trackersPropertiesName;
		this.codeSignature = trackersPropertiesCodeSignature;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCodeSignature() {
		return codeSignature;
	}

	public void setCodeSignature(String codeSignature) {
		this.codeSignature = codeSignature;
	}


	
	
	
	
	
	
	
	
}
