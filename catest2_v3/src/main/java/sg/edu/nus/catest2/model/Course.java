package sg.edu.nus.catest2.model;

import java.time.LocalDate;

import javax.persistence.*;

@Entity
@Table(name="courses")
public class Course {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int courseId;
	private String courseCode;
	private String courseName;
	private LocalDate courseStart;
	private LocalDate courseEnd;
	private int courseUnit;
	private int courseSize;
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name = "departmentId", nullable = false)
	private Department department;
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name = "facultyId", nullable = false)
	private Faculty faculty;
	
	public Course() {
		super();
	}

	public Course(String courseCode, String courseName, LocalDate courseStart, LocalDate courseEnd,
			int courseUnit, int courseSize, Department department, Faculty faculty) {
		super();
		this.courseCode = courseCode;
		this.courseName = courseName;
		this.courseStart = courseStart;
		this.courseEnd = courseEnd;
		this.courseUnit = courseUnit;
		this.courseSize = courseSize;
		this.department = department;
		this.faculty = faculty;
	}

	public void setCourseStart(LocalDate courseStart) {
		this.courseStart = courseStart;
	}

	public void setCourseEnd(LocalDate courseEnd) {
		this.courseEnd = courseEnd;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public LocalDate getCourseStart() {
		return courseStart;
	}

	public LocalDate getCourseEnd() {
		return courseEnd;
	}

	public int getCourseUnit() {
		return courseUnit;
	}

	public void setCourseUnit(int courseUnit) {
		this.courseUnit = courseUnit;
	}

	public int getCourseSize() {
		return courseSize;
	}

	public void setCourseSize(int courseSize) {
		this.courseSize = courseSize;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Faculty getFaculty() {
		return faculty;
	}

	public void setFaculty(Faculty faculty) {
		this.faculty = faculty;
	}

	@Override
	public String toString() {
		return "Course [courseId=" + courseId + ", courseCode=" + courseCode + ", courseName=" + courseName
				+ ", courseStart=" + courseStart + ", courseEnd=" + courseEnd + ", courseUnit=" + courseUnit
				+ ", courseSize=" + courseSize + ", department=" + department + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + courseId;
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
		Course other = (Course) obj;
		if (courseId != other.courseId)
			return false;
		return true;
	}
	
}
