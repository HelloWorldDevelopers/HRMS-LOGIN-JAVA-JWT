package hrms.rnt.login.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hrms.rnt.login.model.EmployeeMaster;
import hrms.rnt.login.projection.EmployeeMasterProjection;

public interface EmployeeMasterRepo extends JpaRepository<EmployeeMaster, Integer> {
	
	EmployeeMasterProjection findByUserName(String userId);
	
	@Query(value = "select profile_picture from employee_profile where staff_id = ?", nativeQuery = true)
	byte[] getProfilePicture(int staffId);
	
	@Query(value = "SELECT rm.role from user_role ur, role_master rm, employee_master em "
			+ "WHERE ur.role_id = rm.role_id AND "
			+ "em.staff_id = ur.staff_id AND ur.staff_id = ?", nativeQuery = true)
	List<String> getEmployeeRoles(int staffId);
}
