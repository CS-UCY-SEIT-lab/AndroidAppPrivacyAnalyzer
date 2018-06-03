package privacyanalyzer.backend.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity(name="protectionlevels")
public class ProtectionLevel extends MyAbstractEntity{

	
	@Column(unique = true)
	private String name;
	
	@Column(columnDefinition = "TEXT")
	private String description;
	
	
	public ProtectionLevel() {
		
		
	}


	/**
	 * @param name
	 * @param description
	 */
	public ProtectionLevel(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
}
