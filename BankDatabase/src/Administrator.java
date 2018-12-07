import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Administrator {
	private Connection connection;
	private Statement stmt;
	private ResultSet rs;

	public Administrator(Connection connection) throws SQLException {
		this.connection = connection;
		stmt = this.connection.createStatement();
	}

	// // print administrator work
	// public void printAdminList() {
	// System.out.println(returnPrev);
	// System.out.println("1. Branch information");
	// System.out.println("2. Admin Client Account");
	// }
	//
	// // print detail work of administrator
	// public void printAdminAccount() {
	// System.out.println(returnPrev);
	// System.out.println("1. Delete Client Account");
	// System.out.println("2. Add Client Account");
	// System.out.println("3. show Client Account");
	// }

	// print adiministrator's branch information
	public void printBranchInfo() throws SQLException {
		rs = stmt.executeQuery("SELECT Branch_name, Address FROM Branch where Branch_num = Bnum AND Adm_ssn = ?");
		while (rs.next()) {
			String bName = rs.getString("Branch_name");
			String address = rs.getString("Address");
			System.out.println("Branch name : " + bName + " Address : " + address);
		}
	}

}
