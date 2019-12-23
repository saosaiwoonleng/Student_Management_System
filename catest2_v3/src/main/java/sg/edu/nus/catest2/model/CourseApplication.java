package sg.edu.nus.catest2.model;

import javax.persistence.*;

@Entity
@Table(name="courseapplication")
public class CourseApplication {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int applicationId;
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name = "studentId", nullable = false)
	private Student student;
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name = "courseId", nullable = false)
	private Course course;
	private String status;
	

	public CourseApplication() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CourseApplication(Student student, Course course, String status) {
		super();
		this.student = student;
		this.course = course;
		this.status = status;
	}

	public int getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(int applicationId) {
		this.applicationId = applicationId;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "CourseApplication [applicationId=" + applicationId + ", student=" + student + ", course=" + course
				+ ", status=" + status + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + applicationId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CourseApplication other = (CourseApplication) obj;
		if (applicationId != other.applicationId)
			return false;
		return true;
	}
	
	

}
