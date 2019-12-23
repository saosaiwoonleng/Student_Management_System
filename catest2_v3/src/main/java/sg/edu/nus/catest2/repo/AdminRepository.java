package sg.edu.nus.catest2.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import sg.edu.nus.catest2.model.*;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
	Admin getByAdminId(int adminId);
	
	@Query("select a from Admin a where a.firstName like %?1% or a.middleName like %?1% "
			+ "or a.surname like %?1% or a.email like %?1%")
	List<Admin> searchAdmin(String name);
	

}
