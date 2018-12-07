import java.sql.Connection;

public class User {
	private Connection connection;
	
	public User(Connection connection) {
		this.connection = connection;
	}

	//show client account
		public void showClientAccount() {
			// retreive account by ssn or name
		}
}
