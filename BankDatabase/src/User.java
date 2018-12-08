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

	// check whether User exists
	public boolean checkUser() throws SQLException {
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
		String sql = "SELECT count(*) FROM Account WHERE Ussn = ? AND Account_num = ?";
		psmt = connection.prepareStatement(sql);
		psmt.setString(1, usrSsn);
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

	// show client information
	public void showMyInfo() throws SQLException {
		String sql = "SELECT Fname, Lname, Phone_num, Sex, Birth_date FROM User where Usr_ssn = ?";
		psmt = connection.prepareStatement(sql);
		psmt.setString(1, usrSsn);
		rs = psmt.executeQuery();
		rs.next();
		
		String fName = rs.getString("Fname");
		String lName = rs.getString("Lname");
		String phoneNum = rs.getString("Phone_num");
		String sex = rs.getString("Sex");
		String birthDate = String.valueOf(rs.getDate("Birth_date"));

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
			String createdDate = String.valueOf(rs.getDate("Created_date"));
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
		String query1 = "UPDATE Account SET Money = Money + ? WHERE Account_num = ?";
		psmt = connection.prepareStatement(query1);
		psmt.setInt(1, amount);
		psmt.setString(2, accountNum);
		if (psmt.executeUpdate() <= 0) {
			return false;
		}

		// count number of transaction
		String query2 = "SELECT count(*) FROM Money WHERE Anum = ?";
		psmt = connection.prepareStatement(query2);
		psmt.setString(1, accountNum);
		rs = psmt.executeQuery();
		rs.next();
		
		int count = rs.getInt(1) + 1;

		// insert deposit transaction
		String query3 = "INSERT INTO Money values(?, ?, ?, ?)";
		psmt = connection.prepareStatement(query3);
		psmt.setInt(1, count);
		psmt.setString(2, accountNum);
		psmt.setInt(3, amount);
		psmt.setString(4, "Deposit");

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
		rs.next();
		
		int balance = rs.getInt(1);
		if (balance - amount < 0)
			return false;

		String query1 = "UPDATE Account SET Money = Money - ? WHERE Account_num = ?";
		psmt = connection.prepareStatement(query1);
		psmt.setInt(1, amount);
		psmt.setString(2, accountNum);

		if (psmt.executeUpdate() <= 0) {
			return false;
		}

		// count number of transaction
		String query2 = "SELECT count(*) FROM Money WHERE Anum = ?";
		psmt = connection.prepareStatement(query2);
		psmt.setString(1, accountNum);
		rs = psmt.executeQuery();
		rs.next();
		
		int count = rs.getInt(1) + 1;

		// insert withdraw transaction
		String query3 = "INSERT INTO Money values(?, ?, ?, ?)";
		psmt = connection.prepareStatement(query3);
		psmt.setInt(1, count);
		psmt.setString(2, accountNum);
		psmt.setInt(3, amount);
		psmt.setString(4, "Withdraw");

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
		rs.next();
		
		int balance = rs.getInt(1);
		if (balance - amount < 0)
			return false;

		String fromQuery1 = "UPDATE Account SET Money = Money - ? WHERE Account_num = ?";
		psmt = connection.prepareStatement(fromQuery1);
		psmt.setInt(1, amount);
		psmt.setString(2, fromAccount);

		if (psmt.executeUpdate() <= 0) {
			return false;
		}

		// count number of transaction
		String fromQuery2 = "SELECT count(*) FROM Money WHERE Anum = ?";
		psmt = connection.prepareStatement(fromQuery2);
		psmt.setString(1, fromAccount);
		rs = psmt.executeQuery();
		rs.next();
		
		int fromCount = rs.getInt(1) + 1;

		// insert remittance transaction
		String fromQuery3 = "INSERT INTO Money values(?, ?, ?, ?)";
		psmt = connection.prepareStatement(fromQuery3);
		psmt.setInt(1, fromCount);
		psmt.setString(2, fromAccount);
		psmt.setInt(3, amount);
		psmt.setString(4, "Remittance");

		if (psmt.executeUpdate() <= 0) {
			return false;
		}

		String toQuery1 = "UPDATE Account SET Money = Money + ? WHERE Account_num = ?";
		psmt = connection.prepareStatement(toQuery1);
		psmt.setInt(1, amount);
		psmt.setString(2, toAccount);

		if (psmt.executeUpdate() <= 0) {
			return false;
		}

		// count number of transaction
		String toQuery2 = "SELECT count(*) FROM Money WHERE Anum = ?";
		psmt = connection.prepareStatement(toQuery2);
		psmt.setString(1, toAccount);
		rs = psmt.executeQuery();
		rs.next();
		
		int toCount = rs.getInt(1) + 1;

		// insert remittanced transaction
		String toQuery3 = "INSERT INTO Money values(?, ?, ?, ?)";
		psmt = connection.prepareStatement(toQuery3);
		psmt.setInt(1, toCount);
		psmt.setString(2, toAccount);
		psmt.setInt(3, amount);
		psmt.setString(4, "Remittanced");

		if (psmt.executeUpdate() <= 0) {
			return false;
		}

		return true;
	}

	// add loan
	public boolean getLoan(int amount) throws SQLException {
		String query1 = "SELECT count(*) FROM Loan";
		psmt = connection.prepareStatement(query1);
		rs = psmt.executeQuery();
		rs.next();
		
		int count = rs.getInt(1) + 1;
		
		String query2 = "INSERT INTO Loan values(?, ?, ?)";
		psmt = connection.prepareStatement(query2);
		psmt.setInt(1, count);
		psmt.setString(2, usrSsn);
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
		int count = 0;
		while (rs.next()) {
			count++;
			int loanNum = rs.getInt("Loan_num");
			int amount = rs.getInt("Amount");
			System.out.println("Loan_num : " + String.valueOf(loanNum) + " Amount : " + String.valueOf(amount));
		}
		if (count == 0) System.out.println("You don't have any loan");
	}

}
