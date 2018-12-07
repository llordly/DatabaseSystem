import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Administrator {
	private Connection connection;
	private String admSsn;

	private PreparedStatement psmt;
	private Statement stmt;
	private ResultSet rs;

	public Administrator(Connection connection, String admSsn) throws SQLException {
		this.connection = connection;
		this.admSsn = admSsn;
	}

	// check whether administrator exists
	public boolean checkAdmin() throws SQLException {
		String sql = "SELECT count(*) FROM Administrator WHERE Adm_ssn = ?";
		psmt = connection.prepareStatement(sql);
		psmt.setString(1, admSsn);
		rs = psmt.executeQuery();
		rs.next();

		if (rs.getInt(1) > 0)
			return true;
		else {
			System.out.println("There are no one who matched that ssn");
			return false;
		}
			
	}

	// check whether account exists
	public boolean checkAccount(String account) throws SQLException {
		String sql = "SELECT count(*) FROM Account WHERE Adm_ssn = ? AND Account_num = ?";
		psmt = connection.prepareStatement(sql);
		psmt.setString(1, admSsn);
		psmt.setString(2, account);
		rs = psmt.executeQuery();
		rs.next();

		if (rs.getInt(1) > 0)
			return true;
		else {
			System.out.println("There are no one which matched that account");
			return false;
		}
	}
	
	// print adiministrator's branch information
	public void printBranchInfo() throws SQLException {
		String sql = "SELECT Branch_name, Address FROM Branch, Administrator WHERE Branch_num = Bnum AND Adm_ssn = ?";
		psmt = connection.prepareStatement(sql);
		psmt.setString(1, admSsn);
		rs = psmt.executeQuery();
		while (rs.next()) {
			String bName = rs.getString("Branch_name");
			String address = rs.getString("Address");
			System.out.println("Branch name : " + bName + " Address : " + address);
		}
	}

	// delete client's account
	public boolean deleteAccount(String usrAccount) throws SQLException {
		String sql = "DELETE FROM Account where Account_num = ?";
		psmt = connection.prepareStatement(sql);
		psmt.setString(1, usrAccount);
		int result = psmt.executeUpdate();

		if (result > 0)
			return true;
		else
			return false;
	}

	// add client's account
	public boolean addAccount(Scanner sc) throws SQLException {
		System.out.println("type client information separated by space");
		System.out.println("ex) Usr_ssn Fname Lname Phone_num Sex Birth_date");
		String keyBoard = sc.nextLine();
		StringTokenizer st = new StringTokenizer(keyBoard);

		String sql = "INSERT INTO Account values(?, ?, ?, ?, ?, ?)";
		psmt = connection.prepareStatement(sql);
		psmt.setString(1, st.nextToken());
		psmt.setString(2, st.nextToken());
		psmt.setString(3, st.nextToken());
		psmt.setInt(4, Integer.parseInt(st.nextToken()));
		psmt.setString(5, st.nextToken());
		psmt.setTime(6, Time.valueOf(st.nextToken()));

		int result = psmt.executeUpdate();

		if (result > 0)
			return true;
		else
			return false;
	}

	// print client's account information
	public void showClientAccount(String usrSsn) throws SQLException {
		String sql = "SELECT Account_num, Account_type, Created_date, Money FROM Account, Administrator where Adm_ssn = Assn AND Ussn = ?";
		psmt = connection.prepareStatement(sql);
		psmt.setString(1, usrSsn);
		rs = psmt.executeQuery();
		while (rs.next()) {
			String accountNum = rs.getString("Account_num");
			String accountType = rs.getString("Account_type");
			String createdDate = String.valueOf(rs.getTime("Created_date"));
			String money = String.valueOf(rs.getInt("Money"));
			System.out.println("Account_num : " + accountNum + " Account_type : " + accountType + " Created_date : "
					+ createdDate + " Balance : " + money);
		}
	}
}
