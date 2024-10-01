package hrms.rnt.login.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "employee_master")
public class EmployeeMaster {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "staff_id")
	int staffId;
	
	@Column(name = "user_id")
	String userName;
	
	@Column(name = "password")
	String password;
	
	@Column(name = "f_name")
	String firstName;
	
	@Column(name = "l_name")
	String lastName;
	
	@Column(name = "email_id")
	String emailId;

}
