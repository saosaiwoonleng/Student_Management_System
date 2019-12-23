package sg.edu.nus.catest2.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name="students")
public class Student {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int studentId;
	@NotBlank(message = "Name is mandatory")
	private String firstName;
	private String middleName;
	private String surname;
	private int mobileNum;
	private String address;
	@NotBlank(message = "Email is mandatory")
	private String email;
	private String gender;
	private int age;
	
	public Student() {
		super();
	}

	public Student(String firstName, String middleName, String surname, int mobileNum, String address, String email,
			String gender, int age) {
		super();
		this.firstName = firstName;
		this.middleName = middleName;
		this.surname = surname;
		this.mobileNum = mobileNum;
		this.address = address;
		this.email = email;
		this.gender = gender;
		this.age = age;
	}

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
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

	public int getMobileNum() {
		return mobileNum;
	}

	public void setMobileNum(int mobileNum) {
		this.mobileNum = mobileNum;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "Student [studentId=" + studentId + ", firstName=" + firstName + ", middleName=" + middleName
				+ ", surname=" + surname + ", mobileNum=" + mobileNum + ", address=" + address + ", email=" + email
				+ ", gender=" + gender + ", age=" + age + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + studentId;
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
		Student other = (Student) obj;
		if (studentId != other.studentId)
			return false;
		return true;
	}
	
}
