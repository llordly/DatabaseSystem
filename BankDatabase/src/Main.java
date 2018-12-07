import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Connection connection;
		Statement statement;
		ResultSet resultSet;
		
		String driverName = "com.mysql.cj.jdbc.Driver";
		String url = "jdbc:mysql://localhost:3306/bank?characterEncoding=UTF-8&serverTimezone=UTC";
		String user = "root";
		String password = "0317";
		
		try {
			Class.forName(driverName);
			connection = DriverManager.getConnection(url, user, password);
			System.out.println("connected");
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
