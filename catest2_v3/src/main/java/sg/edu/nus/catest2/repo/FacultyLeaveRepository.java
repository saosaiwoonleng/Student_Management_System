package sg.edu.nus.catest2.repo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sg.edu.nus.catest2.model.*;

public interface FacultyLeaveRepository extends JpaRepository<FacultyLeave, Integer> {	
	@Query("select fl from FacultyLeave fl where fl.faculty.facultyId = :facultyId")
	List<FacultyLeave> getFacultyLeaveByFacultyId(@Param("facultyId") int facultyId);
	
	FacultyLeave getFacultyLeaveByLeaveId(int leaveId);
	
	@Query("SELECT fl FROM FacultyLeave fl WHERE fl.faculty.facultyId = :facultyId AND fl.status='Approved'")
	ArrayList<FacultyLeave> getApprovedFacultyLeaveByFacultyId(@Param("facultyId") int facultyId);
	
	@Query("SELECT fl FROM FacultyLeave fl WHERE fl.faculty.facultyId = :facultyId AND fl.status='Rejected'")
	ArrayList<FacultyLeave> getRejectedFacultyLeaveByFacultyId(@Param("facultyId") int facultyId);
	
	@Query("SELECT fl FROM FacultyLeave fl WHERE fl.faculty.facultyId = :facultyId AND fl.status='Pending'")
	ArrayList<FacultyLeave> getPendingFacultyLeaveByFacultyId(@Param("facultyId") int facultyId);

}
