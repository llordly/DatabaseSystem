import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class User {
	private Connection connection;
	private String usrSsn;

	private PreparedStatement psmt;
	private Statement stmt;
	private ResultSet rs;

	public User(Connection connection, String usrSsn) {
		this.connection = connection;
		this.usrSsn = usrSsn;
	}

	// show client information
	public void showMyInfo() throws SQLException {
		String sql = "SELECT Fname, Lname, Phone_num, Sex, Birth_date FROM User where Usr_ssn = ?";
		psmt = connection.prepareStatement(sql);
		psmt.setString(1, usrSsn);
		rs = psmt.executeQuery();

		String fName = rs.getString("Fname");
		String lName = rs.getString("Lname");
		String phoneNum = rs.getString("Phone_num");
		String sex = rs.getString("Sex");
		String birthDate = String.valueOf(rs.getTime("Birth_date"));

		System.out.println("Fname : " + fName + " Lname : " + lName + " Phone_num : " + phoneNum + " Sex : " + sex
				+ " Birth_date : " + birthDate);
	}

	// show client account
	public void showMyAccount() throws SQLException {
		String sql = "SELECT Name, Account_num, Account_type, Created_date, Money FROM Account, Administrator where Adm_ssn = Assn AND Ussn = ?";
		psmt = connection.prepareStatement(sql);
		psmt.setString(1, usrSsn);
		rs = psmt.executeQuery();
		while (rs.next()) {
			String admName = rs.getString("Name");
			String accountNum = rs.getString("Account_num");
			String accountType = rs.getString("Account_type");
			String createdDate = String.valueOf(rs.getTime("Created_date"));
			String money = String.valueOf(rs.getInt("Money"));
			System.out.println("Adm_name : " + admName + " Account_num : " + accountNum + " Account_type : "
					+ accountType + " Created_date : " + createdDate + " Balance : " + money);
		}
	}

	// show client card
	public void showMyCard() throws SQLException {
		String sql = "SELECT Card_num, Valid_year, Valid_month, Card_type, Limits FROM Card, User where Usr_ssn = Ussn AND Ussn = ?";
		psmt = connection.prepareStatement(sql);
		psmt.setString(1, usrSsn);
		rs = psmt.executeQuery();
		while (rs.next()) {
			String cardNum = rs.getString("Card_num");
			String validYear = rs.getString("Valid_year");
			String validMonth = rs.getString("Valid_month");
			String cardType = rs.getString("Card_type");
			String limits = rs.getString("Limits");
			System.out.println("Card_num : " + cardNum + " Valid_year : " + validYear + " Valid_month : " + validMonth
					+ " Card_type : " + cardType + " Limits : " + limits);
		}
	}

	// deposit money to usr account
	public boolean deposit(int amount, String accountNum) throws SQLException {

		String query1 = "UPDATE Account SET Money = Money + ? WHERE Usr_ssn = ? AND Account_num = ?";
		psmt = connection.prepareStatement(query1);
		psmt.setInt(1, amount);
		psmt.setString(2, usrSsn);
		psmt.setString(3, accountNum);

		if (psmt.executeUpdate() <= 0) {
			return false;
		}

		// count number of transaction
		String query2 = "SELECT count(*) FROM Money WHERE Account_num = ?";
		psmt = connection.prepareStatement(query2);
		psmt.setString(1, accountNum);
		rs = psmt.executeQuery();
		int count = rs.getInt(1);

		// insert deposit transaction
		String query3 = "INSERT INTO Money values(?, ?, ?, ?)";
		psmt = connection.prepareStatement(query2);
		psmt.setInt(1, count);
		psmt.setString(2, accountNum);
		psmt.setInt(3, amount);
		psmt.setString(4, "deposit");

		if (psmt.executeUpdate() <= 0) {
			return false;
		}

		return true;
	}

	// withdraw money from usr account
	public boolean withdraw(int amount, String accountNum) throws SQLException {

		// if balance is negative, return false
		String sql = "SELECT Money FROM Account WHERE Account_num = ?";
		psmt = connection.prepareStatement(sql);
		psmt.setString(1, accountNum);
		rs = psmt.executeQuery();
		int balance = rs.getInt(1);
		if (balance - amount < 0)
			return false;

		String query1 = "UPDATE Account SET Money = Money - ? WHERE Usr_ssn = ? AND Account_num = ?";
		psmt = connection.prepareStatement(query1);
		psmt.setInt(1, amount);
		psmt.setString(2, usrSsn);
		psmt.setString(3, accountNum);

		if (psmt.executeUpdate() <= 0) {
			return false;
		}

		// count number of transaction
		String query2 = "SELECT count(*) FROM Money WHERE Account_num = ?";
		psmt = connection.prepareStatement(query2);
		psmt.setString(1, accountNum);
		rs = psmt.executeQuery();
		int count = rs.getInt(1);

		// insert withdraw transaction
		String query3 = "INSERT INTO Money values(?, ?, ?, ?)";
		psmt = connection.prepareStatement(query2);
		psmt.setInt(1, count);
		psmt.setString(2, accountNum);
		psmt.setInt(3, amount);
		psmt.setString(4, "withdraw");

		if (psmt.executeUpdate() <= 0) {
			return false;
		}

		return true;
	}

	// remittance money from usr1 to usr2
	public boolean remittance(int amount, String fromAccount, String toAccount) throws SQLException {

		// if balance is negative, return false
		String sql = "SELECT Money FROM Account WHERE Account_num = ?";
		psmt = connection.prepareStatement(sql);
		psmt.setString(1, fromAccount);
		rs = psmt.executeQuery();
		int balance = rs.getInt(1);
		if (balance - amount < 0)
			return false;

		String fromQuery1 = "UPDATE Account SET Money = Money - ? WHERE Usr_ssn = ? AND Account_num = ?";
		psmt = connection.prepareStatement(fromQuery1);
		psmt.setInt(1, amount);
		psmt.setString(2, usrSsn);
		psmt.setString(3, fromAccount);

		if (psmt.executeUpdate() <= 0) {
			return false;
		}

		// count number of transaction
		String fromQuery2 = "SELECT count(*) FROM Money WHERE Account_num = ?";
		psmt = connection.prepareStatement(fromQuery2);
		psmt.setString(1, fromAccount);
		rs = psmt.executeQuery();
		int fromCount = rs.getInt(1);

		// insert remittance transaction
		String fromQuery3 = "INSERT INTO Money values(?, ?, ?, ?)";
		psmt = connection.prepareStatement(fromQuery3);
		psmt.setInt(1, fromCount);
		psmt.setString(2, fromAccount);
		psmt.setInt(3, amount);
		psmt.setString(4, "remittance");

		if (psmt.executeUpdate() <= 0) {
			return false;
		}
		
		String toQuery1 = "UPDATE Account SET Money = Money + ? WHERE Usr_ssn = ? AND Account_num = ?";
		psmt = connection.prepareStatement(toQuery1);
		psmt.setInt(1, amount);
		psmt.setString(2, usrSsn);
		psmt.setString(3, toAccount);

		if (psmt.executeUpdate() <= 0) {
			return false;
		}

		// count number of transaction
		String toQuery2 = "SELECT count(*) FROM Money WHERE Account_num = ?";
		psmt = connection.prepareStatement(toQuery2);
		psmt.setString(1, toAccount);
		rs = psmt.executeQuery();
		int toCount = rs.getInt(1);

		// insert remittanced transaction
		String toQuery3 = "INSERT INTO Money values(?, ?, ?, ?)";
		psmt = connection.prepareStatement(toQuery3);
		psmt.setInt(1, toCount);
		psmt.setString(2, fromAccount);
		psmt.setInt(3, amount);
		psmt.setString(4, "remittanced");

		if (psmt.executeUpdate() <= 0) {
			return false;
		}

		return true;
	}
	
	// add loan
	public boolean getLoan(int amount, String accountNum) throws SQLException {
		String query1 = "SELECT count(*) FROM Loan WHERE Ussn = ?";
		psmt = connection.prepareStatement(query1);
		psmt.setString(1, accountNum);
		rs = psmt.executeQuery();
		int count = rs.getInt(1);
		
		String query2 = "INSERT INTO Loan values(?, ?, ?)";
		psmt = connection.prepareStatement(query2);
		psmt.setInt(1, count);
		psmt.setString(2, accountNum);
		psmt.setInt(3, amount);
		
		if (psmt.executeUpdate() <= 0) {
			return false;
		}
		
		return true;
	}
	
	// show loan information of user;
	public void showLoanInfo() throws SQLException {
		String sql = "SELECT Loan_num, Amount FROM Loan, User WHERE Usr_ssn = Ussn AND Ussn = ?";
		psmt = connection.prepareStatement(sql);
		psmt.setString(1, usrSsn);
		rs = psmt.executeQuery();
		while (rs.next()) {
			int loanNum = rs.getInt("Loan_num");
			int amount = rs.getInt("Amount");
			System.out.println("Loan_num : " + String.valueOf(loanNum) + " Amount : " + String.valueOf(amount));
		}
	}
	
}
