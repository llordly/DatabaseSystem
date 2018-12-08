import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Administrator {
	private Connection connection;
	private String admSsn;

	private Statement stmt;
	private PreparedStatement psmt;
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

	// check whether User exists
	public boolean checkUser(String usrSsn) throws SQLException {
		String sql = "SELECT count(*) FROM User WHERE Usr_ssn = ?";
		psmt = connection.prepareStatement(sql);
		psmt.setString(1, usrSsn);
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
		String sql = "SELECT count(*) FROM Account WHERE Assn = ? AND Account_num = ?";
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

	// add client
	public boolean addClient(Scanner sc) throws SQLException, NoSuchElementException {
		System.out.println("type client information separated by space");
		System.out.println("ex) Usr_ssn Fname Lname Phone_num Sex Birth_date");
		String keyBoard = sc.nextLine();
		StringTokenizer st = new StringTokenizer(keyBoard);
		
		String sql = "INSERT INTO User values(?, ?, ?, ?, ?, ?)";
		psmt = connection.prepareStatement(sql);
		psmt.setString(1, st.nextToken());
		psmt.setString(2, st.nextToken());
		psmt.setString(3, st.nextToken());
		psmt.setInt(4, Integer.parseInt(st.nextToken()));
		psmt.setString(5, st.nextToken());
		psmt.setString(6, st.nextToken());

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
			String createdDate = String.valueOf(rs.getDate("Created_date"));
			String money = String.valueOf(rs.getInt("Money"));
			System.out.println("Account_num : " + accountNum + " Account_type : " + accountType + " Created_date : "
					+ createdDate + " Balance : " + money);
		}
	}

	public boolean addClientAccount(String usrSsn, String accountType) throws SQLException {
		String query1 = "SELECT Account_num FROM Account ORDER BY Account_num DESC";
		stmt = connection.createStatement();
		rs = stmt.executeQuery(query1);
		rs.next();
		int lastAccount = Integer.parseInt(rs.getString("Account_num"));
		lastAccount += 1;
		
		String query2 = "INSERT INTO Account values(?, ?, ?, ?, ?, 0)";
		
		psmt = connection.prepareStatement(query2);
		psmt.setString(1, String.valueOf(lastAccount));
		psmt.setString(2, admSsn);
		psmt.setString(3, usrSsn);
		psmt.setString(4, accountType);
		psmt.setString(5, todayDate());
		
		int result = psmt.executeUpdate();

		if (result > 0)
			return true;
		else
			return false;
	}
	
	public String todayDate() {
		Date today = new Date();
		
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
		
		return String.valueOf(date.format(today));
	}
}
