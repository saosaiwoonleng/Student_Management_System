package sg.edu.nus.catest2.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sg.edu.nus.catest2.model.*;

public interface GradeRepository extends JpaRepository<Grade, Integer> {
	@Query("select g from Grade g where g.course.courseId = :courseId")
	List<Grade> getGradesByCourseId(@Param("courseId") int courseId);
	
	@Query("select g from Grade g where g.student.studentId = :studentId")
	List<Grade> getGradesByStudentId(@Param("studentId") int studentId);
	
	@Query("select g from Grade g where g.student.studentId = :studentId")
	Grade getGradeByStudentId(@Param("studentId") int studentId);
	
	Grade getGradeByGradeId(int gradeId);
	
	@Query("select g from Grade g where g.course.courseName like %?1% or g.course.courseCode like %?1% or "
			+ "g.student.firstName like %?1% or g.student.middleName like %?1% or g.student.surname like %?1% ")
	List<Grade> searchGrade(String name);
	
	@Query("select g from Grade g where g.student.studentId = :studentId and g.course.courseId = :courseId")
	Grade getGradeByStudentIdAndCourseId(@Param("studentId") int studentId,@Param("courseId") int courseId);
	
	//reo
	@Query(value = "select * from grades where course_Id = ?1 and student_Id = ?2" , nativeQuery=true)
	public Grade findByCourseIdStudentId(int courseId, int studentId);
	
	Grade findByGradeId(int gradeId);
}
