package sg.edu.nus.catest2.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import sg.edu.nus.catest2.model.*;

public interface StudentRepository extends JpaRepository<Student, Integer>{
	
	@Query("select s from Student s where s.firstName like %?1% or s.middleName like %?1% "
			+ "or s.surname like %?1% or s.address like %?1% or s.email like %?1%")
	List<Student> searchStudent(String name);
	
	Student getStudentByStudentId(int studentId);
	
	//reo
	Student getByStudentId(int Student);

}
