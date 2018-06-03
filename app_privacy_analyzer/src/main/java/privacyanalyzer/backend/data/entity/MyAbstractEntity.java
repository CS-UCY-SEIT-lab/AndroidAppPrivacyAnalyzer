package privacyanalyzer.backend.data.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

@MappedSuperclass
public class MyAbstractEntity implements Serializable {

	
	@Id
	@GeneratedValue
	private Long id;

	  @Column(name = "created_at")
	  public Date createdAt;
	  
	  
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isNew() {
		return id == null;
	}

	public Long getId() {
		return id;
	}
	

	 
	  @PrePersist
	  void createdAt() {
	    this.createdAt = new Date();
	  }

	@Override
	public int hashCode() {
		if (id == null) {
			return super.hashCode();
		}

		return 31 + id.hashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (id == null) {
			// New entities are only equal if the instance if the same
			return super.equals(other);
		}

		if (this == other) {
			return true;
		}
		if (!(other instanceof MyAbstractEntity)) {
			return false;
		}
		return id.equals(((MyAbstractEntity) other).id);
	}
}
