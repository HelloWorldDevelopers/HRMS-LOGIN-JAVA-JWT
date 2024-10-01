package hrms.rnt.login.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="application_master")
public class ApplicationMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "app_id")
	int appId;
	
	@Column(name = "app_name")
	String appName;
	
	@Column(name = "app_icon")
	String appIcon;
	
	@Column(name = "deleted_by")
	LocalDateTime deletedBy;
	
}
