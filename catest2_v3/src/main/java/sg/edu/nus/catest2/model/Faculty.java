package sg.edu.nus.catest2.model;

import javax.persistence.*;

@Entity
@Table(name="faculties")
public class Faculty {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int facultyId;
	private String firstName;
	private String middleName;
	private String surname;
	private String email;
	private int mobileNum;
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name = "departmentId", nullable = false)
	private Department department;

	public Faculty() {
		super();
	}
	public Faculty(String firstName, String middleName, String surname, String email, int mobileNum,
			Department department) {
		super();
		this.firstName = firstName;
		this.middleName = middleName;
		this.surname = surname;
		this.email = email;
		this.mobileNum = mobileNum;
		this.department = department;
	}
	public int getFacultyId() {
		return facultyId;
	}
	public void setFacultyId(int facultyId) {
		this.facultyId = facultyId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getMiddleName() {
		return middleName;
	}
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getMobileNum() {
		return mobileNum;
	}
	public void setMobileNum(int mobileNum) {
		this.mobileNum = mobileNum;
	}
	public Department getDepartment() {
		return department;
	}
	public void setDepartment(Department department) {
		this.department = department;
	}
	@Override
	public String toString() {
		return "Faculty [facultyId=" + facultyId + ", firstName=" + firstName + ", middleName=" + middleName
				+ ", surname=" + surname + ", email=" + email + ", mobileNum=" + mobileNum + ", department="
				+ department + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + facultyId;
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
		Faculty other = (Faculty) obj;
		if (facultyId != other.facultyId)
			return false;
		return true;
	}
	
}
