package sg.edu.nus.catest2.model;

import javax.persistence.*;

@Entity
@Table(name="grades")
public class Grade {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int gradeId;
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name = "studentId", nullable = false)
	private Student student;
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name = "courseId", nullable = false)
	private Course course;
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name = "facultyId", nullable = false)
	private Faculty faculty;
	private String grade;
	
	public Grade() {
		super();
	}

	public Grade(Student student, Course course, Faculty faculty, String grade) {
		super();
		this.student = student;
		this.course = course;
		this.faculty = faculty;
		this.grade = grade;
	}

	public int getGradeId() {
		return gradeId;
	}

	public void setGradeId(int gradeId) {
		this.gradeId = gradeId;
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

	public Faculty getFaculty() {
		return faculty;
	}

	public void setFaculty(Faculty faculty) {
		this.faculty = faculty;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	@Override
	public String toString() {
		return "Grade [gradeId=" + gradeId + ", student=" + student + ", course=" + course + ", faculty=" + faculty
				+ ", grade=" + grade + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + gradeId;
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
		Grade other = (Grade) obj;
		if (gradeId != other.gradeId)
			return false;
		return true;
	}

}
