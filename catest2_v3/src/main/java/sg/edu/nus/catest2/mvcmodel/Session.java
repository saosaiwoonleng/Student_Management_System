package sg.edu.nus.catest2.mvcmodel;

public class Session {
	private String userType;
	private int sessionId;
	public Session() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Session(String userType, int sessionId) {
		super();
		this.userType = userType;
		this.sessionId = sessionId;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public int getSessionId() {
		return sessionId;
	}
	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}
	@Override
	public String toString() {
		return "Session [userType=" + userType + ", sessionId=" + sessionId + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + sessionId;
		result = prime * result + ((userType == null) ? 0 : userType.hashCode());
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
		Session other = (Session) obj;
		if (sessionId != other.sessionId)
			return false;
		if (userType == null) {
			if (other.userType != null)
				return false;
		} else if (!userType.equals(other.userType))
			return false;
		return true;
	}
	
	

}
