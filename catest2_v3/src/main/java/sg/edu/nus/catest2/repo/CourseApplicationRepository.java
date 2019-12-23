package sg.edu.nus.catest2.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sg.edu.nus.catest2.model.*;

public interface CourseApplicationRepository extends JpaRepository<CourseApplication, Integer> {
	
	@Query("select ca from CourseApplication ca where ca.student.studentId = :studentId")
	List<CourseApplication> getCourseApplicationsByStudentId(@Param("studentId") int studentId);

	
	@Query("select ca from CourseApplication ca where ca.course.courseId = :courseId")
	List<CourseApplication> getCourseApplicationsByCourseId(@Param("courseId") int courseId);
	
	@Query("select ca from CourseApplication ca where ca.course.courseId = :courseId and ca.status = :status")
	List<CourseApplication> getCourseApplicationsByCourseIdAndStatus(@Param("courseId") int courseId,
			@Param("status") String status);
	
	CourseApplication getCourseApplicationByApplicationId(int applicationId);
	
	//List<CourseApplication> getApprovedCourseByStudentId(Student student);
		@Query("select ca from CourseApplication ca where ca.student.studentId = :studentId AND ca.status='Approved'")
		List<CourseApplication> getApprovedCourseByStudentId(@Param("studentId") int studentId);
		
		@Query("select ca from CourseApplication ca where ca.student.studentId = :studentId AND ca.status='Pending'")
		List<CourseApplication> getPendingCourseByStudentId(@Param("studentId")int studentId);

		@Query("SELECT ca FROM CourseApplication ca WHERE ca.course.courseId= :courseId AND ca.student.studentId= :stuId" )
		CourseApplication findByCourseAndStudent(	@Param("courseId") int courseId, 
													@Param("stuId") int stuId);

		@Query("select ca from CourseApplication ca where ca.student.studentId = :studentId AND ca.status='Rejected'")
		List<CourseApplication> getRejectCourseByStudentId(@Param("studentId") int studentId);
		
		//reo method
		@Query(value = "select * from courseapplication where course_Id = ?1 and status='Approved'" , nativeQuery=true)
		public List<CourseApplication> findByCourseId(int courseId);
		
		@Query(value ="select * from courseapplication ca , courses c where c.course_id = ca.course_id and ca.course_Id = ?1 and ca.status='Approved' and c.faculty_Id = ?2", nativeQuery=true)
		public List<CourseApplication> findByCourseIdAndFacultyId(int courseId, int facultyId);  
		
		@Query("select ca from CourseApplication ca where ca.student.firstName like %?1% or ca.student.middleName like %?1% "
				+ "or ca.student.surname like %?1%")
		List<CourseApplication> searchStudentName(String name);

}
