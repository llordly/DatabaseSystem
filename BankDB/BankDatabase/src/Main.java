import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Connection connection = null;
		
		String driverName = "com.mysql.cj.jdbc.Driver";
		String user = "root";
		String password = "0317";
		String portNum = "3306";
		
		Scanner keyboard = new Scanner(System.in);
		System.out.print("port number : ");
		portNum = keyboard.nextLine().trim();
		System.out.print("user : ");
		user = keyboard.nextLine().trim();
		System.out.print("password : ");
		password = keyboard.nextLine().trim();
		
		String url = "jdbc:mysql://localhost:" + portNum + "/bank?characterEncoding=UTF-8&"
				+ "serverTimezone=UTC";
		
		try {
			Class.forName(driverName);
			connection = DriverManager.getConnection(url, user, password);
			System.out.println("connected");
			
		} catch (ClassNotFoundException e) {
			System.out.println("driver loading failed");
		} catch (SQLException e) {
			System.out.println("connection failed");
		} 
		
		// start program
		RunBank rb = new RunBank(connection, keyboard);
		
		try {
			rb.run();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// close program
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
