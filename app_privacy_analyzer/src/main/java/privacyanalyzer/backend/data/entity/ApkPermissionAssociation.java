package privacyanalyzer.backend.data.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.persistence.Table;

@Table(name="apkPermission",
	    uniqueConstraints=
	        @UniqueConstraint(columnNames={"apk_id", "permission_id","permission_type"})
	)
@Entity
public class ApkPermissionAssociation extends MyAbstractEntity {

	
	@ManyToOne
	@NotNull
	private ApkModel apk;
	
	@ManyToOne
	@NotNull
	private Permission permission;
	
	@Column(name = "permission_type")
	@NotNull
	private String permissionType;

	
	
	
	
	/**
	 * 
	 */
	public ApkPermissionAssociation() {
	}





	/**
	 * @param apk
	 * @param permission
	 * @param permissionType
	 */
	public ApkPermissionAssociation(ApkModel apk, Permission permission, String permissionType) {
		super();
		this.apk = apk;
		this.permission = permission;
		this.permissionType = permissionType;
	}





	public ApkModel getApk() {
		return apk;
	}





	public void setApk(ApkModel apk) {
		this.apk = apk;
	}





	public Permission getPermission() {
		return permission;
	}





	public void setPermission(Permission permission) {
		this.permission = permission;
	}





	public String getPermissionType() {
		return permissionType;
	}





	public void setPermissionType(String permissionType) {
		this.permissionType = permissionType;
	}
	
	
	
}
