package sg.edu.nus.catest2.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sg.edu.nus.catest2.model.*;

public interface UserRepository extends JpaRepository<User, Integer> {
	public User findByUserName(String username);
	
	@Query("select u from User u where u.adminId = 0 and u.facultyId = 0")
	List<User> getAllStudentUsers();
	
	@Query("select u from User u where u.studentId = 0 and u.facultyId = 0")
	List<User> getAllAdminUsers();
	
	@Query("select u from User u where u.adminId = 0 and u.studentId = 0")
	List<User> getAllFacultyUsers();
	
	User getByFacultyId(int facultyId);
	
	User getByStudentId(int studentId);
	
	User getByAdminId(int adminId);
	
	User getUserByUserId(int userId);

}
