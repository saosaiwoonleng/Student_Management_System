package sg.edu.nus.catest2.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import sg.edu.nus.catest2.model.*;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
	@Query("select d from Department d where d.departmentName like %?1%")
	List<Department> searchDepartment(String name);
	
	Department getDepartmentByDepartmentId(int departmentId);
	
}
