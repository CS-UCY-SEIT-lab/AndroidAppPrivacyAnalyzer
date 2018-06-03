package privacyanalyzer.backend.data.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity(name="permissions")
public class Permission extends MyAbstractEntity{


	 
	 
	 private String permissionName;
	 
	 @Column(columnDefinition = "TEXT")
	 private String  permissionDesc;
	 
	 @JsonInclude()
	 @Transient
	 private String  protectionLevel;
	 
	 @ManyToOne
	 private ProtectionLevel protectionlvl;
	 
	 @Column(unique = true)
	 private String  permissionValue;
	 
	public Permission( ) {
}
	 
	 /*@ManyToMany
	 @JoinTable(name = "apk_permission",joinColumns = @JoinColumn(name="permission_id"),inverseJoinColumns = @JoinColumn(name="apk_id"))
	 private List<ApkModel> apks;*/
	 
	/**
	 * @param id
	 * @param permissionName
	 * @param permissionDesc
	 * @param protectionLevel
	 * @param permissionValue
	 */
	public Permission( String permissionName, String permissionDesc, String protectionLevel,
			String permissionValue) {
		super();
		
		this.permissionName = permissionName;
		this.permissionDesc = permissionDesc;
		this.protectionLevel = protectionLevel;
		this.permissionValue = permissionValue;
		
	}
	
	public Permission(String permissionValue) {
		
		this.permissionName =  permissionValue.split("\\.")[permissionValue.split("\\.").length-1];
		this.permissionDesc =  "Unknown permission";
		this.protectionLevel = "No information available";
		this.permissionValue = permissionValue;
		this.protectionlvl=null;
		
	}

	public String getPermissionName() {
		return permissionName;
	}
	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}
	public String getPermissionDesc() {
		return permissionDesc;
	}
	public void setPermissionDesc(String permissionDesc) {
		this.permissionDesc = permissionDesc;
	}
	 @JsonInclude()
	 @Transient
	public String getProtectionLevel() {
		return protectionLevel;
	}

	public void setProtectionLevel(String protectionLevel) {
		this.protectionLevel = protectionLevel;
	}
	public String getPermissionValue() {
		return permissionValue;
	}
	public void setPermissionValue(String permissionValue) {
		this.permissionValue = permissionValue;
	}

	public ProtectionLevel getProtectionlvl() {
		return protectionlvl;
	}

	public void setProtectionlvl(ProtectionLevel protectionlvl) {
		this.protectionlvl = protectionlvl;
	}

	public String getProtectionlvlDesc() {
		if (protectionlvl==null) return "No information available";
		return protectionlvl.getDescription();
	}
	
	public String getProtectionlvlName() {
		if (protectionlvl==null) return "No information available";
		return protectionlvl.getName();
	}
	
	
	
	@Override
	public String toString() {
		return this.permissionName;
	}
	
}
