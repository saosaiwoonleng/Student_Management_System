package sg.edu.nus.catest2.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name="admins")
public class Admin {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int adminId;
	@NotBlank(message = "Name is mandatory")
	private String firstName;
	private String middleName;
	private String surname;
	private int mobileNum;
	@NotBlank(message = "Email is mandatory")
	private String email;
	
	public Admin() {
		super();
	}

	public Admin(String firstName, String middleName, String surname, int mobileNum, String email) {
		super();
		this.firstName = firstName;
		this.middleName = middleName;
		this.surname = surname;
		this.mobileNum = mobileNum;
		this.email = email;
	}

	public int getAdminId() {
		return adminId;
	}

	public void setAdminId(int adminId) {
		this.adminId = adminId;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Admin [adminId=" + adminId + ", firstName=" + firstName + ", middleName=" + middleName + ", surname="
				+ surname + ", mobileNum=" + mobileNum + ", email=" + email + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + adminId;
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
		Admin other = (Admin) obj;
		if (adminId != other.adminId)
			return false;
		return true;
	}
	
}
