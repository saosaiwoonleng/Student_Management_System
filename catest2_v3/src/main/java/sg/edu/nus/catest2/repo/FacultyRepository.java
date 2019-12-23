package sg.edu.nus.catest2.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sg.edu.nus.catest2.model.*;

public interface FacultyRepository extends JpaRepository<Faculty, Integer>{
	Faculty getByFacultyId(int facultyId);
	
	@Query("select f from Faculty f where f.firstName like %?1% or f.middleName like %?1% "
			+ "or f.surname like %?1% or f.department.departmentName like %?1% "
			+ "or f.email like %?1%")
	List<Faculty> searchFaculty(String name);
	
	@Query("select f from Faculty f where f.department.departmentId = :departmentId")
	List<Faculty> getFacultiesByDepartmentId(@Param("departmentId") int departmentId);
	
	
	

}
