package privacyanalyzer.backend.data.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Table(name="apkTracker"
,uniqueConstraints=
    @UniqueConstraint(columnNames={"apk_id", "tracker_id"})
)
@Entity
public class ApkTrackerAssociation extends MyAbstractEntity{

	

	@ManyToOne
	@NotNull
	private ApkModel apk;
	
	@ManyToOne
	@NotNull
	private Tracker tracker;

	/**
	 * 
	 */
	public ApkTrackerAssociation() {
		super();
	}

	/**
	 * @param apk
	 * @param tracker
	 */
	public ApkTrackerAssociation(ApkModel apk, Tracker tracker) {
		super();
		this.apk = apk;
		this.tracker = tracker;
	}

	public ApkModel getApk() {
		return apk;
	}

	public void setApk(ApkModel apk) {
		this.apk = apk;
	}

	public Tracker getTracker() {
		return tracker;
	}

	public void setTracker(Tracker tracker) {
		this.tracker = tracker;
	}
	
	
	
}
