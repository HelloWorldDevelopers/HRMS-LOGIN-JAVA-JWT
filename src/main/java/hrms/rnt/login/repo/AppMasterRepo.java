package hrms.rnt.login.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hrms.rnt.login.model.ApplicationMaster;

public interface AppMasterRepo extends JpaRepository<ApplicationMaster, Integer> {

	List<ApplicationMaster> findAllByDeletedByIsNull();

}
