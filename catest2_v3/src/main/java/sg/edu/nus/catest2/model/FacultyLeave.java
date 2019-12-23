package sg.edu.nus.catest2.model;

import java.time.LocalDate;

import javax.persistence.*;

@Entity
@Table(name="facultyleave")
public class FacultyLeave {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int leaveId;
	@ManyToOne(fetch=FetchType.EAGER, optional=false)
	@JoinColumn(name = "facultyId", nullable = false)
	private Faculty faculty;
	private LocalDate leaveStart;
	private LocalDate leaveEnd;
	private String status;
	
	public FacultyLeave() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FacultyLeave(Faculty faculty, LocalDate leaveStart, LocalDate leaveEnd, String status) {
		super();
		this.faculty = faculty;
		this.leaveStart = leaveStart;
		this.leaveEnd = leaveEnd;
		this.status = status;
	}

	public int getLeaveId() {
		return leaveId;
	}

	public void setLeaveId(int leaveId) {
		this.leaveId = leaveId;
	}

	public Faculty getFaculty() {
		return faculty;
	}

	public void setFaculty(Faculty faculty) {
		this.faculty = faculty;
	}

	public LocalDate getLeaveStart() {
		return leaveStart;
	}

	public void setLeaveStart(LocalDate leaveStart) {
		this.leaveStart = leaveStart;
	}

	public LocalDate getLeaveEnd() {
		return leaveEnd;
	}

	public void setLeaveEnd(LocalDate leaveEnd) {
		this.leaveEnd = leaveEnd;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "FacultyLeave [leaveId=" + leaveId + ", faculty=" + faculty + ", leaveStart=" + leaveStart
				+ ", leaveEnd=" + leaveEnd + ", status=" + status + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + leaveId;
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
		FacultyLeave other = (FacultyLeave) obj;
		if (leaveId != other.leaveId)
			return false;
		return true;
	}
	
	

}
